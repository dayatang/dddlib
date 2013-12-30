package org.openkoala.koala.auth.ss3adapter;

import org.openkoala.koala.auth.AuthHandler;
import org.openkoala.koala.auth.AuthInfo;
import org.openkoala.koala.auth.password.PasswordEncoder;

/**
 * Spring Security3.0认证处理器实现
 * @author zhuyuanbiao
 * @date 2013年12月23日 下午4:50:06
 *
 */
public class SS3AuthHandler implements AuthHandler {
	
	private PasswordEncoder passwordEncoder;

	@Override
	public boolean isAuth(AuthInfo loginInfo) {
		return isPasswordValid(loginInfo);
	}

	private boolean isPasswordValid(AuthInfo loginInfo) {
		return loginInfo.getEncodedPassword().equals(passwordEncoder.encode(loginInfo.getPassword()));
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}
