package org.openkoala.koala.monitor.toolkit.asm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * 对字节码进行修改，采用asm2.1框架
 * 
 * @author leadyu
 * @since Jwebap 0.5
 * @date 2007-9-7
 */
public class ASMInjectorStrategy implements InjectorStrategy {

	protected static final String METHOD_POSTFIX = "_$proxy";

	protected static final String HANDLER_PREFIX = "handle$";

	protected static final String METHOD_PREFIX = "method$";

	protected static final String METHOD_PROXY_PREFIX = "methodProxy$";
 
	private static final Log log = LogFactory.getLog(ASMInjectorStrategy.class);

	/**
	 * 用于寻找类资源的loader
	 */
	private ClassLoader resourceLoader = null;

	/**
	 * 指定loader进行注入
	 */
	private ClassLoader definedLoader = null;

	/**
	 * 是否要注入父类
	 */
	private boolean injectSuper = false;


	public ASMInjectorStrategy(boolean injectSuper) {
		this(null,injectSuper);
	}

	public ASMInjectorStrategy(ClassLoader definedLoader, boolean injectSuper) {
		this.definedLoader = definedLoader;
		// 默认采用上下文ClassLoader，而不能是this.class.getClassLoader()
		resourceLoader = Thread.currentThread().getContextClassLoader();
		this.injectSuper = injectSuper;
	}

	private static Method findResource = null;

	private static Method defineClass = null;

	static {
		ini();
	}

	private static void ini() {
		Class ll = null;
		try {
			ll = Class.forName("java.lang.ClassLoader");
		} catch (ClassNotFoundException e) {
		}

		try {
			findResource = ll.getDeclaredMethod("findResource",
					new Class[] { String.class });
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		findResource.setAccessible(true);

		try {
			defineClass = ll.getDeclaredMethod("defineClass", new Class[] {
					String.class, byte[].class, Integer.TYPE, Integer.TYPE });
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		defineClass.setAccessible(true);
	}

	/**
	 * 类静态注入，并注册handle工厂
	 */
	public Class inject(String className,MethodInjectHandlerFactory factory) throws InjectException {	
		return injectInternal(className,factory);
	}
	
	/**
	 * 类静态注入，并注册handle工厂
	 */
	public Class inject(String className,MethodInjectHandler handle) throws InjectException {
		return injectInternal(className,handle);
	}
	
	
	private void injectHandle(String className,Object hanldeObject){
		//注册注入代码
		if(hanldeObject!=null && hanldeObject instanceof MethodInjectHandler){
			StaticHandleFactory.registerHandle(className,(MethodInjectHandler)hanldeObject);				
		}	
		//注册注入代码
		if(hanldeObject!=null && hanldeObject instanceof MethodInjectHandlerFactory){
			StaticHandleFactory.registerFactory(className,(MethodInjectHandlerFactory)hanldeObject);				
		}
	}
	/**
	 * 类静态增强
	 */
	private Class injectInternal(String className,Object hanldeObject) throws InjectException {
		
		Class clazz = null;
		try {
			injectHandle(className,hanldeObject);
			
			clazz = defineClass(className, this.resourceLoader,
					this.definedLoader,hanldeObject);
		} catch (Exception e) {
			throw new InjectException(className + "注入失败.", e);
		}
		return clazz;

	}

	/**
	 * 生成字节码
	 * 
	 * @param rawClassData
	 * @return
	 * @throws Exception
	 */
	public ClassDef genByteCode(byte[] rawClassData) throws Exception {

		ClassReader reader = new ClassReader(rawClassData);
		ClassWriter classWriter = new ClassWriter(true);
		ClassVisitor target = classWriter;

		ClassInitClassVisitor classInitClassVisitor = new ClassInitClassVisitor(
				target);
		InjectClassVisitor injectClassVisitor = new InjectClassVisitor(
				classInitClassVisitor);
		ASMClassVisitor visitor = new ASMClassVisitor(target,
				injectClassVisitor);

		reader.accept(visitor, false);

		ClassDef clazz = visitor.getType();
		clazz.setByteCode(classWriter.toByteArray());
		
		return clazz;
	}

	/**
	 * 采用父子委托策略为修改的类寻找最合适的ClassLoader进行加载，这样保证
	 * 符合父子委托策略的CloassLoader模型能够正确的加载类，保证不会在模型中 存在多个版本的同名类。
	 * 并且这样对中间件的支持最好，适应复杂的部署模型，比如同一个EAR中的EJB,但是有些不采用这种策略的中间件可能遇到问题。
	 * 
	 * @throws ClassNotFoundException
	 * @throws DefineBytecodeException
	 * @throws GenBytecodeException
	 */
	private Class defineClass(String clazz, ClassLoader resourceLoader,
			ClassLoader definedLoader,Object hanldeObject) throws ClassNotFoundException,
			GenBytecodeException, DefineBytecodeException {

		if(resourceLoader==null){
			throw new ClassNotFoundException(clazz);
		}
		
		try {
			defineClass(clazz, resourceLoader.getParent(), definedLoader,hanldeObject);
		} catch (ClassNotFoundException fe) {
			URL url = null;
			try {
				url = (URL) findResource.invoke(resourceLoader,
						new Object[] { clazz.replace('.', '/') + ".class" });
				
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			if (url == null) {
				throw new ClassNotFoundException(clazz + " not found.");
			} else {
				return defineClass(clazz, url,
						definedLoader == null ? resourceLoader : definedLoader,hanldeObject);
			}
		}

		return null;
	}

	private Class defineClass(String className, URL url,
			ClassLoader definedLoader,Object hanldeObject) throws ClassNotFoundException,
			GenBytecodeException, DefineBytecodeException {

		InputStream is = null;
		try {
			is = url == null ? null : url.openStream();
		} catch (IOException e) {
		}

		if (is == null) {
			throw new ClassNotFoundException("class resource:" + url
					+ " can't open.");
		}

		byte[] b = null;
		try {
			b = new byte[is.available()];

			// 设定buffer大小
			int n = 512;
			byte buffer[] = new byte[n];
			int pos = 0;
			// 读取类文件二进制字节码
			while (n > 0) {
				int t = is.read(buffer, 0, n);
				if (t <= -1) {
					break;
				}
				System.arraycopy(buffer, 0, b, pos, t);
				pos += t;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		ClassDef type=null;
		try {
			type = genByteCode(b);
			// debug，调试生成的字节码
			// FileOutputStream file=new
			// FileOutputStream("d:/jad/"+className.substring(className.lastIndexOf('.')+1)+".class");
			// file.write(r);
			// file.flush();
			// file.close();
		} catch (Throwable e) {
			throw new GenBytecodeException(e.getMessage(), e);
		}

		String superClz=type==null?null:type.getSuperName();
		
		//如果injectSuper=true,先注入父类
		if(injectSuper==true){
			if(superClz!=null){
				if(!(superClz.startsWith("java/") || superClz.startsWith("javax/") || superClz.startsWith("sun/"))){
					try {
						injectInternal(superClz.replace('/', '.'),hanldeObject);
					} catch (InjectException e) {
						log.warn("在注入类"+className+"时对父类:"+superClz+"的注入错误");
						e.printStackTrace();
					}
				}
			}
		}
		
		Object clazz = null;

		try {
			clazz = defineClass.invoke(definedLoader, new Object[] { className,
					type.getByteCode(), new Integer(0), new Integer(type.getByteCode().length) });
		} catch (Throwable e) {
			throw new DefineBytecodeException(type+"注入错误:"+e.getCause().getMessage(), e.getCause());
		}

		return (Class) clazz;
	}

	public String toString() {
		return "ASMInjectorStrategy(uses http://asm.objectweb.org)";
	}

	/**
	 * 包注入内部实现类，用于根据包名寻找包内所有的类
	 * @author leadyu(yu-lead@163.com)
	 * @since Jwebap 0.5
	 * @date  Mar 7, 2008
	 */
	private abstract class DoProcess{
		Class[] doProcess(String pckName,Object[] args) throws InjectException{
			ClassLoader loader=Thread.currentThread().getContextClassLoader();
			String resourceName=pckName.substring(0,pckName.length()-1).replace('.','/');
			Enumeration urls;
			try {
				urls = loader.getResources(resourceName);
			} catch (IOException e) {
				throw new InjectException(resourceName+"不存在.",e);
			}
			List cls=new ArrayList();
			ArrayList clf=new ArrayList();
			int failCount=0;
			while(urls.hasMoreElements()){
				URL url=(URL)urls.nextElement();
				String path=url.getFile();
				File dir=new File(path);
				if(path.indexOf(".jar")>0){
					String jarPath=path.substring(0,path.indexOf(".jar"))+".jar";
					if(jarPath.indexOf("file:/")>-1){
						jarPath=jarPath.substring(jarPath.indexOf("file:/")+6);
					}
					File jarFile=new File(jarPath);
					if(jarFile.exists()){
						JarFile jar;
						try {
							jar = new JarFile(jarFile);
						} catch (IOException e) {
							throw new InjectException(jarFile+"读取不正确.",e);
						}
						Enumeration entries=jar.entries();
						
						
						while(entries.hasMoreElements()){
							JarEntry entry=(JarEntry)entries.nextElement();
							if(!entry.isDirectory()){
								String name=entry.getName();
								if(name!=null && name.indexOf(".class")>-1 && name.indexOf(resourceName)>-1){
									String className=name.substring(0,name.indexOf(".class")).replace('/','.');
									if(!clf.contains(className)){
										clf.add(className);
									}
									
								}
							}
						}
							
						
					}
					
				}else if(dir.exists()){
					String[] files=dir.list();
					for(int i=0;i<files.length;i++){
						if(!files[i].endsWith(".class")){
							continue;
						}
						try {
							String className = pckName.substring(0, pckName
									.length() - 1)
									+ files[i].substring(0, files[i]
											.lastIndexOf('.'));

							if(!clf.contains(className)){
								clf.add(className);
							}
							
						} catch (Exception e) {
							log.warn("className illegal:"+pckName);
						}
						
						
					}
				}
				
			}
			
			//inject each class in files
			for(int i=0;i<clf.size();i++){
				Class clazz=null;
				String className = (String)clf.get(i);
				try {
					clazz = doInject(className, args);
					log.debug("inject class:"+className);
				} catch (Exception e) {
					log.warn(className+"注入失败"+e.getMessage()+",不过这不影响对包的注入.");
					e.printStackTrace();
				}
				
				if(clazz!=null){
					cls.add(clazz);
				}else{
					failCount++;
				}
				
			}
			
			log.debug("inject total "+cls.size()+"class.fail "+failCount);
			Class[] cs=new Class[]{};
			if(cls.size()>0){
				cls.toArray(cs);
			}
			return cs;
		}
		
		abstract Class doInject(String className,Object[] args) throws ClassNotFoundException, InjectException;
	};
	
	public Class[] injectPackage(String pckName, MethodInjectHandlerFactory factory) throws InjectException {
		
		//注入包级handle
		if(factory!=null){
			StaticHandleFactory.registerPkgFactory(pckName,factory);				
		}
		DoProcess process=new DoProcess(){
			Class doInject(String className,Object[] args)throws ClassNotFoundException, InjectException{
				return injectInternal(className,null);
			}		
		};
		return process.doProcess(pckName,null);
	}

	public Class[] injectPackage(String pckName, MethodInjectHandler handle) throws InjectException {
		//注入包级handle
		if(handle!=null){
			StaticHandleFactory.registerPkgHandle(pckName,handle);				
		}
		DoProcess process=new DoProcess(){
			Class doInject(String className,Object[] args)throws ClassNotFoundException, InjectException{
				return injectInternal(className,null);
			}		
		};
		return process.doProcess(pckName,null);
	}
}

/**
 * 生成字节码错误,当调用ASM生成新的字节码时发生错误抛出
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date Mar 1, 2008
 */
class GenBytecodeException extends Exception {
	public GenBytecodeException(Throwable e) {
		super(e);
	}

	public GenBytecodeException(String message, Throwable e) {
		super(message, e);
	}

	public GenBytecodeException(String message) {
		super(message);
	}
}

/**
 * 类定义错误，当字节码定义时发生错误抛出
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date Mar 1, 2008
 */
class DefineBytecodeException extends Exception {
	public DefineBytecodeException(Throwable e) {
		super(e);
	}

	public DefineBytecodeException(String message, Throwable e) {
		super(message, e);
	}

	public DefineBytecodeException(String message) {
		super(message);
	}
}