package com.dayatang.datasource4saas.dscreator;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.datasource4saas.Constants;
import com.dayatang.utils.Assert;

/**
 * 根据数据库类型、映射策略、JDBC配置、租户数据库映射等信息，为特定租户提供JDBC四个基本属性：
 * 驱动程序类、URL，用户名，口令，其中URL和用户名可能是经过租户映射转换而得来的。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class JdbcConfiguration {
	private String tenant;
	private Configuration dbTenantMappings;
	private DbType dbType;
	private TenantDbMappingStrategy mappingStrategy;
	private String host;
	private String port;
	private String dbname;
	private String instance;
	private String username;
	private String password;
	private String extraUrlString;

	/**
	 * 生成特定租户的配置。从类路径下的ds-config.properties读取数据库类型、租户数据库映射策略和JDBC属性，从
	 * 类路径下的tenant-db-mappings.properties读取租户与数据库属性的映射。
	 * @param tenant 租户ID
	 */
	public JdbcConfiguration(String tenant) {
		this(tenant, new ConfigurationFactory().fromClasspath(Constants.DB_CONF_FILE), 
				new ConfigurationFactory().fromClasspath(Constants.DB_MAPPING_FILE));
	}

	/**
	 * 
	 * @param tenant 租户ID
	 * @param dsConfiguration 数据库类型、租户数据库映射策略和JDBC属性配置
	 * @param dbTenantMappings 租户与数据库属性的映射。
	 */
	public JdbcConfiguration(String tenant, Configuration dsConfiguration, Configuration dbTenantMappings) {
		Assert.notNull(dsConfiguration);
		Assert.notNull(dbTenantMappings);
		this.tenant = tenant;
		this.dbTenantMappings = dbTenantMappings;
		dbType = DbType.of(dsConfiguration.getString(Constants.DB_TYPE));
		Assert.notNull(dbType);
		mappingStrategy = TenantDbMappingStrategy.of(dsConfiguration.getString(Constants.TENANT_MAPPING_STRATEGY));
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
	
	private String getHost() {
		if (mappingStrategy == TenantDbMappingStrategy.HOST) {
			return dbTenantMappings.getString(tenant);
		}
		return host;
	}

	private String getPort() {
		if (mappingStrategy == TenantDbMappingStrategy.PORT) {
			return dbTenantMappings.getString(tenant);
		}
		return port;
	}

	private String getDbname() {
		if (mappingStrategy == TenantDbMappingStrategy.DBNAME) {
			return dbTenantMappings.getString(tenant);
		}
		return dbname;
	}

	private String getInstance() {
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

	public String getDriverClassName() {
		return dbType.getDriverClassName();
	}

	public String getUrl() {
		return dbType.getUrl(getHost(), getPort(), getDbname(), getInstance(), getUsername(), extraUrlString);
	}
}
