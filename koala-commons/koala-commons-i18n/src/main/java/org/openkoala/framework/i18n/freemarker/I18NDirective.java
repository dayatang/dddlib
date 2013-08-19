package org.openkoala.framework.i18n.freemarker; 

import java.io.IOException;
import java.util.Map;
import org.openkoala.framework.i18n.I18NManager;
import freemarker.core.Environment;
import freemarker.core.TextBlock;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * I18N FreeMarker指令
 * @author Ken
 * @since 2013-01-14
 *
 */
public class I18NDirective implements TemplateDirectiveModel {
	
	private static final String LOCALE_ATTR = "locale";
	private static final String KEY_ATTR = "key";
	private String message;

	/**
	 * 执行用户自定义指令 
	 */
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		// key属性必须指定
		if (!params.containsKey(KEY_ATTR)) {
			throw new RuntimeException("The key attribute must be appointed");
		}
		if (params.containsKey(LOCALE_ATTR)) {
			message = I18NManager.getMessage(((SimpleScalar) params.get(KEY_ATTR)).getAsString(), // 
					((SimpleScalar) params.get(LOCALE_ATTR)).getAsString());
		} else {
			message = I18NManager.getMessage(((SimpleScalar) params.get(KEY_ATTR)).getAsString());
		}
		new TextBlock(message).accept(Environment.getCurrentEnvironment());
	}
	
}
