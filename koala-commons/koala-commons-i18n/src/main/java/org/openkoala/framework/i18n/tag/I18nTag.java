package org.openkoala.framework.i18n.tag; 

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.openkoala.framework.i18n.I18NManager;

/**
 * i18n标签处理类
 * @author Ken
 * @since 2012-11-5
 *
 */
public class I18nTag extends SimpleTagSupport {
	
	/**
	 * 国际化资源文件中的key
	 */
	private String key;
	
	/**
	 * 指定当前所在的位置（如：cn、en等）
	 */
	private String locale;

	public void setKey(String key) {
		this.key = key;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Override
	public void doTag() throws JspException, IOException {
		String message = null;
		
		HttpServletRequest request = (HttpServletRequest)((PageContext) this.getJspContext()).getRequest();
		
		if (locale == null) {
			message = I18NManager.getMessage(key, request.getLocale().getLanguage());
		} else {
			message = I18NManager.getMessage(key, locale);
		}
		
		// 将取出的国际化信息写入到页面中 
		this.getJspContext().getOut().write(message);
	}
	

}
