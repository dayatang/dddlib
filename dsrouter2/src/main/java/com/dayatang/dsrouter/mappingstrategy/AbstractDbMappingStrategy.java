package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.urltranslator.DbMappingStrategy;
import com.dayatang.utils.Assert;
import com.dayatang.configuration.Configuration;

/**
 * 租户数据库映射策略。
 * @author yyang
 *
 */
public abstract class AbstractDbMappingStrategy implements DbMappingStrategy {

	private Configuration configuration;

	public AbstractDbMappingStrategy(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		Assert.notNull(configuration, "Mapping info not exists!");
		return configuration;
	}

	public String getPort(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_PORT);
	}

	public String getDbName(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_DB_NAME);
	}

	public String getHost(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_HOST);
	}

	public String getSchema(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_SCHEMA);
	}

	public String getInstanceName(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_INSTANCE);
	}
}
