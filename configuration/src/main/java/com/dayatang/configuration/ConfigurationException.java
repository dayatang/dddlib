package com.dayatang.configuration;

public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 8555018829304253349L;

	public ConfigurationException() {
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
