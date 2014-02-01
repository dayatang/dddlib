package com.dayatang.i18n.support;

import java.io.Serializable;
import java.util.Locale;

import com.dayatang.i18n.I18nService;
import com.dayatang.i18n.LocaleContextHolder;
import com.dayatang.i18n.NoSuchMessageException;

public class I18nServiceAccessor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2426808354379252982L;

	private final I18nService i18nService;

	private final Locale defaultLocale;

	/**
	 * Create a new I18nServiceAccessor, using LocaleContextHolder's locale as
	 * default locale.
	 * 
	 * @param i18nService
	 *            the i18nService to wrap
	 * @see com.dayatang.i18n.LocaleContextHolder#getLocale()
	 */
	public I18nServiceAccessor(I18nService i18nService) {
		this.i18nService = i18nService;
		this.defaultLocale = null;
	}

	/**
	 * Create a new I18nServiceAccessor, using the given default locale.
	 * 
	 * @param i18nService
	 *            the i18nService to wrap
	 * @param defaultLocale
	 *            the default locale to use for message access
	 */
	public I18nServiceAccessor(I18nService i18nService, Locale defaultLocale) {
		this.i18nService = i18nService;
		this.defaultLocale = defaultLocale;
	}

	/**
	 * Return the default locale to use if no explicit locale has been given.
	 * <p>
	 * The default implementation returns the default locale passed into the
	 * corresponding constructor, or LocaleContextHolder's locale as fallback.
	 * Can be overridden in subclasses.
	 * 
	 * @see #I18nServiceAccessor(com.dayatang.i18n.i18nService,
	 *      java.util.Locale)
	 * @see com.dayatang.i18n.LocaleContextHolder#getLocale()
	 */
	protected Locale getDefaultLocale() {
		return (this.defaultLocale != null ? this.defaultLocale
				: LocaleContextHolder.getLocale());
	}

	/**
	 * Retrieve the message for the given code and the default Locale.
	 * 
	 * @param code
	 *            code of the message
	 * @param defaultMessage
	 *            String to return if the lookup fails
	 * @return the message
	 */
	public String getMessage(String code, String defaultMessage) {
		return this.i18nService.getMessage(code, null, defaultMessage,
				getDefaultLocale());
	}

	/**
	 * Retrieve the message for the given code and the given Locale.
	 * 
	 * @param code
	 *            code of the message
	 * @param defaultMessage
	 *            String to return if the lookup fails
	 * @param locale
	 *            Locale in which to do lookup
	 * @return the message
	 */
	public String getMessage(String code, String defaultMessage, Locale locale) {
		return this.i18nService.getMessage(code, null, defaultMessage, locale);
	}

	/**
	 * Retrieve the message for the given code and the default Locale.
	 * 
	 * @param code
	 *            code of the message
	 * @param args
	 *            arguments for the message, or <code>null</code> if none
	 * @param defaultMessage
	 *            String to return if the lookup fails
	 * @return the message
	 */
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return this.i18nService.getMessage(code, args, defaultMessage,
				getDefaultLocale());
	}

	/**
	 * Retrieve the message for the given code and the given Locale.
	 * 
	 * @param code
	 *            code of the message
	 * @param args
	 *            arguments for the message, or <code>null</code> if none
	 * @param defaultMessage
	 *            String to return if the lookup fails
	 * @param locale
	 *            Locale in which to do lookup
	 * @return the message
	 */
	public String getMessage(String code, Object[] args, String defaultMessage,
			Locale locale) {
		return this.i18nService.getMessage(code, args, defaultMessage, locale);
	}

	/**
	 * Retrieve the message for the given code and the default Locale.
	 * 
	 * @param code
	 *            code of the message
	 * @return the message
	 * @throws com.dayatang.i18n.NoSuchMessageException
	 *             if not found
	 */
	public String getMessage(String code) throws NoSuchMessageException {
		return this.i18nService.getMessage(code, null, getDefaultLocale());
	}

	/**
	 * Retrieve the message for the given code and the given Locale.
	 * 
	 * @param code
	 *            code of the message
	 * @param locale
	 *            Locale in which to do lookup
	 * @return the message
	 * @throws com.dayatang.i18n.NoSuchMessageException
	 *             if not found
	 */
	public String getMessage(String code, Locale locale)
			throws NoSuchMessageException {
		return this.i18nService.getMessage(code, null, locale);
	}

	/**
	 * Retrieve the message for the given code and the default Locale.
	 * 
	 * @param code
	 *            code of the message
	 * @param args
	 *            arguments for the message, or <code>null</code> if none
	 * @return the message
	 * @throws com.dayatang.i18n.NoSuchMessageException
	 *             if not found
	 */
	public String getMessage(String code, Object[] args)
			throws NoSuchMessageException {
		return this.i18nService.getMessage(code, args, getDefaultLocale());
	}

	/**
	 * Retrieve the message for the given code and the given Locale.
	 * 
	 * @param code
	 *            code of the message
	 * @param args
	 *            arguments for the message, or <code>null</code> if none
	 * @param locale
	 *            Locale in which to do lookup
	 * @return the message
	 * @throws com.dayatang.i18n.NoSuchMessageException
	 *             if not found
	 */
	public String getMessage(String code, Object[] args, Locale locale)
			throws NoSuchMessageException {
		return this.i18nService.getMessage(code, args, locale);
	}

}
