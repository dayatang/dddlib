package org.openkoala.opencis.git;

public class GitCISConfigurationException extends GitCISException {

	private static final long serialVersionUID = 8943812247135911385L;

	public GitCISConfigurationException() {
		super();
	}
	
	public GitCISConfigurationException(String message) {
		super(message);
	}
	
	public GitCISConfigurationException(Throwable cause) {
		super(cause);
	}
	
	public GitCISConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
