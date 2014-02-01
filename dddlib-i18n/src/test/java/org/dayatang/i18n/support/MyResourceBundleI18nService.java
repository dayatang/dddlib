package org.dayatang.i18n.support;

import org.dayatang.i18n.impl.ResourceBundleI18nService;
import org.dayatang.i18n.support.I18nServiceAccessor;

public class MyResourceBundleI18nService extends ResourceBundleI18nService {

	public MyResourceBundleI18nService() {
		setBasename("org.dayatang.i18n.messages");
	}

	// ~ Methods
	// ========================================================================================================

	public static I18nServiceAccessor getAccessor() {
		return new I18nServiceAccessor(new MyResourceBundleI18nService());
	}
}
