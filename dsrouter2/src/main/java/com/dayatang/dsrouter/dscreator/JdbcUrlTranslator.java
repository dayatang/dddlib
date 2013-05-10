package com.dayatang.dsrouter.dscreator;

import java.util.Properties;

public interface JdbcUrlTranslator {

	String translateUrl(String tenant, Properties properties);
	
}
