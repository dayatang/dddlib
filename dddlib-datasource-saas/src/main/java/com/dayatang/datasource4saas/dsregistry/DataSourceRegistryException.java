package com.dayatang.datasource4saas.dsregistry;

public class DataSourceRegistryException extends RuntimeException {

	private static final long serialVersionUID = -7765655688299199245L;

	public DataSourceRegistryException() {
	}

	public DataSourceRegistryException(String message) {
		super(message);
	}

	public DataSourceRegistryException(Throwable cause) {
		super(cause);
	}

	public DataSourceRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

}
