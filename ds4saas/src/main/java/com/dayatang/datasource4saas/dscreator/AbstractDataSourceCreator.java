package com.dayatang.datasource4saas.dscreator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.datasource4saas.Constants;
import com.dayatang.datasource4saas.dsregistry.DataSourceCreator;

/**
 * 抽象数据源创建器实现
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public abstract class AbstractDataSourceCreator implements DataSourceCreator {

	private Configuration dsConfiguration;				//数据源配置
	private Configuration tenantDbMapping;				//租户到数据库链接参数的映射
	/**
	 * 获取数据源配置
	 * @return
	 */
	private Configuration getDsConfiguration() {
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

	private Configuration getTenantDbMapping() {
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


	@Override
	public DataSource createDataSourceForTenant(String tenant) {
		DataSource result = createDataSource();
		fillProperties(result);
		JdbcConfiguration jdbcConfiguration = getJdbcConfiguration(tenant);
		setProperty(result, getDriverClassPropName(), jdbcConfiguration.getDriverClassName());
		setProperty(result, getUrlPropName(), jdbcConfiguration.getUrl());
		setProperty(result, getUsernamePropName(), jdbcConfiguration.getUsername());
		setProperty(result, getPasswordPropName(), jdbcConfiguration.getPassword());
		return result;
	}

	protected abstract DataSource createDataSource();
	
	protected abstract String getDriverClassPropName();
	
	protected abstract String getUrlPropName();
	
	protected abstract String getUsernamePropName();
	
	protected abstract String getPasswordPropName();

	/**
	 * 填充数据库连接池特定的数据源配置。
	 * @param dataSource
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void fillProperties(DataSource dataSource) {
		Properties dsProperties = getDsConfiguration().getProperties();
		for (Entry<Object, Object> entry : dsProperties.entrySet()) {
			setProperty(dataSource, entry.getKey().toString(), entry.getValue());
		}
	}

	private void setProperty(Object obj, String propName, Object propValue) {
		try {
			BeanUtils.setProperty(obj, propName, propValue);
		} catch (IllegalAccessException e) {
			throw new DataSourceCreationException("Datasource property setting failed", e);
		} catch (InvocationTargetException e) {
			throw new DataSourceCreationException("Datasource property setting failed", e);
		}
	}
	
	private JdbcConfiguration getJdbcConfiguration(String tenant) {
		return new JdbcConfiguration(tenant, getDsConfiguration(), getTenantDbMapping());
	}
}
