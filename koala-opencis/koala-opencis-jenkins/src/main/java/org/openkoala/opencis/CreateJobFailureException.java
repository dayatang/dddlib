package org.openkoala.opencis;

/**
 * 创建job失败
 * @author zhuyuanbiao
 * @date 2013年12月18日 下午4:40:10
 *
 */
public class CreateJobFailureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1828633110513627939L;

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
