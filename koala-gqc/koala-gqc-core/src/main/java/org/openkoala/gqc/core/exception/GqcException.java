package org.openkoala.gqc.core.exception;

public abstract class GqcException extends RuntimeException {
	private static final long serialVersionUID = 4331400917444670599L;

	public GqcException() {
		super();
	}

	public GqcException(String message) {
		super(message);
	}

	public GqcException(Throwable cause) {
		super(cause);
	}

	public GqcException(String message, Throwable cause) {
		super(message, cause);
	}
}
