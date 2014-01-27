package org.openkoala.koala.auth.impl;

/**
 * 用户账号不存在
 * @author zhuyuanbiao
 * @date Dec 27, 2013 9:53:14 AM
 *
 */
public class UseraccountNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8014674717226146087L;

	public UseraccountNotExistException() {
		super();
	}

	public UseraccountNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public UseraccountNotExistException(String message) {
		super(message);
	}

	public UseraccountNotExistException(Throwable cause) {
		super(cause);
	}

}
