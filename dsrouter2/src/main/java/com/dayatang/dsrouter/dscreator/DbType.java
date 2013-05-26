package com.dayatang.dsrouter.dscreator;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.configuration.Configuration;
import com.dayatang.dsrouter.Constants;

public enum DbType {
	
	MYSQL {
		@Override
		public String getUrl(String tenant, Configuration dsConfiguration, TenantDbMappingStrategy mappingStrategy,
				Configuration tenantDbMapping) {
			String result =  String.format("jdbc:mysql://%s:%s/%s", 
					mappingStrategy.getHost(tenant, dsConfiguration.getString(Constants.JDBC_HOST), tenantDbMapping), 
					mappingStrategy.getPort(tenant, dsConfiguration.getString(Constants.JDBC_PORT), tenantDbMapping), 
					mappingStrategy.getDbName(tenant, dsConfiguration.getString(Constants.JDBC_DB_NAME), tenantDbMapping));
			String extraUrlString = dsConfiguration.getString(Constants.JDBC_EXTRA_URL_STRING);
			if (StringUtils.isBlank(extraUrlString)) {
				return result;
			}
			return extraUrlString.startsWith("?") ? result + extraUrlString : result + "?" + extraUrlString;
		}
	},
	ORACLE {
		@Override
		public String getUrl(String tenant, Configuration dsConfiguration, TenantDbMappingStrategy mappingStrategy,
				Configuration tenantDbMapping) {
			return String.format("jdbc:oracle:thin:@%s:%s:%s", 
					mappingStrategy.getHost(tenant, dsConfiguration.getString(Constants.JDBC_HOST), tenantDbMapping), 
					mappingStrategy.getPort(tenant, dsConfiguration.getString(Constants.JDBC_PORT), tenantDbMapping), 
					mappingStrategy.getDbName(tenant, dsConfiguration.getString(Constants.JDBC_INSTANCE), tenantDbMapping));
		}
	},
	POSTGRESQL {
		@Override
		public String getUrl(String tenant, Configuration dsConfiguration, TenantDbMappingStrategy mappingStrategy,
				Configuration tenantDbMapping) {
			String result =  String.format("jdbc:postgresql://%s:%s/%s", 
					mappingStrategy.getHost(tenant, dsConfiguration.getString(Constants.JDBC_HOST), tenantDbMapping), 
					mappingStrategy.getPort(tenant, dsConfiguration.getString(Constants.JDBC_PORT), tenantDbMapping), 
					mappingStrategy.getDbName(tenant, dsConfiguration.getString(Constants.JDBC_DB_NAME), tenantDbMapping));
			String extraUrlString = dsConfiguration.getString(Constants.JDBC_EXTRA_URL_STRING);
			if (StringUtils.isBlank(extraUrlString)) {
				return result;
			}
			return extraUrlString.startsWith("?") ? result + extraUrlString : result + "?" + extraUrlString;
		}
	};

	public abstract String getUrl(String tenant, Configuration dsConfiguration, TenantDbMappingStrategy mappingStrategy,
			Configuration tenantDbMapping);

	public static DbType of(String value) {
		for (DbType each : DbType.values()) {
			if (each.name().equalsIgnoreCase(value)) {
				return each;
			}
		}
		throw new IllegalStateException("DB type '" + value + "' not exists!");
	}
	
}
