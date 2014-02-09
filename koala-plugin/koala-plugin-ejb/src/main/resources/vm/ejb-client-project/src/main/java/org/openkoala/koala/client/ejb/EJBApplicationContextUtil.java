package org.openkoala.koala.client.ejb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 使用spring提供的方法来扫描
 * @author lingen
 *
 */
public class EJBApplicationContextUtil {

	/**
	 * spring上下文
	 */
	private static ApplicationContext acx;
	
	static{
		acx=new ClassPathXmlApplicationContext("classpath:ejb-client.xml"); 
	}
	
	public static ApplicationContext getApplicationContext(){
		return acx;
	}
}
