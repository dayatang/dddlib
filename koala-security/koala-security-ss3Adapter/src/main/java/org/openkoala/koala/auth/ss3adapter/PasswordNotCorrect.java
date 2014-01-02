package org.openkoala.koala.auth.ss3adapter;

/**
 * 密码不正确异常
 * @author zhuyuanbiao
 * @date 2014年1月2日 上午11:54:41
 *
 */
public class PasswordNotCorrect extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6043492689396984868L;

	public PasswordNotCorrect() {
		super();
	}

	public PasswordNotCorrect(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordNotCorrect(String message) {
		super(message);
	}

	public PasswordNotCorrect(Throwable cause) {
		super(cause);
	}

}
