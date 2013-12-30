package org.openkoala.koala.auth.ss3adapter;

import org.openkoala.koala.auth.SaltSource;
import org.openkoala.koala.auth.password.PasswordEncoder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * Spring Security3.0密码加密实现
 * @author zhuyuanbiao
 * @date Dec 24, 2013 9:39:54 AM
 *
 */
public class SS3PasswordEncoder implements PasswordEncoder {
	
	private Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
	
	private SaltSource saltSource;

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	@Override
	public String encode(String password) {
		if (saltSource != null) {
			return md5PasswordEncoder.encodePassword(password, saltSource.getSalt());
		}
		return md5PasswordEncoder.encodePassword(password, null);
	}

}
