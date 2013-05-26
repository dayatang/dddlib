package com.dayatang.dsrouter.dscreator;

/**
 * 数据源创建异常。
 * @author yyang
 *
 */
public class DataSourceCreationException extends RuntimeException {

	private static final long serialVersionUID = -4211956277706156746L;

	public DataSourceCreationException() {
	}

	public DataSourceCreationException(String arg0) {
		super(arg0);
	}

	public DataSourceCreationException(Throwable arg0) {
		super(arg0);
	}

	public DataSourceCreationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
