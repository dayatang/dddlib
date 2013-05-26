package com.dayatang.dsrouter.dscreator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.utils.Slf4jLogger;

public abstract class AbstractDataSourceCreator implements DataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(AbstractDataSourceCreator.class);

	private TenantDbMappingStrategy mappingStrategy;
	private Configuration dsConfiguration;
	private Configuration tenantDbMapping;

	private DbType dbType;
	private DataSource dataSource;

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

	// 主要为了可测试性设置
	void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource createDataSourceForTenant(String tenant) {
		if (dataSource == null) {
			dataSource = createDataSource();
		}
		try {
			fillProperties(dataSource);
			fillStandardProperties(dataSource, tenant);
			return dataSource;
		} catch (Exception e) {
			String message = "Create data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}

	protected abstract DataSource createDataSource();

	protected void fillProperties(DataSource dataSource) throws IllegalAccessException, InvocationTargetException {
		Properties dsProperties = getDsConfiguration().getProperties();
		for (Entry<Object, Object> entry : dsProperties.entrySet()) {
			BeanUtils.setProperty(dataSource, entry.getKey().toString(), entry.getValue());
		}
	}

	private void fillStandardProperties(DataSource dataSource, String tenant) throws IllegalAccessException,
			InvocationTargetException {
		Map<String, String> standardProperties = getStandardPropMappings();
		BeanUtils.setProperty(dataSource, standardProperties.get(Constants.JDBC_DRIVER_CLASS_NAME),
				dsConfiguration.getString(Constants.JDBC_DRIVER_CLASS_NAME));
		BeanUtils.setProperty(dataSource, standardProperties.get(Constants.JDBC_URL), getUrl(tenant));
		BeanUtils.setProperty(dataSource, standardProperties.get(Constants.JDBC_USERNAME),
				getUsername(tenant, dsConfiguration.getString(Constants.JDBC_USERNAME)));
		BeanUtils.setProperty(dataSource, standardProperties.get(Constants.JDBC_PASSWORD),
				dsConfiguration.getString(Constants.JDBC_PASSWORD));
	}

	private String getUsername(String tenant, String defaultValue) {
		return mappingStrategy.getSchema(tenant, defaultValue, tenantDbMapping);
	}

	private String getUrl(String tenant) {
		return dbType.getUrl(tenant, dsConfiguration, mappingStrategy, tenantDbMapping);
	}

	protected abstract Map<String, String> getStandardPropMappings();

	@SuppressWarnings("unused")
	private static void printDsProps(DataSource result) throws IllegalAccessException, InvocationTargetException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> dsProps = BeanUtils.describe(result);
			for (String key : dsProps.keySet()) {
				// LOGGER.debug("----------------{}: {}", key,
				// dsProps.get(key));
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

}
