package org.openkoala.koala.client.ejb;

import org.springframework.context.ApplicationContext;

/**
 * 获取EJB服务的客户端类
 * @author lingen
 *
 */
public class KoalaEJBClient {

	/**
	 * 传入需要的服务的接口，返回EJB远程服务
	 * @param c
	 * @return
	 * @throws Exception 
	 */
	public static Object getEJBService(Class c) throws Exception{
		ApplicationContext acx = EJBApplicationContextUtil.getApplicationContext();
		Object obj = acx.getBean(c);
		if(obj==null)throw new Exception("找不到此服务:"+c);
		return obj;
	}
	
	public static void main(String args[]) throws Exception{
//		AccountApplicationRemote accountApplicationRemote = (AccountApplicationRemote)KoalaEJBClient.getEJBService(AccountApplicationRemote.class);
//		System.out.println(accountApplicationRemote.queryMoney("lingen"));
	}
}
