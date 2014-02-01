package com.dayatang.dsrouter.builder.mysql;

import java.sql.Driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.dsrouter.builder.AbstractDataSourceBuilder;
import com.dayatang.mysql.jdbc.GeminiReplicationDriver;

public abstract class AbstractMySQLDataSourceBuilder extends
		AbstractDataSourceBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7520434282533167135L;

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractMySQLDataSourceBuilder.class);

	@Override
	protected Class<? extends Driver> getDriverClass(String url) {
		if (url.indexOf(",") == -1) {
			return com.mysql.jdbc.Driver.class;
		}
		return GeminiReplicationDriver.class;
	}

	@Override
	protected String getJdbcUrl(String url) {
		return "jdbc:mysql://" + url
				+ "/?characterEncoding=UTF-8&useUnicode=true";
	}

}
