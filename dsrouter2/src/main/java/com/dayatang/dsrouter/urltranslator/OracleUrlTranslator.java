package com.dayatang.dsrouter.urltranslator;

import java.util.Properties;

import com.dayatang.dsrouter.dscreator.JdbcUrlTranslator;

public class OracleUrlTranslator implements JdbcUrlTranslator {

	private DbMappingStrategy mappingStrategy;
	
	public OracleUrlTranslator(DbMappingStrategy mappingStrategy) {
		this.mappingStrategy = mappingStrategy;
	}

	@Override
	public String translateUrl(String tenant, Properties properties) {
		return String.format("jdbc:oracle:thin:@%s:%s:%s", 
				mappingStrategy.getHost(tenant, properties), 
				mappingStrategy.getPort(tenant, properties), 
				mappingStrategy.getInstanceName(tenant, properties));
	}

}
