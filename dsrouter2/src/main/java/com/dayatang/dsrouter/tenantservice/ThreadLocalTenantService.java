package com.dayatang.dsrouter.tenantservice;

import com.dayatang.dsrouter.TenantService;

public class ThreadLocalTenantService implements TenantService {

	@Override
	public String getTenant() {
		return ThreadLocalTenantHolder.getTenant();
	}

}
