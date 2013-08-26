package com.dayatang.datasource4saas.dscreator;

import com.dayatang.configuration.Configuration;

/**
 * 租户数据库映射策略。
 * 
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 * 
 */
public enum TenantDbMappingStrategy {
	SCHEMA {
		@Override
		public void process(DbInfo dbInfo, String tenant, Configuration dbTenantMappings) {
			dbInfo.setUsername(dbTenantMappings.getString(tenant));
		}
	},
	DBNAME {
		@Override
		public void process(DbInfo dbInfo, String tenant, Configuration dbTenantMappings) {
			dbInfo.setDbname(dbTenantMappings.getString(tenant));
		}
	},
	HOST {
		@Override
		public void process(DbInfo dbInfo, String tenant, Configuration dbTenantMappings) {
			dbInfo.setHost(dbTenantMappings.getString(tenant));
		}
	},
	PORT {
		@Override
		public void process(DbInfo dbInfo, String tenant, Configuration dbTenantMappings) {
			dbInfo.setPort(dbTenantMappings.getString(tenant));
		}
	},
	INSTANCE {
		@Override
		public void process(DbInfo dbInfo, String tenant, Configuration dbTenantMappings) {
			dbInfo.setInstance(dbTenantMappings.getString(tenant));
		}
	};

	public static TenantDbMappingStrategy of(String value) {
		for (TenantDbMappingStrategy each : TenantDbMappingStrategy.values()) {
			if (each.name().equalsIgnoreCase(value)) {
				return each;
			}
		}
		throw new IllegalStateException("Tenant DB mapping strategy '" + value + "' not existsDataSourceOfTenant!");
	}

	public abstract void process(DbInfo dbInfo, String tenant, Configuration dbTenantMappings);
}
