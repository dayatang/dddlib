package org.openkoala.bpm.application.exception;

/**
 * 审核异常
 * @author lambo
 *
 */
public class VerifyTaskException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public VerifyTaskException(){
		super();
	}
	
	public VerifyTaskException(String message){
		super(message);
	}
	
	public VerifyTaskException(String message, Throwable cause){
		super(message, cause);
	}

}
