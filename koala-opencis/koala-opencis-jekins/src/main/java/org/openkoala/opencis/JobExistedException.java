package org.openkoala.opencis;

/**
 * Job已经存在异常
 * @author zhuyuanbiao
 * @date 2013年12月18日 下午4:33:29
 *
 */
public class JobExistedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1828633110513627939L;

	public JobExistedException() {
		super();
	}

	public JobExistedException(String message, Throwable cause) {
		super(message, cause);
	}

	public JobExistedException(String message) {
		super(message);
	}

	public JobExistedException(Throwable cause) {
		super(cause);
	}

}
