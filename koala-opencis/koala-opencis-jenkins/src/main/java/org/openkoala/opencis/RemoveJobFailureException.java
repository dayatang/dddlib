package org.openkoala.opencis;

/**
 * 删除Job失败异常
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 14, 2013 2:53:15 PM
 */
public class RemoveJobFailureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 891138641196161467L;

	public RemoveJobFailureException() {
		super();
	}

	public RemoveJobFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public RemoveJobFailureException(String message) {
		super(message);
	}

	public RemoveJobFailureException(Throwable cause) {
		super(cause);
	}

}
