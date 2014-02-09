package org.openkoala.opencis.git;

public class NoGitlabHostException extends GitCISConfigurationException {

	private static final long serialVersionUID = 8957509044359439104L;

	public NoGitlabHostException() {
		super();
	}
	
	public NoGitlabHostException(String message) {
		super(message);
	}
	
	public NoGitlabHostException(Throwable cause) {
		super(cause);
	}
	
	public NoGitlabHostException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
