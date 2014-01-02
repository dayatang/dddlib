package org.openkoala.koala.auth;

import org.openkoala.koala.auth.vo.DefaultUserDetailsImpl;

public class SimpleAuthHandler implements AuthHandler {

	@Override
	public UserDetails authenticate(String useraccount, String password) {
		if (useraccount.equals(password)) {
			DefaultUserDetailsImpl result = new DefaultUserDetailsImpl();
			result.setUseraccount(useraccount);
			result.setRealName(password);
			result.setSuper(false);
			return result;
		}
		return null;
	}

}
