package com.dayatang.dsrouter.dscreator;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.dsrouter.Constants;
import com.dayatang.utils.Assert;


public class JdbcConfiguration {
	private String tenant;
	private Configuration dbTenantMappings;
	private DbType dbType;
	private TenantDbMappingStrategy mappingStrategy;
	private String driverClassName;
	private String host;
	private String port;
	private String dbname;
	private String instance;
	private String username;
	private String password;
	private String extraUrlString;

	public JdbcConfiguration(String tenant) {
		this(tenant, new ConfigurationFactory().fromClasspath(Constants.DB_CONF_FILE), 
				new ConfigurationFactory().fromClasspath(Constants.DB_MAPPING_FILE));
	}

	public JdbcConfiguration(String tenant, Configuration dsConfiguration, Configuration dbTenantMappings) {
		Assert.notNull(dsConfiguration);
		Assert.notNull(dbTenantMappings);
		this.tenant = tenant;
		this.dbTenantMappings = dbTenantMappings;
		dbType = DbType.of(dsConfiguration.getString(Constants.DB_TYPE));
		Assert.notNull(dbType);
		mappingStrategy = TenantDbMappingStrategy.of(dsConfiguration.getString(Constants.TENANT_MAPPING_STRATEGY));
		if (mappingStrategy == null) {
			mappingStrategy = TenantDbMappingStrategy.DBNAME;
		}
		driverClassName = dsConfiguration.getString(Constants.JDBC_DRIVER_CLASS_NAME);
		host = dsConfiguration.getString(Constants.JDBC_HOST);
		port = dsConfiguration.getString(Constants.JDBC_PORT);
		dbname = dsConfiguration.getString(Constants.JDBC_DB_NAME);
		instance = dsConfiguration.getString(Constants.JDBC_INSTANCE);
		username = dsConfiguration.getString(Constants.JDBC_USERNAME);
		password = dsConfiguration.getString(Constants.JDBC_PASSWORD);
		extraUrlString = dsConfiguration.getString(Constants.JDBC_EXTRA_URL_STRING);
	}

	public void setDbType(DbType dbType) {
		this.dbType = dbType;
	}
	
	public void setMappingStrategy(TenantDbMappingStrategy mappingStrategy) {
		this.mappingStrategy = mappingStrategy;
	}
	
	public String getDriverClassName() {
		return driverClassName;
	}

	public String getHost() {
		if (mappingStrategy == TenantDbMappingStrategy.HOST) {
			return dbTenantMappings.getString(tenant);
		}
		return host;
	}

	public String getPort() {
		if (mappingStrategy == TenantDbMappingStrategy.PORT) {
			return dbTenantMappings.getString(tenant);
		}
		return port;
	}

	public String getDbname() {
		if (mappingStrategy == TenantDbMappingStrategy.DBNAME) {
			return dbTenantMappings.getString(tenant);
		}
		return dbname;
	}

	public String getInstance() {
		if (mappingStrategy == TenantDbMappingStrategy.INSTANCE) {
			return dbTenantMappings.getString(tenant);
		}
		return instance;
	}

	public String getUsername() {
		if (mappingStrategy == TenantDbMappingStrategy.SCHEMA) {
			return dbTenantMappings.getString(tenant);
		}
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getExtraUrlString() {
		return extraUrlString;
	}

	public String getUrl() {
		return dbType.getUrl(this);
	}
}
