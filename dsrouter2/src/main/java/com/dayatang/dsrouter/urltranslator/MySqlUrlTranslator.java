package com.dayatang.dsrouter.urltranslator;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.dscreator.JdbcUrlTranslator;
import com.dayatang.utils.Assert;

public class MySqlUrlTranslator implements JdbcUrlTranslator {

	private DbMappingStrategy mappingStrategy;

	public MySqlUrlTranslator(DbMappingStrategy mappingStrategy) {
		this.mappingStrategy = mappingStrategy;
	}

	@Override
	public String translateUrl(String tenant, Properties properties) {
		Assert.notNull(mappingStrategy, "Mapping strategy not assigned!");
		String result =  String.format("jdbc:mysql://%s:%s/%s", 
				mappingStrategy.getHost(tenant, properties), 
				mappingStrategy.getPort(tenant, properties), 
				mappingStrategy.getDbName(tenant, properties));
		String extraUrlString = properties.getProperty(Constants.JDBC_EXTRA_URL_STRING);
		if (StringUtils.isBlank(extraUrlString)) {
			return result;
		}
		return extraUrlString.startsWith("?") ? result + extraUrlString : result + "?" + extraUrlString;
	}

}
