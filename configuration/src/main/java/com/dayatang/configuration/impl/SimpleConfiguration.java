package com.dayatang.configuration.impl;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

public class SimpleConfiguration extends AbstractConfiguration {

	public SimpleConfiguration() {
		super();
		hTable = new Hashtable<String, String>();
	}

	@Override
	public Properties getProperties() {
		Properties results = new Properties();
		for (Map.Entry<String, String> each : getHashtable().entrySet()) {
			results.put(each.getKey(), each.getValue());
		}
		return results;
	}

	@Override
	public void load() {
	}

}
