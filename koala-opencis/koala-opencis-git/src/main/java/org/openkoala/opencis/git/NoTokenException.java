package org.openkoala.opencis.git;

public class NoTokenException extends GitCISConfigurationException {

	private static final long serialVersionUID = -7520656932579916446L;

	public NoTokenException() {
		super();
	}
	
	public NoTokenException(String message) {
		super(message);
	}
	
	public NoTokenException(Throwable cause) {
		super(cause);
	}
	
	public NoTokenException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
