package com.dayatang.datasource4saas.dscreator;


/**
 * 租户数据库映射策略。
 * 
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 * 
 */
public enum TenantDbMappingStrategy {
	SCHEMA, DBNAME, HOST, PORT, INSTANCE;

	public static TenantDbMappingStrategy of(String value) {
		for (TenantDbMappingStrategy each : TenantDbMappingStrategy.values()) {
			if (each.name().equalsIgnoreCase(value)) {
				return each;
			}
		}
		throw new IllegalStateException("Tenant DB mapping strategy '" + value + "' not existsDataSourceOfTenant!");
	}
}
