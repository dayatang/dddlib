package com.dayatang.dsrouter.dscreator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.utils.Slf4jLogger;

public abstract class AbstractDataSourceCreator implements DataSourceCreator {
	
	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(AbstractDataSourceCreator.class);

	private JdbcUrlTranslator urlTranslator;
	private Configuration configuration;
	private DataSource dataSource;

	public AbstractDataSourceCreator(JdbcUrlTranslator urlTranslator) {
		this.urlTranslator = urlTranslator;
		this.configuration = new ConfigurationFactory().fromClasspath(Constants.DB_CONF_FILE);
	}

	public AbstractDataSourceCreator(JdbcUrlTranslator urlTranslator, Configuration configuration) {
		this.urlTranslator = urlTranslator;
		this.configuration = configuration;
	}

	//主要为了可测试性设置
	public void setDataSource(DataSource dataSource) {
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

	private void fillProperties(DataSource dataSource) throws IllegalAccessException, InvocationTargetException {
		Properties properties = configuration.getProperties();
		for (Object key : properties.keySet()) {
			BeanUtils.setProperty(dataSource, key.toString(), properties.get(key));
		}
	}
	
	private void fillStandardProperties(DataSource dataSource, String tenant) throws IllegalAccessException, InvocationTargetException {
		Map<String, String> standardProperties = getStandardPropMappings();
		for (Object key : standardProperties.keySet()) {
			BeanUtils.setProperty(dataSource, standardProperties.get(key), configuration.getString(key.toString()));
		}
		BeanUtils.setProperty(dataSource, standardProperties.get(Constants.JDBC_URL), urlTranslator.translateUrl(tenant, configuration.getProperties()));
	}
	
	protected abstract Map<String, String> getStandardPropMappings();

	@SuppressWarnings("unused")
	private static void printDsProps(DataSource result) throws IllegalAccessException, InvocationTargetException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> dsProps = BeanUtils.describe(result);
			for (String key : dsProps.keySet()) {
				//LOGGER.debug("----------------{}: {}", key, dsProps.get(key));
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

}
