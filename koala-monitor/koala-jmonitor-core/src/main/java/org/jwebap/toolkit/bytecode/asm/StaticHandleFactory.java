package org.jwebap.toolkit.bytecode.asm;

import java.util.HashMap;
import java.util.Map;

/**
 * 字节码注入handle管理工厂
 * 
 * 被增强的类会在第一次被访问时，从工厂中获得方法的handle，并保存在静态变量里
 * 
 * @todo 现在对注入的字节码，为每一个类方法都保留了一个handle静态属性，当handle都一样时，造成一定的内存浪费
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date Mar 7, 2008
 */
public class StaticHandleFactory {

	private static Map factorys = new HashMap();

	/**
	 * 获取方法handle
	 * 
	 * @param className
	 * @param methodName
	 * @param signature
	 * @return
	 */
	public static synchronized MethodInjectHandler getMethodHandler(
			String className, String methodName, String signature) {

		Object handleObject = factorys.get(className);
		
		/**
		 * 用类名查找handle
		 */
		if (handleObject != null
				&& handleObject instanceof MethodInjectHandlerFactory) {
			MethodInjectHandlerFactory factory = (MethodInjectHandlerFactory) handleObject;
			return factory.getMethodHandler(className, methodName, signature);
		} else if (handleObject != null
				&& handleObject instanceof MethodInjectHandler) {
			return (MethodInjectHandler) handleObject;
		}

		/**
		 * 用包名查找handle
		 */
		int last=className.lastIndexOf('.');
		
		String pkgName=last<0?"":className.substring(0,last)+".*";
		Object pkgHandle = factorys.get(pkgName);
		if (pkgHandle != null
				&& pkgHandle instanceof MethodInjectHandlerFactory) {
			MethodInjectHandlerFactory factory = (MethodInjectHandlerFactory) pkgHandle;
			return factory.getMethodHandler(className, methodName, signature);
		} else if (pkgHandle != null
				&& pkgHandle instanceof MethodInjectHandler) {
			return (MethodInjectHandler) pkgHandle;
		}
		
		return null;

	}

	/**
	 * 注册类注入工厂
	 * 
	 * @param className
	 * @param factory
	 * @see MethodInjectHandlerFactory
	 *      invokeHandle的工厂,注入的代码可以做到根据不同的条件(比如:类名,方法名等)执行不同的注入代码
	 */
	public static void registerFactory(String className,
			MethodInjectHandlerFactory factory) {
		factorys.put(className, factory);
	}

	/**
	 * 注册类注入工厂
	 * 
	 * @param className
	 * @param handle
	 * @see MethodInjectHandler invokeHandle,注入代码需要实现的接口
	 */
	public static void registerHandle(String className,
			MethodInjectHandler handle) {
		factorys.put(className, handle);
	}
	
	public static void registerPkgFactory(String pkgName,
			MethodInjectHandlerFactory factory) {
		factorys.put(pkgName, factory);
	}
	
	public static void registerPkgHandle(String pkgName,
			MethodInjectHandler handle) {
		factorys.put(pkgName, handle);
	}

}
