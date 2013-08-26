package com.dayatang.excel;

public class ExcelException extends RuntimeException {

	private static final long serialVersionUID = -1339154856062033012L;

	public ExcelException() {
	}

	public ExcelException(String message) {
		super(message);
	}

	public ExcelException(Throwable cause) {
		super(cause);
	}

	public ExcelException(String message, Throwable cause) {
		super(message, cause);
	}

}
