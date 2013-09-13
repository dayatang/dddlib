package org.openkoala.opencis.jira.service.impl;

public class ProjectKeyLengthNotBetweenTwoAndTenCharacterLettersException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectKeyLengthNotBetweenTwoAndTenCharacterLettersException(){
		super();
	}
	
	public ProjectKeyLengthNotBetweenTwoAndTenCharacterLettersException(String message){
		super(message);
	}
	
	public ProjectKeyLengthNotBetweenTwoAndTenCharacterLettersException(String message, Throwable cause){
		super(message, cause);
	}

}
