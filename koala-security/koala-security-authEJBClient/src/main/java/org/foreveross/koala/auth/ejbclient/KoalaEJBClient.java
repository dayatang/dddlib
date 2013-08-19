package org.openkoala.koala.auth.ejbclient;

import java.util.List;

import org.openkoala.koala.auth.UserDetails;
import org.openkoala.koala.auth.UserDetailsManager;
import org.openkoala.koala.auth.ejb.AuthEJBApplication;
import org.openkoala.koala.auth.impl.jdbc.JdbcCustomUserDetails;
import org.springframework.context.ApplicationContext;


public class KoalaEJBClient {


	public static Object getEJBService(Class c) throws Exception{
		ApplicationContext acx = EJBApplicationContextUtil.getApplicationContext();
		Object obj = acx.getBean(c);
		if(obj==null)throw new Exception("閹靛彞绗夐崚鐗堫劃閺堝秴濮�"+c);
		return obj;
	}
	
	public static void main(String args[]) throws Exception{
		//TestApplicationRemote accountApplicationRemote = (TestApplicationRemote)KoalaEJBClient.getEJBService(TestApplicationRemote.class);
		//accountApplicationRemote.testMethod();
		UserDetailsManager authRemote = (UserDetailsManager)KoalaEJBClient.getEJBService(UserDetailsManager.class);
		JdbcCustomUserDetails ls = (JdbcCustomUserDetails)authRemote.loadUserByUseraccount("caoyong");
		 System.out.println(ls.getUseraccount());
		 
		 
		AuthEJBApplication aut = (AuthEJBApplication) KoalaEJBClient.getEJBService(AuthEJBApplication.class);
		List<String> sf = aut.getRolesByUser("caoyong");
		for(String a : sf)
		{
			System.out.println(a);
		}
		
		
	}
}
