package org.openkoala.koala.monitor.exception;

/**
 * 所有JwebapInitialException的子类指明Jwebap启动过程发生的异常
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.6
 * @date  2008-10-4
 */
public class ContextInitialException  extends RuntimeException {

	static final long serialVersionUID = -19811222L;
	
	public ContextInitialException(String message) {
		super(message);
	}

	public ContextInitialException(String message, Throwable cause) {
		super(message, cause);
	}

}
