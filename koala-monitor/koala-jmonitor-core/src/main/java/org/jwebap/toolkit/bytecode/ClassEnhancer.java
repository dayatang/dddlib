package org.jwebap.toolkit.bytecode;

import org.jwebap.toolkit.bytecode.asm.ASMInjectorStrategy;
import org.jwebap.toolkit.bytecode.asm.MethodInjectHandler;
import org.jwebap.toolkit.bytecode.asm.MethodInjectHandlerFactory;
import org.jwebap.toolkit.bytecode.asm.StaticHandleFactory;


/**
 * 类注入增强器
 * @author leadyu
 * @since Jwebap 0.5
 * @date  2007-9-7
 */
public class ClassEnhancer {

	/**
	 * 完成类的字节码增强，但是不指定handle，而由运行时指定(调用StaticHandleFactory进行handle注册)
	 * @param oriClassName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InjectException
	 * @see StaticHandleFactory
	 */
	public  Class createClass(String oriClassName) throws ClassNotFoundException, InjectException{
		return createClass(null,oriClassName,null,false);
	}
	
	public Class createClass(ClassLoader definedLoader,String oriClassName) throws ClassNotFoundException, InjectException{
		return createClass(definedLoader,oriClassName,null,false);
	}
	
	/**
	 * 完成类的静态注入
	 * 
	 * @param definedLoader		指定注入的ClassLoader
	 * @param oriClassName		类名
	 * @param factory			handle工厂
	 * @param injectSuper		是否要注入所有该类的父类
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InjectException
	 */
	public Class createClass(String oriClassName,MethodInjectHandlerFactory factory,ClassLoader definedLoader,boolean injectSuper) throws ClassNotFoundException, InjectException{
		
		Class cls=null;
		
		/**
		 * 采用ASM进行字节码生成策略，最后定义到JVM中,好处是基于jdk14
		 * @todo 实现基于jdk15的instrument机制进行注入
		 */
		InjectorStrategy st=new ASMInjectorStrategy(injectSuper);
		
		cls=st.inject(oriClassName,factory);
		
		return cls;
	}
	
	public  Class createClass(String oriClassName,MethodInjectHandlerFactory factory) throws ClassNotFoundException, InjectException{
		return createClass(oriClassName,factory,false);
	}
	
	/**
	 * 完成类的字节码增强
	 * @param oriClassName 类名
	 * @param factory	handle工厂
	 * @param injectSuper	是否注入父类
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InjectException
	 */
	public  Class createClass(String oriClassName,MethodInjectHandlerFactory factory,boolean injectSuper) throws ClassNotFoundException, InjectException{
		return createClass(oriClassName,factory,null,injectSuper);
	}
	
	public  Class createClass(ClassLoader definedLoader,String oriClassName,MethodInjectHandlerFactory factory) throws ClassNotFoundException, InjectException{
		
		return createClass(oriClassName,factory,definedLoader,false);
	}
	
	public  Class createClass(String oriClassName,MethodInjectHandler handle) throws ClassNotFoundException, InjectException{
		return createClass(oriClassName,handle,false);
	}
	
	/**
	 * 完成类的字节码增强
	 * @param oriClassName 类名
	 * @param handle	方法handle
	 * @param injectSuper	是否注入父类
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InjectException
	 */
	public  Class createClass(String oriClassName,MethodInjectHandler handle,boolean injectSuper) throws ClassNotFoundException, InjectException{
		return createClass(null,oriClassName,handle,injectSuper);
	}
	
	public  Class createClass(ClassLoader definedLoader,String oriClassName,MethodInjectHandler handle,boolean injectSuper) throws ClassNotFoundException, InjectException{
		Class cls=null;
		
		/**
		 * 采用ASM进行字节码生成策略，最后定义到JVM中,好处是基于jdk14
		 * @todo 实现基于jdk15的instrument机制进行注入
		 */
		InjectorStrategy st=new ASMInjectorStrategy(definedLoader,injectSuper);
		
		cls=st.inject(oriClassName,handle);
		
		return cls;
		
	}
	
	public  Class createClass(ClassLoader definedLoader,String oriClassName,MethodInjectHandler handle) throws ClassNotFoundException, InjectException{
		return createClass(definedLoader,oriClassName,handle,false);
	}
	
}
