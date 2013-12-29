package org.openkoala.opencis.jenkins;

/**
 * 创建Job失败异常
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 10:24:32 AM
 */
public class CreateJobFailureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3248239266666279051L;

	public CreateJobFailureException() {
		super();
	}

	public CreateJobFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public CreateJobFailureException(String message) {
		super(message);
	}

	public CreateJobFailureException(Throwable cause) {
		super(cause);
	}

}
