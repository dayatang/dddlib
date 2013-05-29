package com.dayatang.dsrouter.dscreator;

import com.dayatang.configuration.Configuration;

/**
 * 租户数据库映射策略。
 * 
 * @author yyang
 * 
 */
public enum TenantDbMappingStrategy {
	SCHEMA {
		@Override
		public String getSchema(String tenant, String defaultValue, Configuration mappings) {
			return mappings.getString(tenant, defaultValue);
		}
	},
	
	DBNAME {
		@Override
		public String getDbName(String tenant, String defaultValue, Configuration mappings) {
			return mappings.getString(tenant, defaultValue);
		}
	},
	
	HOST {
		@Override
		public String getHost(String tenant, String defaultValue, Configuration mappings) {
			return mappings.getString(tenant);
		}
	}, 
	
	PORT {
		@Override
		public String getPort(String tenant, String defaultValue, Configuration mappings) {
			return mappings.getString(tenant);
		}
	}, 
	
	INSTANCE {
		@Override
		public String getInstanceName(String tenant, String defaultValue, Configuration mappings) {
			return mappings.getString(tenant);
		}
	};

	public String getPort(String tenant, String defaultValue, Configuration mappings) {
		return defaultValue;
	}

	public String getDbName(String tenant, String defaultValue, Configuration mappings) {
		return defaultValue;
	}

	public String getHost(String tenant, String defaultValue, Configuration mappings) {
		return defaultValue;
	}

	public String getSchema(String tenant, String defaultValue, Configuration mappings) {
		return defaultValue;
	}

	public String getInstanceName(String tenant, String defaultValue, Configuration mappings) {
		return defaultValue;
	}

	public static TenantDbMappingStrategy of(String value) {
		for (TenantDbMappingStrategy each : TenantDbMappingStrategy.values()) {
			if (each.name().equalsIgnoreCase(value)) {
				return each;
			}
		}
		throw new IllegalStateException("Tenant DB mapping strategy '" + value + "' not existsDataSourceOfTenant!");
	}
}
