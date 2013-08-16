package org.jwebap.core;


/**
 * Component组件注册异常
 * 
 * @author leadyu
 * @since Jwebap 0.5
 * @date  Aug 7, 2007
 */
public class RegisterException extends Exception{
	
	public RegisterException(String msg){
		super(msg);
	}
	
	public RegisterException(String msg,Throwable cause){
		super(msg,cause);
	}
}
