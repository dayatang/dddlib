package org.jwebap.cfg.exception;

/**
 * 加载插件配置错误，当插件引用真正加载插件配置时发生错误抛出
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.6
 * @date  2008-10-19
 */
public class PluginDefLinkedException extends RuntimeException {
	
	static final long serialVersionUID = -1L;
	
	public PluginDefLinkedException(String message) {
		super(message);
	}

	public PluginDefLinkedException(String message, Throwable cause) {
		super(message, cause);
	}
}
