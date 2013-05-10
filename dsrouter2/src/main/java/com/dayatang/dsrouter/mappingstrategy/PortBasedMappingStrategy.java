package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

import com.dayatang.configuration.Configuration;

public class PortBasedMappingStrategy extends AbstractDbMappingStrategy {

	public PortBasedMappingStrategy(Configuration configuration) {
		super(configuration);
	}

	@Override
	public String getPort(String tenant, Properties properties) {
		return getConfiguration().getString(tenant);
	}

}
