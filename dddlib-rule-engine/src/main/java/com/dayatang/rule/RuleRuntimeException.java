package com.dayatang.rule;

/**
 * @author chencao
 * 
 */
public class RuleRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9048840251163984911L;

	public RuleRuntimeException() {
		super();
	}

	public RuleRuntimeException(String message) {
		super(message);
	}

	public RuleRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuleRuntimeException(Throwable cause) {
		super(cause);
	}
}
