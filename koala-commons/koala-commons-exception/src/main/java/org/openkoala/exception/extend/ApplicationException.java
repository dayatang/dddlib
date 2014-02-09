package org.openkoala.exception.extend;

import org.openkoala.exception.base.BaseException;

/**
 * 类    名：org.openkoala.exception.extend.ApplicationException<br />
 *   
 * 功能描述：应用异常	<br />
 *  
 * 创建日期：2013-1-21  <br />   
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
public class ApplicationException extends BaseException {

	private static final long serialVersionUID = 5394041231546831105L;

	public ApplicationException(String errorCode) {
		this(errorCode, null);
	}
	
	public ApplicationException(String errorCode, String defaultMessage) {
		super(errorCode, defaultMessage);
	}

	public ApplicationException(Throwable cause, String errorCode,
			String defaultMessage) {
		super(cause, errorCode, defaultMessage);
	}

	public ApplicationException(Throwable cause, String errorCode) {
		super(cause, errorCode);
	}

	
}
