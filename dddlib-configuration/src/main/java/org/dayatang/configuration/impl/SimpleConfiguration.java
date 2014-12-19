package org.dayatang.configuration.impl;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

public class SimpleConfiguration extends AbstractConfiguration {

	public SimpleConfiguration() {
		super();
		hTable = new Hashtable<String, String>();
	}

	@Override
	public void load() {
	}

}
