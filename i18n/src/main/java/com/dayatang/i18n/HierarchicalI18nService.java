package com.dayatang.i18n;

public interface HierarchicalI18nService extends I18nService {
	/**
	 * Set the parent that will be used to try to resolve messages that this
	 * object can't resolve.
	 * 
	 * @param parent
	 *            the parent I18nService that will be used to resolve messages
	 *            that this object can't resolve. May be <code>null</code>, in
	 *            which case no further resolution is possible.
	 */
	void setParentMessageSource(I18nService parent);

	/**
	 * Return the parent of this I18nService, or <code>null</code> if none.
	 */
	I18nService getParentMessageSource();
}
