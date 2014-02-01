package com.dayatang.datasource4saas;

/**
 * 系统常量定义
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface Constants {
	
	/**
	数据源配置文件
	 */
	String DB_CONF_FILE = "/ds-config.properties";

	/**
	 * 租户数据库映射文件
	 */
	String DB_MAPPING_FILE = "/tenant-db-mapping.properties";
	
	/**
	 * 数据库类型
	 */
	String DB_TYPE = "db.type";
	
	/**
	 * 租户数据库映射策略
	 */
	String TENANT_MAPPING_STRATEGY = "tenant.mapping.strategy";
	
	/**
	 * JDBC URL
	 */
	String JDBC_URL = "jdbc.url";
	
	/**
	 * 数据库服务器主机名/IP地址
	 */
	String JDBC_HOST = "jdbc.host";
	
	/**
	 * 数据库服务器监听TCP端口
	 */
	String JDBC_PORT = "jdbc.port";
	
	/**
	 * 数据库实例
	 */
	String JDBC_INSTANCE = "jdbc.instance";
	
	/**
	 * 数据库名字
	 */
	String JDBC_DB_NAME = "jdbc.dbname";
	
	/**
	 * JDBC URL额外字符串
	 */
	String JDBC_EXTRA_URL_STRING = "jdbc.extraUrlString";
	
	/**
	 * JDBC用户名
	 */
	String JDBC_USERNAME = "jdbc.username";
	
	/**
	 * JDBC口令
	 */
	String JDBC_PASSWORD = "jdbc.password";
}
