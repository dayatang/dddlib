package com.dayatang.i18n;

import java.util.Locale;

public abstract class LocaleContextHolder {

	private static final ThreadLocal<Locale> localeContextHolder = new ThreadLocal<Locale>();

	/**
	 * Reset the Locale for the current thread.
	 */
	public static void resetLocale() {
		localeContextHolder.set(null);
	}

	/**
	 * Associate the given Locale with the current thread, <i>not</i> exposing
	 * it as inheritable for child threads.
	 * 
	 * @param locale
	 *            the current Locale, or <code>null</code> to reset the
	 *            thread-bound context
	 */
	public static void setLocale(Locale locale) {
		localeContextHolder.set(locale);
	}

	/**
	 * Return the Locale associated with the current thread, if any.
	 * 
	 * @return the current Locale, or <code>null</code> if none
	 */
	public static Locale getLocale() {
		Locale locale = localeContextHolder.get();
		return (locale != null ? locale : Locale.getDefault());
	}

}
