package org.jwebap.startup;

/**
 * 插件初始化错误，所有的插件加载错误都是其子类，包括（PluginDefNotFoundException,ConponentInitialException等等）
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.6
 * @date  2008-10-4
 * @see PluginDefNotFoundException,ConponentInitialException
 */
public class PluginInitialException extends RuntimeException {
	
	static final long serialVersionUID = -1L;
	
	public PluginInitialException(String message) {
		super(message);
	}

	public PluginInitialException(String message, Throwable cause) {
		super(message, cause);
	}
}
