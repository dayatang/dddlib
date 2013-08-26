package com.dayatang.datasource4saas.dscreator;

import org.apache.commons.lang3.StringUtils;

public enum DbType {

	MYSQL {

		
		@Override
		public String getDriverClassName() {
			return "com.mysql.jdbc.Driver";
		}

		@Override
		public String getUrl(String host, String port, String dbname,
				String instance, String username, String extraUrlString) {
			String result = String.format("jdbc:mysql://%s:%s/%s", host, port, dbname);
			return addExtraUrlStringIfExists(result, extraUrlString);
		}

	},
	
	POSTGRESQL {

		@Override
		public String getDriverClassName() {
			return "org.postgresql.Driver";
		}

		@Override
		public String getUrl(String host, String port, String dbname,
				String instance, String username, String extraUrlString) {
			String result = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbname);
			return addExtraUrlStringIfExists(result, extraUrlString);
		}
	},
	
	ORACLE {

		@Override
		public String getDriverClassName() {
			return "oracle.jdbc.OracleDriver";
		}

		@Override
		public String getUrl(String host, String port, String dbname,
				String instance, String username, String extraUrlString) {
			String result = String.format("jdbc:oracle:thin:@%s:%s:%s", host, port, instance);
			return addExtraUrlStringIfExists(result, extraUrlString);
		}
	},
	
	DB2 {

		@Override
		public String getDriverClassName() {
			return "com.ibm.db2.jcc.DB2Driver";
		}

		@Override
		public String getUrl(String host, String port, String dbname,
				String instance, String username, String extraUrlString) {
			String result = String.format("jdbc:db2://%s:%s/%s", host, port, dbname);
			return addExtraUrlStringIfExists(result, extraUrlString);
		}
	},
	
	SQLSERVER {

		@Override
		public String getDriverClassName() {
			return "net.sourceforge.jtds.jdbc.Driver";
		}

		@Override
		public String getUrl(String host, String port, String dbname,
				String instance, String username, String extraUrlString) {
			String result = String.format("jdbc:jtds:sqlserver://%s:%s/%s", host, port, dbname);
			return addExtraUrlStringIfExists(result, extraUrlString);
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
	
	public abstract String getDriverClassName();

	public  abstract String getUrl(String host, String port, String dbname,
			String instance, String username, String extraUrlString);
	
	protected String addExtraUrlStringIfExists(String url, String extraUrlString) {
		if (StringUtils.isBlank(extraUrlString)) {
			return url;
		}
		return extraUrlString.startsWith("?") ? url + extraUrlString : url + "?" + extraUrlString;
		
	}
}
