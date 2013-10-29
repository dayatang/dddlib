package org.openkoala.bpm.application.exception;

/**
 * 还未自定义动态表单异常
 * @author lambo
 *
 */
public class FormHavenDefinedException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public FormHavenDefinedException(){
		super();
	}
	
	public FormHavenDefinedException(String message){
		super(message);
	}
	
	public FormHavenDefinedException(String message, Throwable cause){
		super(message, cause);
	}

}
