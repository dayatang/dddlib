package com.dayatang.i18n;

import java.util.Locale;

public class NoSuchMessageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -946662425731182622L;

	/**
	 * Create a new exception.
	 * 
	 * @param code
	 *            code that could not be resolved for given locale
	 * @param locale
	 *            locale that was used to search for the code within
	 */
	public NoSuchMessageException(String code, Locale locale) {
		super("No message found under code '" + code + "' for locale '"
				+ locale + "'.");
	}

	/**
	 * Create a new exception.
	 * 
	 * @param code
	 *            code that could not be resolved for given locale
	 */
	public NoSuchMessageException(String code) {
		super("No message found under code '" + code + "' for locale '"
				+ Locale.getDefault() + "'.");
	}

}
