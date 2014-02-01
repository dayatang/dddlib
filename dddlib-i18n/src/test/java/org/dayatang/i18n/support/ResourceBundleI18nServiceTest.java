package org.dayatang.i18n.support;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.dayatang.i18n.LocaleContextHolder;
import org.dayatang.i18n.NoSuchMessageException;
import org.dayatang.i18n.support.I18nServiceAccessor;
import org.junit.Test;

public class ResourceBundleI18nServiceTest {

	I18nServiceAccessor messages = MyResourceBundleI18nService.getAccessor();

	@Test
	public void getMessage() {

		messages = MyResourceBundleI18nService.getAccessor();

		String msg = messages.getMessage("hi", "这是默认消息！", Locale.ENGLISH);
		assertEquals("hello", msg);

		msg = messages.getMessage("hi", "这是默认消息！");
		assertEquals("你好！", msg);

		msg = messages.getMessage("hi_", "这是默认消息！");
		assertEquals("这是默认消息！", msg);
	}

	@Test(expected = NoSuchMessageException.class)
	public void getMessage_noSuchMessage() {
		messages.getMessage("abc");
	}

	
	@Test
	public void getMessage_holder() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		String msg = messages.getMessage("hi", "这是默认消息！");
		assertEquals("hello", msg);
		
		LocaleContextHolder.resetLocale();
	}
}
