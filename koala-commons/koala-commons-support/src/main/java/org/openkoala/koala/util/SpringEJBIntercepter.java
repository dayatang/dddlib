/*******************************************************************************
 * Copyright (c) 2011-9-8 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.openkoala.koala.util;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Named;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.InstanceProvider;
import com.dayatang.spring.factory.SpringProvider;

/**
 * EJB拦截器，用于返回SpringBean的调用，
 * 用法，在EJB上添加@javax.interceptor.Interceptors(com.csair.gme.application.impl.SpringEJBIntercepter.class)
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2011-9-8
 */
public class SpringEJBIntercepter {

	private static final Log logger = LogFactory
			.getLog(SpringEJBIntercepter.class);
	
	private static final String CONFIG_FILE_0 = "META-INF/springEjbIntercepter.properties";
	private static final String CONFIG_FILE_1 = "springEjbIntercepter.properties";
	private static final String CONFIG_NAME = "auth.springEjbIntercepter";
	private static final String CONFIG_VALUE_DEFAULT = "classpath*:META-INF/spring/root.xml";
	private static final Map<CacheClass, CacheClass> cacheMap = new HashMap<CacheClass, CacheClass>();
	private static InstanceProvider instanceProvider = null;

	static {
		initInstanceFactory();
	}

		@AroundInvoke
	public Object doit(InvocationContext context) throws Exception {
		Class<?> targetClass = context.getTarget().getClass();
		Method targetMethod = context.getMethod();
		CacheClass key = new CacheClass(targetClass, targetMethod);
		if (cacheMap.containsKey(key)) {
			CacheClass cacheClass = cacheMap.get(key);
			Object target = instanceProvider.getInstance(cacheClass.clazz);
			return ((Method) cacheClass.method).invoke(target, context
					.getParameters());
		}

		String serviceName = "";
		for (Annotation a : targetClass.getAnnotations()) {
			if (a instanceof Service) {
				serviceName = ((Service) a).value();
			}
			if(a instanceof Named){
				serviceName = ((Named) a).value();
			}
		}

		Exception ex = null;
		Class interfaceClass = targetClass.getInterfaces()[0];
		try {
			if (instanceProvider.getInstance(interfaceClass) != null) {
				Object target = serviceName != null
						&& serviceName.length() > 0 ? instanceProvider
						.getInstance(interfaceClass, serviceName)
						: InstanceFactory.getInstance(interfaceClass);
				Method method = getMethod(interfaceClass, context.getMethod());
				Object o = method.invoke(target, context
						.getParameters());
				{//cache
					cacheMap.put(key, new CacheClass(interfaceClass, method));
				}
				return o;
			}
			else{
				throw new Exception("Can not find the impl for:"+interfaceClass);
			}
		} catch (Exception e) {
			ex = e;
		}
		
//		for (Annotation a : targetClass.getAnnotations()) {
//			if (a instanceof Remote) {
//				Class<?>[] value = ((Remote) a).value();
//				for (Class<?> c : value) {
//					try {
//						if (InstanceFactory.getInstance(c) != null) {
//							Object target = serviceName != null
//									&& serviceName.length() > 0 ? InstanceFactory
//									.getInstance(c, serviceName)
//									: InstanceFactory.getInstance(c);
//							Method method = getMethod(c, context.getMethod());
//							Object o = method.invoke(target, context
//									.getParameters());
//							{//cache
//								cacheMap.put(key, new CacheClass(c, method));
//							}
//							return o;
//						}
//					} catch (Exception e) {
//						ex = e;
//					}
//				}
//			}
//		}
		if (ex != null) {
			throw ex;
		}
		return null;
	}

	private Method getMethod(Class<?> clazz, Method method) throws Exception {
		return clazz.getMethod(method.getName(), method.getParameterTypes());
	}

	private static Properties loadConfigure() {
		Properties props = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = (new ClassPathResource(CONFIG_FILE_0))
					.getInputStream();
			props.load(inputStream);
		} catch (Exception e) {
			logger.info("Can't load \"" + CONFIG_FILE_0 + "\": ", e);
			try {
				inputStream = (new ClassPathResource(CONFIG_FILE_1))
						.getInputStream();
				props.load(inputStream);
			} catch (Exception ee) {
				logger.info("Can't load \"" + CONFIG_FILE_1 + "\": ", ee);
			}
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
			}
		}
		return props;
	}

//	private static void loadResources() {
//		try {
//			System.out.println("===========loadResources===========");
//			ClassLoader cl = null;
//			try {
//				cl = Thread.currentThread().getContextClassLoader();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			if (cl == null) {
//				cl = SpringEJBIntercepter.class.getClassLoader();
//			}
//			Enumeration<URL> resources = cl.getResources("xtree.vm");
//			while (resources.hasMoreElements()) {
//				URL url = resources.nextElement();
//				System.out.println(url);
//				System.out.println(url.getContent());
//			}
//			System.out.println("===========loadResources===========");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private static void initInstanceFactory() {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@");
		if (instanceProvider != null) {
			return;
		}
		
		if (instanceProvider==null) {
			Properties props = loadConfigure();
			String property = "";
			if(props!=null)
			property = props.getProperty(CONFIG_NAME, "");
			if (property.length() < 1) {
				logger.info("Can't find property \"" + CONFIG_NAME
						+ "\"! Use default \"" + CONFIG_VALUE_DEFAULT + "\".");
				property = CONFIG_VALUE_DEFAULT;
			}
			try {
				instanceProvider = new SpringProvider(new String[] { property });
				InstanceFactory.setInstanceProvider(instanceProvider);
			} catch (Exception e) {
				logger.error(
						"Can't initialize com.dayatang.domain.InstanceFactory",
						e);
			}
		}
		System.out.println("@@@@@@@@@@@@@@@@@@@@@");
	}

	private static class CacheClass {
		private Class<?> clazz;
		private Method method;

		public CacheClass(Class<?> clazz, Method method) {
			this.clazz = clazz;
			this.method = method;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
			result = prime * result
					+ ((method == null) ? 0 : method.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheClass other = (CacheClass) obj;
			if (clazz == null) {
				if (other.clazz != null)
					return false;
			} else if (!clazz.equals(other.clazz))
				return false;
			if (method == null) {
				if (other.method != null)
					return false;
			} else if (!method.equals(other.method))
				return false;
			return true;
		}
	}
}