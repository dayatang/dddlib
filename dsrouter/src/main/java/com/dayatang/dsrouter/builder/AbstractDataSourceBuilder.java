package com.dayatang.dsrouter.builder;

import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDataSourceBuilder implements DataSourceBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5389391685137579724L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataSourceBuilder.class);

	protected abstract Class<? extends DataSource> getDataSourceClass();

	protected abstract String getPasswordProperty();

	protected abstract String getUserProperty();

	protected abstract String getJdbcUrlProperty();

	protected abstract String getDriverProperty();

	protected abstract Class<? extends Driver> getDriverClass(String url);

	protected abstract String getJdbcUrl(String url);

	protected abstract Properties getDefaultProp();

	@Override
	public DataSource buildDataSource(Properties prop) {
		if (prop == null) {
			throw new IllegalArgumentException("datasource's prop is null.");
		}

		DataSource dataSource = null;
		try {
			dataSource = getDataSourceClass().newInstance();
			debug("准备为【{}】构建数据源，prop=【{}】", new Object[] { getDataSourceClass(), prop });
			// dataSource = new ComboPooledDataSource();
			assembleProps(dataSource, prop);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dataSource;
	}

	@Override
	public DataSource buildDataSource(String url, String username, String password, Properties prop) {
		DataSource dataSource = null;
		try {
			dataSource = getDataSourceClass().newInstance();
			debug("准备为【{}】构建数据源，url=【{}】，username=【{}】，password=【****】", new Object[] { getDataSourceClass(), url, username });
			// dataSource = new ComboPooledDataSource();
			assembleBasicProp(dataSource, url, username, password);

			if (prop == null) {
				assembleProps(dataSource, getDefaultProp());
			} else {
				assembleProps(dataSource, prop);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dataSource;
	}

	protected void assembleBasicProp(DataSource dataSource, String url, String username, String password) throws IllegalAccessException,
			InvocationTargetException {
		debug("正在为数据源【{}】组装基础属性driverClass=【{}】，jdbcUrl=【{}】，user=【{}】，password=【****】", new Object[] { getDataSourceClass(),
				getDriverClass(url).getName(), getJdbcUrl(url), username });

		BeanUtils.setProperty(dataSource, getDriverProperty(), getDriverClass(url).getName());
		BeanUtils.setProperty(dataSource, getJdbcUrlProperty(), getJdbcUrl(url));
		BeanUtils.setProperty(dataSource, getUserProperty(), username);
		BeanUtils.setProperty(dataSource, getPasswordProperty(), password);
	}

	private void assembleProps(DataSource dataSource, Properties properties) throws IllegalAccessException, InvocationTargetException {
		if (properties == null) {
			return;
		}
		for (Map.Entry<Object, Object> each : properties.entrySet()) {
			debug("正在为数据源【{}】组装可选属性【{}】=【{}】", new Object[] { dataSource, each.getKey(), each.getValue() });
			BeanUtils.setProperty(dataSource, (String) each.getKey(), each.getValue());
		}
	}

	private void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}
}
