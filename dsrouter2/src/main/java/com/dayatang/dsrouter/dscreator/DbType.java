package com.dayatang.dsrouter.dscreator;

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
	
	ORACLE {
		@Override
		public String getUrl(JdbcConfiguration jdbcConfiguration) {
			return String.format("jdbc:oracle:thin:@%s:%s:%s", jdbcConfiguration.getHost(),
					jdbcConfiguration.getPort(), jdbcConfiguration.getInstance());
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
	
	DERBY {
		@Override
		public String getUrl(JdbcConfiguration jdbcConfiguration) {
			String result = String.format("jdbc:derby://%s/%s", jdbcConfiguration.getHost(),
					jdbcConfiguration.getDbname());
			String extraUrlString = jdbcConfiguration.getExtraUrlString();
			if (StringUtils.isBlank(extraUrlString)) {
				return result;
			}
			return extraUrlString.startsWith(";") ? result + extraUrlString : result + ";" + extraUrlString;
		}
	},

	;

	public static DbType of(String value) {
		for (DbType each : DbType.values()) {
			if (each.name().equalsIgnoreCase(value)) {
				return each;
			}
		}
		throw new IllegalStateException("DB type '" + value + "' not exists!");
	}

	public abstract String getUrl(JdbcConfiguration jdbcConfiguration);

}
