package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

import com.dayatang.configuration.Configuration;

public class SchemaBasedMappingStrategy extends AbstractDbMappingStrategy {

	public SchemaBasedMappingStrategy(Configuration configuration) {
		super(configuration);
	}

	@Override
	public String getSchema(String tenant, Properties properties) {
		return getConfiguration().getString(tenant);
	}

}
