package com.dayatang;

public class InstanceProviderNotFoundException extends IocException {

	private static final long serialVersionUID = -742775077430352894L;

	public InstanceProviderNotFoundException() {
	}

	public InstanceProviderNotFoundException(String message) {
		super(message);
	}

	public InstanceProviderNotFoundException(Throwable cause) {
		super(cause);
	}

	public InstanceProviderNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
