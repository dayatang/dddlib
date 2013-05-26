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

public abstract class AbstractDataSourceCreator implements DataSourceCreator {

	private TenantDbMappingStrategy mappingStrategy;
	private Configuration dsConfiguration;
	private Configuration tenantDbMapping;

	private DbType dbType;

	public AbstractDataSourceCreator() {
		dsConfiguration = new ConfigurationFactory().fromClasspath(Constants.DB_CONF_FILE);
		tenantDbMapping = new ConfigurationFactory().fromClasspath(Constants.DB_MAPPING_FILE);
		mappingStrategy = TenantDbMappingStrategy.of(dsConfiguration.getString(Constants.TENANT_MAPPING_STRATEGY));
		dbType = DbType.of(dsConfiguration.getString(Constants.DB_TYPE));
	}

	public Configuration getDsConfiguration() {
		return dsConfiguration;
	}

	public void setDsConfiguration(Configuration dsConfiguration) {
		this.dsConfiguration = dsConfiguration;
	}

	public Configuration getTenantDbMapping() {
		return tenantDbMapping;
	}

	public void setTenantDbMapping(Configuration tenantDbMapping) {
		this.tenantDbMapping = tenantDbMapping;
	}

	protected void fillProperties(DataSource dataSource) throws IllegalAccessException, InvocationTargetException {
		Properties dsProperties = getDsConfiguration().getProperties();
		for (Entry<Object, Object> entry : dsProperties.entrySet()) {
			BeanUtils.setProperty(dataSource, entry.getKey().toString(), entry.getValue());
		}
	}

	protected String getUsername(String tenant, String defaultValue) {
		return mappingStrategy.getSchema(tenant, defaultValue, tenantDbMapping);
	}

	protected String getUrl(String tenant) {
		JdbcConfiguration jdbcConfiguration = new JdbcConfiguration(dsConfiguration);
		jdbcConfiguration.setDbname(mappingStrategy.getDbName(tenant, jdbcConfiguration.getDbname(), tenantDbMapping));
		jdbcConfiguration.setHost(mappingStrategy.getHost(tenant, jdbcConfiguration.getHost(), tenantDbMapping));
		jdbcConfiguration.setInstance(mappingStrategy.getInstanceName(tenant, jdbcConfiguration.getInstance(), tenantDbMapping));
		jdbcConfiguration.setPort(mappingStrategy.getPort(tenant, jdbcConfiguration.getPort(), tenantDbMapping));
		return dbType.getUrl(jdbcConfiguration);
	}
}
