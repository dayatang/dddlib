package org.openkoala.koala.auth.ss3adapter;

import org.openkoala.koala.auth.password.PasswordEncoder;

/**
 * 直接返回原始密码
 * @author zhuyuanbiao
 * @date Dec 27, 2013 10:34:42 AM
 *
 */
public class PlainPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(String password) {
		return password;
	}

}
