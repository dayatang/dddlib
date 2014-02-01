package com.dayatang.dsrouter.context.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.dayatang.dsrouter.context.GlobalLoginDataSourceContext;

public class InMemoryDataSourceContext extends GlobalLoginDataSourceContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2074938459585490033L;

	private Map<String, String> urlMapping = new HashMap<String, String>();

	private Map<String, String> schemaMapping = new HashMap<String, String>();

	private Map<String, Properties> propertiesMapping = new HashMap<String, Properties>();

	/*
	 * getter and setter
	 */
	public Map<String, String> getUrlMapping() {
		return urlMapping;
	}

	public void setUrlMapping(Map<String, String> urlMapping) {
		this.urlMapping = urlMapping;
	}

	public Map<String, String> getSchemaMapping() {
		return schemaMapping;
	}

	public void setSchemaMapping(Map<String, String> schemaMapping) {
		this.schemaMapping = schemaMapping;
	}

	public Map<String, Properties> getPropertiesMapping() {
		return propertiesMapping;
	}

	public void setPropertiesMapping(Map<String, Properties> propertiesMapping) {
		this.propertiesMapping = propertiesMapping;
	}

	/*
	 * method
	 */

	@Override
	public Properties getProperties() {
		return propertiesMapping.get(determineCurrentLookupKey());
	}

	@Override
	public String getSchema() {
		return schemaMapping.get(determineCurrentLookupKey());
	}

	@Override
	public String getUrl() {
		return urlMapping.get(determineCurrentLookupKey());
	}

	protected Object determineCurrentLookupKey() {
		return ContextHolder.getContextType();
	}

}
