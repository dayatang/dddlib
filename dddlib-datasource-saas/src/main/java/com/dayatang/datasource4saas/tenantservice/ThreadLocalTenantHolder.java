package com.dayatang.datasource4saas.tenantservice;


/**
 * 用线程本地变量来存取租户名称的租户持有器的实现。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class ThreadLocalTenantHolder {
	
	private static final ThreadLocal<String> context = new ThreadLocal<String>();
	
	private ThreadLocalTenantHolder() {
	}

	public static String getTenant() {
		return context.get();
	}
	
	public static void setTenant(String tenant) {
		context.set(tenant);
	}

	public static void removeTenant() {
		context.remove();
	}
	
}
