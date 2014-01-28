package org.openkoala.bpm.application.exception;

/**
 * 审核异常
 * @author lambo
 *
 */
public class WebServiceOfJBPMServiceException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public WebServiceOfJBPMServiceException(){
		super();
	}
	
	public WebServiceOfJBPMServiceException(String message){
		super(message);
	}
	
	public WebServiceOfJBPMServiceException(String message, Throwable cause){
		super(message, cause);
	}

}
