package org.jwebap.cfg.exception;

/**
 * 对象解析错误，当解析对象时发生错误时抛出
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.6
 * @date  2008-10-19
 */
public class BeanParseException extends Exception {

	static final long serialVersionUID = -19811222L;
	
	public BeanParseException(String message) {
		super(message);
	}

	public BeanParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
