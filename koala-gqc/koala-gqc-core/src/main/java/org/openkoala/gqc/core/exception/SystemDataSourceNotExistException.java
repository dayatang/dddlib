package org.openkoala.gqc.core.exception;

public class SystemDataSourceNotExistException extends GqcException {
	private static final long serialVersionUID = -799753224561578269L;
	
	public SystemDataSourceNotExistException() {
		super();
	}

	public SystemDataSourceNotExistException(String message) {
		super(message);
	}

	public SystemDataSourceNotExistException(Throwable cause) {
		super(cause);
	}

	public SystemDataSourceNotExistException(String message, Throwable cause) {
		super(message, cause);
	}
}
