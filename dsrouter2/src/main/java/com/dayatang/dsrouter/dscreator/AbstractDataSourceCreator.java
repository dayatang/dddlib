package com.dayatang.dsrouter.dscreator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;

/**
 * 抽象数据源创建器实现
 * @author yyang
 *
 */
public abstract class AbstractDataSourceCreator implements DataSourceCreator {

	private Configuration dsConfiguration;				//数据源配置
	private Configuration tenantDbMapping;				//租户到数据库链接参数的映射
	private DbType dbType;								//数据库类型
	private TenantDbMappingStrategy mappingStrategy;	//租户到数据库参数的映射策略

	/**
	 * 获取数据源配置
	 * @return
	 */
	public Configuration getDsConfiguration() {
		if (dsConfiguration == null) {
			dsConfiguration = new ConfigurationFactory().fromClasspath(Constants.DB_CONF_FILE);
		}
		return dsConfiguration;
	}

	/**
	 * 设置数据源配置
	 * @param dsConfiguration
	 */
	public void setDsConfiguration(Configuration dsConfiguration) {
		this.dsConfiguration = dsConfiguration;
	}

	public Configuration getTenantDbMapping() {
		if (tenantDbMapping == null) {
			tenantDbMapping = new ConfigurationFactory().fromClasspath(Constants.DB_MAPPING_FILE);
		}
		return tenantDbMapping;
	}

	/**
	 * 设置租户数据源映射
	 * @param tenantDbMapping
	 */
	public void setTenantDbMapping(Configuration tenantDbMapping) {
		this.tenantDbMapping = tenantDbMapping;
	}

	public DbType getDbType() {
		if (dbType == null) {
			dbType = DbType.of(getDsConfiguration().getString(Constants.DB_TYPE));
		}
		return dbType;
	}

	public void setDbType(DbType dbType) {
		this.dbType = dbType;
	}

	public TenantDbMappingStrategy getMappingStrategy() {
		if (mappingStrategy == null) {
			mappingStrategy = TenantDbMappingStrategy.of(getDsConfiguration().getString(Constants.TENANT_MAPPING_STRATEGY));
		}
		return mappingStrategy;
	}

	public void setMappingStrategy(TenantDbMappingStrategy mappingStrategy) {
		this.mappingStrategy = mappingStrategy;
	}

	/**
	 * 填充数据库连接池特定的数据源配置。
	 * @param dataSource
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected void fillProperties(DataSource dataSource) throws IllegalAccessException, InvocationTargetException {
		Properties dsProperties = getDsConfiguration().getProperties();
		for (Entry<Object, Object> entry : dsProperties.entrySet()) {
			BeanUtils.setProperty(dataSource, entry.getKey().toString(), entry.getValue());
		}
	}

	/**
	 * 根据租户获取JDBC用户名。
	 * @param tenant
	 * @return
	 */
	protected String getUsername(String tenant) {
		return getMappingStrategy().getSchema(tenant, getDsConfiguration().getString(Constants.JDBC_USERNAME), getTenantDbMapping());
	}

	/**
	 * 根据租户获取JDBC URL。
	 * @param tenant
	 * @return
	 */
	protected String getUrl(String tenant) {
		JdbcConfiguration jdbcConfiguration = new JdbcConfiguration(getDsConfiguration());
		jdbcConfiguration.setDbname(getMappingStrategy().getDbName(tenant, jdbcConfiguration.getDbname(), getTenantDbMapping()));
		jdbcConfiguration.setHost(getMappingStrategy().getHost(tenant, jdbcConfiguration.getHost(), getTenantDbMapping()));
		jdbcConfiguration.setInstance(getMappingStrategy().getInstanceName(tenant, jdbcConfiguration.getInstance(), getTenantDbMapping()));
		jdbcConfiguration.setPort(getMappingStrategy().getPort(tenant, jdbcConfiguration.getPort(), getTenantDbMapping()));
		return getDbType().getUrl(jdbcConfiguration);
	}
}
