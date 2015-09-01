package org.dddlib.organisation.domain;

public class HrException extends RuntimeException {

	private static final long serialVersionUID = 62105045849226323L;

	public HrException() {
	}

	public HrException(String message) {
		super(message);
	}

	public HrException(Throwable cause) {
		super(cause);
	}

	public HrException(String message, Throwable cause) {
		super(message, cause);
	}

}
