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
	
	protected JdbcConfiguration getJdbcConfiguration(String tenant) {
		return new JdbcConfiguration(tenant, getDsConfiguration(), getTenantDbMapping());
	}
}
