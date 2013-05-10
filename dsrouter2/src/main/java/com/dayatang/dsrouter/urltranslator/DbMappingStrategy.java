package com.dayatang.dsrouter.urltranslator;

import java.util.Properties;

/**
 * 租户数据库映射策略。
 * @author yyang
 *
 */
public interface DbMappingStrategy {

	String getPort(String tenant, Properties properties);

	String getDbName(String tenant, Properties properties);

	String getHost(String tenant, Properties properties);

	String getSchema(String tenant, Properties properties);

	String getInstanceName(String tenant, Properties properties);
}
