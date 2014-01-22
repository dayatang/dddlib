package org.openkoala.opencis.exception;
/**
 * 重复授权异常
 * @author zjh
 *
 */@SuppressWarnings("serial")

public class DuplicateAuthzException extends Exception {

	public DuplicateAuthzException() {
		// TODO Auto-generated constructor stub
	}

	public DuplicateAuthzException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DuplicateAuthzException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public DuplicateAuthzException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
