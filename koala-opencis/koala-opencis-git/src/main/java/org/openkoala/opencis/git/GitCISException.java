package org.openkoala.opencis.git;

/**
 * Git CIS 异常基类
 * @author xmfang
 *
 */
public class GitCISException extends RuntimeException {

	private static final long serialVersionUID = 6621845391814111121L;

	public GitCISException() {
		super();
	}
	
	public GitCISException(String message) {
		super(message);
	}
	
	public GitCISException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GitCISException(Throwable cause) {
		super(cause);
	}
}
