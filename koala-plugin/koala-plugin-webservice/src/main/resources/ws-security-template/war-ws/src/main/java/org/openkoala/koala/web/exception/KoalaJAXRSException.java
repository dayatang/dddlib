package org.openkoala.koala.web.exception;

import java.io.Serializable;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Koala JAX-RS异常
 * @author zyb
 * @since 2013-6-27 上午10:38:59
 */
public class KoalaJAXRSException extends WebApplicationException {

	private static final long serialVersionUID = 324599482202936074L;

	public KoalaJAXRSException() {
	}
	
	public KoalaJAXRSException(String errorCode, String message) {
		super(Response.status(Status.INTERNAL_SERVER_ERROR).entity(new KoalaJAXRSFault(errorCode, message))
				.type(MediaType.APPLICATION_XML).build());
	}

	public KoalaJAXRSException(Response response) {
		super(response);
	}

	@XmlRootElement(name = "KoalaJAXRSFault")
	static class KoalaJAXRSFault implements Serializable {

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

}
