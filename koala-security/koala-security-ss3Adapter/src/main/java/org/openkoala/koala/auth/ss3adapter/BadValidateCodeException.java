package org.openkoala.koala.auth.ss3adapter;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码不正确异常
 * @author zhuyuanbiao
 * @date 2014年1月13日 上午10:51:29
 *
 */
public class BadValidateCodeException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4618077312779644887L;

	public BadValidateCodeException(String msg) {
		super(msg);
	}

}
