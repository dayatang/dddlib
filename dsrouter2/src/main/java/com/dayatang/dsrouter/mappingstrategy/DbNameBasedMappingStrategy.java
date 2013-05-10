package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

import com.dayatang.configuration.Configuration;

public class DbNameBasedMappingStrategy extends AbstractDbMappingStrategy {

	public DbNameBasedMappingStrategy(Configuration configuration) {
		super(configuration);
	}


	@Override
	public String getDbName(String tenant, Properties properties) {
		return getConfiguration().getString(tenant);
	}

}
