package com.dayatang.rule;

/**
 * @author chencao
 * 
 */
public class UnSupportedRuleFormatException extends RuleRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -777453146281299453L;

	public UnSupportedRuleFormatException() {
		super();
	}

	public UnSupportedRuleFormatException(String message) {
		super(message);
	}

	public UnSupportedRuleFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnSupportedRuleFormatException(Throwable cause) {
		super(cause);
	}
}
