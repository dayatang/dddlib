package org.openkoala.koala.web.jaxrs.handler;

import java.io.Serializable;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.annotation.XmlRootElement;

import org.openkoala.exception.base.BaseException;
import org.openkoala.exception.extend.KoalaException;
import org.springframework.security.access.AccessDeniedException;

/**
 * JAX-RS异常处理
 * @author zyb
 * @since 2013-6-27 上午11:10:29
 */
@Provider
public class KoalaWsExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {
		if (exception instanceof BaseException) {
			BaseException baseException = (BaseException) exception;
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
					new KoalaJAXRSFault(baseException.getErrorCode(), exception.getMessage())).
					type(MediaType.APPLICATION_XML).build();
		}
		if (exception instanceof AccessDeniedException) {
			return Response.status(Status.UNAUTHORIZED).entity(
					new KoalaJAXRSFault("", "Denied to access.")).
					type(MediaType.APPLICATION_XML).build();
		}
		return null;
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
