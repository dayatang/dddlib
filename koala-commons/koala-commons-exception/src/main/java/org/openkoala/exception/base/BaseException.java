package org.openkoala.exception.base;

import org.openkoala.exception.base.utils.WebErrUtils;

/**
 * 类    名：org.openkoala.exception.base.BaseException<br />
 *   
 * 功能描述：具体功能做描述。	<br />
 *  
 * 创建日期：2013-1-18  <br />   
 * 
 * 版本信息：v 1.0<br />
 * 
 * 版权信息：Copyright (c) 2011 openkoala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:jiangwei@openkoala.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 3438322119670695654L;
	
	//错误编码 对应i18n的key
	protected String errorCode = "errorCode.undefined";
	//默认错误信息
	protected String defaultMessage;

	
	public BaseException(Throwable cause) {
		super(cause);
		if(cause instanceof BaseException){
			BaseException e = (BaseException)cause;
			this.defaultMessage = e.defaultMessage;
			this.errorCode = e.errorCode;
		}
	}

	/**
	 * @param cause  原始的异常信息
	 * @param errorCode 错误编码对应i18n的key
	 */
	public BaseException(Throwable cause,String errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

	/**
	 * @param cause  原始的异常信息
	 * @param errorCode   错误编码对应i18n的key
	 * @param defaultMessage  默认显示信息，即i18n配置文件无对应值时显示
	 */
	public BaseException(Throwable cause,String errorCode, String defaultMessage) {
		this(cause,errorCode);
		this.defaultMessage = defaultMessage;
	}

	/**
	 * 
	 * @param errorCode   错误编码对应i18n的key
	 * @param defaultMessage  默认显示信息，即i18n配置文件无对应值时显示
	 */
	public BaseException(String errorCode, String defaultMessage) {
		this.errorCode = errorCode;
		this.defaultMessage = defaultMessage;
	}


	@Override
	public String getMessage() {
        String message = null;
        message = WebErrUtils.getI18nMessage(errorCode,defaultMessage);
        return message;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}
	
	
}
