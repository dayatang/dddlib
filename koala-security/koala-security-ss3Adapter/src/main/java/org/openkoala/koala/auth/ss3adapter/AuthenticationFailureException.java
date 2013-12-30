package org.openkoala.koala.auth.ss3adapter;

import org.springframework.security.core.AuthenticationException;

/**
 * 认证失败异常
 * @author zhuyuanbiao
 * @date 2013年12月23日 下午5:00:59
 *
 */
public class AuthenticationFailureException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7132103903673286973L;

	public AuthenticationFailureException(String msg) {
		super(msg);
	}

}
