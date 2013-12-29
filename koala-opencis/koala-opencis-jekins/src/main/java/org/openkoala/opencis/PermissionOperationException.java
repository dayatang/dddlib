package org.openkoala.opencis;

public class PermissionOperationException extends RuntimeException {

	private static final long serialVersionUID = -6361189421736526712L;

	public PermissionOperationException() {
		super();
	}

	public PermissionOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionOperationException(String message) {
		super(message);
	}

	public PermissionOperationException(Throwable cause) {
		super(cause);
	}

}
