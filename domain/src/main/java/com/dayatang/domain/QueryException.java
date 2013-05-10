package com.dayatang.domain;

public class QueryException extends RuntimeException {

	private static final long serialVersionUID = -6280573107268515266L;

	public QueryException() {
	}

	public QueryException(String message) {
		super(message);
	}

	public QueryException(Throwable cause) {
		super(cause);
	}

	public QueryException(String message, Throwable cause) {
		super(message, cause);
	}

}
