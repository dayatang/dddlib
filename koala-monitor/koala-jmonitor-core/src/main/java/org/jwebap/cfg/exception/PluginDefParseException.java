package org.jwebap.cfg.exception;

/**
 * 插件定义加载错误，当解析插件配置时发生错误抛出
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.6
 * @date  2008-10-19
 */
public class PluginDefParseException extends Exception {
	
	static final long serialVersionUID = -1L;
	
	public PluginDefParseException(String message) {
		super(message);
	}

	public PluginDefParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
