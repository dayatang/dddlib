package org.openkoala.koala.web.jaxrs.handler;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAX-RS异常封装
 * @author zyb
 * @since 2013-6-27 下午3:27:04
 */
@XmlRootElement(name = "KoalaJAXRSFault")
public class KoalaJAXRSFault implements Serializable {

	private static final long serialVersionUID = 3177076260183561767L;

	private String errorCode;
	
	private String message;
	
	public KoalaJAXRSFault() {
		
	}

	public KoalaJAXRSFault(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
