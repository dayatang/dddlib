package org.openkoala.koala.client.ws;

import org.springframework.context.ApplicationContext;

/**
 * 提供给客户端调用
 * 
 * @author lingen
 * 
 */
public class KoalaWsClient {

	/**
	 * 传入所需要的服务的接口，返回WS远程类给客户端调用
	 * 
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Object getWsService(Class c) throws Exception {
		ApplicationContext acx = WSApplicationContextUtil.getApplicationContext();
		Object obj = acx.getBean(c);
		if (obj == null)
			throw new Exception("找不到所需要的WS服务:" + c);
		return obj;
	}
	
	/**
	 * 测试
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{
//		AccountApplication application = (AccountApplication) KoalaWsClient.getWsService(AccountApplication.class);
//		System.out.println(application.queryMoney("lingen"));
	}
}
