package org.openkoala.opencis.jira.service.impl;

public class ProjectKeyNotAllCharacterLettersException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectKeyNotAllCharacterLettersException(){
		super();
	}
	
	public ProjectKeyNotAllCharacterLettersException(String message){
		super(message);
	}
	
	public ProjectKeyNotAllCharacterLettersException(String message, Throwable cause){
		super(message, cause);
	}

}
