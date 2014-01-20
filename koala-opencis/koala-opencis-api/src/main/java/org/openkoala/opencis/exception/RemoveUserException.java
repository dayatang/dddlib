package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

/**
 * 移除用户时的异常
 * @author zjh
 *
 */
public class RemoveUserException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = -4440703203419133877L;

	public RemoveUserException() {
		// TODO Auto-generated constructor stub
		super();
	}

	public RemoveUserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RemoveUserException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
