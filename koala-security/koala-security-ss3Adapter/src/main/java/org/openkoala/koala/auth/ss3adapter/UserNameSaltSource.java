package org.openkoala.koala.auth.ss3adapter;

import org.openkoala.koala.auth.SaltSource;

/**
 * 用户名盐值 
 * @author zhuyuanbiao
 * @date Dec 27, 2013 11:00:52 AM
 *
 */
public class UserNameSaltSource implements SaltSource {
	
	@Override
	public Object getSalt() {
		return null;
	}

}
