package com.dayatang.dsrouter.tenantservice;

import com.dayatang.dsrouter.TenantService;

/**
 * 基于ThreadLocal的租户服务，从局部线程变量中获取租户
 */
public class ThreadLocalTenantService implements TenantService {

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.dsrouter.TenantService#getTenant()
	 */
	@Override
	public String getTenant() {
		return ThreadLocalTenantHolder.getTenant();
	}

}
