package org.openkoala.opencis;

public class PermissionElementNotExistException extends RuntimeException {

	private static final long serialVersionUID = -6623654064882331613L;

	public PermissionElementNotExistException() {
		super();
	}

	public PermissionElementNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionElementNotExistException(String message) {
		super(message);
	}

	public PermissionElementNotExistException(Throwable cause) {
		super(cause);
	}

}
