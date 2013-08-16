package org.openkoala.exception.extend;

/**
 * 类    名：org.openkoala.exception.extend.BizException<br />
 *   
 * 功能描述：业务异常<br />
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
public class BizException extends ApplicationException {

	private static final long serialVersionUID = -140076445751602150L;

	public BizException(String errorCode, String defaultMessage) {
		super(errorCode, defaultMessage);
	}

	public BizException(Throwable cause, String errorCode, String defaultMessage) {
		super(cause, errorCode, defaultMessage);
	}

	public BizException(Throwable cause, String errorCode) {
		super(cause, errorCode);
	}

	
}
