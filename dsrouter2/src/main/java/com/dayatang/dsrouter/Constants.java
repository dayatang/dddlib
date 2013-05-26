package com.dayatang.dsrouter;

/**
 * 系统常量定义
 * @author yyang
 *
 */
public interface Constants {
	
	/**
	数据源配置文件
	 */
	static final String DB_CONF_FILE = "/ds-config.properties";

	/**
	 * 租户数据库映射文件
	 */
	static final String DB_MAPPING_FILE = "/tenant-db-mapping.properties";
	
	/**
	 * 租户数据库映射策略
	 */
	static final String DB_TYPE = "db.type";
	
	/**
	 * 租户数据库映射策略
	 */
	static final String TENANT_MAPPING_STRATEGY = "tenant.mapping.strategy";
	
	/**
	 * JDBC驱动类名
	 */
	static final String JDBC_DRIVER_CLASS_NAME = "jdbc.driverClassName";
	
	/**
	 * JDBC URL
	 */
	static final String JDBC_URL = "jdbc.url";
	
	/**
	 * 数据库服务器主机名/IP地址
	 */
	static final String JDBC_HOST = "jdbc.host";
	
	/**
	 * 数据库服务器监听TCP端口
	 */
	static final String JDBC_PORT = "jdbc.port";
	
	/**
	 * 数据库实例
	 */
	static final String JDBC_INSTANCE = "jdbc.instance";
	
	/**
	 * 数据库SCHEMA
	 */
	static final String JDBC_SCHEMA = "jdbc.schema";
	
	/**
	 * 数据库名字
	 */
	static final String JDBC_DB_NAME = "jdbc.dbname";
	
	/**
	 * JDBC URL额外字符串
	 */
	static final String JDBC_EXTRA_URL_STRING = "jdbc.extraUrlString";
	
	/**
	 * JDBC用户名
	 */
	static final String JDBC_USERNAME = "jdbc.username";
	
	/**
	 * JDBC口令
	 */
	static final String JDBC_PASSWORD = "jdbc.password";
}
