package org.jwebap.util;

/**
 * Bean操作帮助类
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date 2007-12-24
 */
public class BeanUtil {

	/**
	 * 根据当前ClassLoader环境初始化类实例,而不是采用Class.forName
	 * @param clazzName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Object newInstance(String clazzName) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Class clz = loader.loadClass(clazzName);
		return clz.newInstance();
	}
	
	
}
