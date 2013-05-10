package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

import com.dayatang.configuration.Configuration;

public class InstanceBasedMappingStrategy extends AbstractDbMappingStrategy {

	public InstanceBasedMappingStrategy(Configuration configuration) {
		super(configuration);
	}

	@Override
	public String getInstanceName(String tenant, Properties properties) {
		return getConfiguration().getString(tenant);
	}

}
