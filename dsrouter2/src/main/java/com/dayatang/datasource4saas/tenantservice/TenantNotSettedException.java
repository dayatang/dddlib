package com.dayatang.datasource4saas.tenantservice;

public class TenantNotSettedException extends RuntimeException {

	private static final long serialVersionUID = 7983830505725201735L;

	public TenantNotSettedException() {
	}

	public TenantNotSettedException(String arg0) {
		super(arg0);
	}

	public TenantNotSettedException(Throwable arg0) {
		super(arg0);
	}

	public TenantNotSettedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
