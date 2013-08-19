package org.jwebap.toolkit.bytecode;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jwebap.toolkit.bytecode.asm.ASMInjectorStrategy;
import org.jwebap.toolkit.bytecode.asm.MethodInjectHandler;
import org.jwebap.toolkit.bytecode.asm.MethodInjectHandlerFactory;
import org.jwebap.toolkit.bytecode.asm.StaticHandleFactory;

/**
 * 包注入增强器 对包中所有类进行注入，以后需要改进此类，结构和算法还没很好的设计
 * 
 * @author leadyu
 * @since Jwebap 0.5
 * @date Aug 26, 2007
 */
public class PackageEnhancer {

	private static final Log log = LogFactory.getLog(PackageEnhancer.class);

	protected ClassEnhancer classEnhancer = null;

	public PackageEnhancer() {
		classEnhancer = new ClassEnhancer();
	}

	/**
	 * 对包中所有的类进行字节码增强，但是不指定handle，而由运行时指定(调用StaticHandleFactory进行handle注册)
	 * 
	 * @param pckName
	 * @return
	 * @throws IOException
	 * @see StaticHandleFactory
	 */
	public Class[] createPackage(String pckName) throws InjectException {
		return createPackage(null, pckName);

	}

	public Class[] createPackage(ClassLoader definedLoader, String pckName)
			throws InjectException {
		return createPackage(definedLoader, pckName, null);
	}

	/**
	 * 对包中所有的类进行字节码增强
	 * 
	 * @param pckName
	 *            包名
	 * @param factory
	 *            handle工厂
	 * @return
	 * @throws IOException
	 */
	public Class[] createPackage(String pckName,
			MethodInjectHandlerFactory factory) throws InjectException {
		return createPackage(null, pckName, factory);
	}

	public Class[] createPackage(ClassLoader definedLoader, String pckName,
			MethodInjectHandlerFactory factory) throws InjectException {
		Class[] cls = null;

		/**
		 * 采用ASM进行字节码生成策略，最后定义到JVM中,好处是基于jdk14
		 */
		InjectorStrategy st = new ASMInjectorStrategy(definedLoader, false);

		cls = st.injectPackage(pckName, factory);

		return cls;
	}

	public Class[] createPackage(String pckName, MethodInjectHandler handle)
			throws InjectException {
		return createPackage(pckName, handle, null);
	}

	public Class[] createPackage(String pckName, MethodInjectHandler handle,
			ClassLoader definedLoader) throws InjectException {
		Class[] cls = null;

		/**
		 * 采用ASM进行字节码生成策略，最后定义到JVM中,好处是基于jdk14
		 */
		InjectorStrategy st = new ASMInjectorStrategy(definedLoader, false);

		cls = st.injectPackage(pckName, handle);

		return cls;
	}

}
