package org.openkoala.exception.extend;

import org.openkoala.exception.base.BaseException;

/**
 * 类    名：org.openkoala.exception.extend.KoalaException<br />
 *   
 * 功能描述：koala框架异常<br />
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
public class KoalaException extends BaseException {

	private static final long serialVersionUID = -3420470129689449107L;

	public KoalaException(String errorCode, String defaultMessage) {
		super(errorCode, defaultMessage);
	}

	public KoalaException(Throwable cause, String errorCode,
			String defaultMessage) {
		super(cause, errorCode, defaultMessage);
	}

	public KoalaException(Throwable cause, String errorCode) {
		super(cause, errorCode);
	}

	

}
