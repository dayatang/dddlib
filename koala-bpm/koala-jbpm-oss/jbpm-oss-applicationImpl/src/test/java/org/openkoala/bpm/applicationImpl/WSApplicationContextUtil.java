package org.openkoala.bpm.applicationImpl;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 使用spring提供的方法来扫描
 * @author lingen
 *
 */
public class WSApplicationContextUtil {

	/**
	 * spring上下文
	 */
	private static ApplicationContext acx;
	
	static{
		acx=new ClassPathXmlApplicationContext("classpath:META-INF/spring/ws-client.xml"); 
	}
	
	public static ApplicationContext getApplicationContext(){
		return acx;
	}
}
