package com.dayatang.datasource4saas.dscreator;

import org.apache.commons.lang3.StringUtils;

public enum DbType {

	MYSQL {
		@Override
		public String getUrl(JdbcConfiguration jdbcConfiguration) {
			String result = String.format("jdbc:mysql://%s:%s/%s", jdbcConfiguration.getHost(),
					jdbcConfiguration.getPort(), jdbcConfiguration.getDbname());
			String extraUrlString = jdbcConfiguration.getExtraUrlString();
			if (StringUtils.isBlank(extraUrlString)) {
				return result;
			}
			return extraUrlString.startsWith("?") ? result + extraUrlString : result + "?" + extraUrlString;
		}
	},
	
	POSTGRESQL {
		@Override
		public String getUrl(JdbcConfiguration jdbcConfiguration) {
			String result = String.format("jdbc:postgresql://%s:%s/%s", jdbcConfiguration.getHost(),
					jdbcConfiguration.getPort(), jdbcConfiguration.getDbname());
			String extraUrlString = jdbcConfiguration.getExtraUrlString();
			if (StringUtils.isBlank(extraUrlString)) {
				return result;
			}
			return extraUrlString.startsWith("?") ? result + extraUrlString : result + "?" + extraUrlString;
		}
	},
	
	ORACLE {
		@Override
		public String getUrl(JdbcConfiguration jdbcConfiguration) {
			return String.format("jdbc:oracle:thin:@%s:%s:%s", jdbcConfiguration.getHost(),
					jdbcConfiguration.getPort(), jdbcConfiguration.getInstance());
		}
	},
	
	DB2 {
		@Override
		public String getUrl(JdbcConfiguration jdbcConfiguration) {
			return String.format("jdbc:db2://%s:%s/%s", jdbcConfiguration.getHost(),
					jdbcConfiguration.getPort(), jdbcConfiguration.getDbname());
		}
	},
	
	SQLSERVER {
		@Override
		public String getUrl(JdbcConfiguration jdbcConfiguration) {
			return String.format("jdbc:jtds:sqlserver://%s:%s/%s", jdbcConfiguration.getHost(),
					jdbcConfiguration.getPort(), jdbcConfiguration.getDbname());
		}
	};

	public static DbType of(String value) {
		for (DbType each : DbType.values()) {
			if (each.name().equalsIgnoreCase(value)) {
				return each;
			}
		}
		throw new IllegalStateException("DB type '" + value + "' not existsDataSourceOfTenant!");
	}

	public abstract String getUrl(JdbcConfiguration jdbcConfiguration);

}
