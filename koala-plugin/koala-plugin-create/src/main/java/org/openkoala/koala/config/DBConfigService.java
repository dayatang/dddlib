package org.openkoala.koala.config;

import java.io.File;

import org.openkoala.koala.action.xml.XmlReader;
import org.openkoala.koala.action.xml.XmlWriter;
import org.openkoala.koala.config.vo.DBConfigVO;
import org.openkoala.koala.exception.KoalaException;
/**
 * 
 * 类    名：DBConfigService.java
 *   
 * 功能描述： 提供读取及写入数据库的功能
 *  
 * 创建日期：2013-1-29下午3:25:54     
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：lingen(lingen.liu@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class DBConfigService {

	//数据库配置属性
	private static final String CONNECTION_URL = "/xmlns:project/xmlns:properties/xmlns:db.connectionURL";

	private static final String DIALECT = "/xmlns:project/xmlns:properties/xmlns:hibernate.dialect";

	private static final String HBM2DLL = "/xmlns:project/xmlns:properties/xmlns:hibernate.hbm2ddl.auto";

	private static final String JDBC_DRIVER = "/xmlns:project/xmlns:properties/xmlns:db.jdbcDriver";

	private static final String PASSWORD = "/xmlns:project/xmlns:properties/xmlns:db.password";

	private static final String SHOW_SQL = "/xmlns:project/xmlns:properties/xmlns:hibernate.show_sql";

	private static final String USERNAME = "/xmlns:project/xmlns:properties/xmlns:db.username";

	private static final String DB_TYPE = "/xmlns:project/xmlns:properties/xmlns:db.Type";
	
	private static final String DB_GROUPID = "/xmlns:project/xmlns:properties/xmlns:db.groupId";
	
	private static final String DB_ARTIFACTID = "/xmlns:project/xmlns:properties/xmlns:db.artifactId";

	
	//测试环境数据库配置属性
	private static final String TEST_CONNECTION_URL = "/xmlns:project/xmlns:properties/xmlns:test.db.connectionURL";

	private static final String TEST_DIALECT = "/xmlns:project/xmlns:properties/xmlns:test.hibernate.dialect";

	private static final String TEST_HBM2DLL = "/xmlns:project/xmlns:properties/xmlns:test.hibernate.hbm2ddl.auto";

	private static final String TEST_JDBC_DRIVER = "/xmlns:project/xmlns:properties/xmlns:test.db.jdbcDriver";

	private static final String TEST_PASSWORD = "/xmlns:project/xmlns:properties/xmlns:test.db.password";

	private static final String TEST_SHOW_SQL = "/xmlns:project/xmlns:properties/xmlns:test.hibernate.show_sql";

	private static final String TEST_USERNAME = "/xmlns:project/xmlns:properties/xmlns:test.db.username";

	private static final String TEST_DB_TYPE = "/xmlns:project/xmlns:properties/xmlns:test.db.Type";
	
	private static final String TEST_DB_GROUPID = "/xmlns:project/xmlns:properties/xmlns:test.db.groupId";
	
	private static final String TEST_DB_ARTIFACTID = "/xmlns:project/xmlns:properties/xmlns:test.db.artifactId";

	/**
	 * 传入项目路径，返回当前项目已有的数据库配置
	 * 
	 * @param pomFile
	 * @return
	 * @throws KoalaException
	 */
	public static DBConfigVO queryDBConfig(String projectPath)
			throws KoalaException {
		String pomXml = projectPath + "/pom.xml";
		if (new File(pomXml).exists() == false)
			throw new KoalaException("指定的项目路径错误");

		XmlReader reader = XmlReader.getPomReader(pomXml);
		DBConfigVO config = new DBConfigVO();
		config.setConnectionURL(reader.queryString(CONNECTION_URL));
		config.setDialect(reader.queryString(DIALECT));
		config.setHbm2ddl(reader.queryString(HBM2DLL));
		config.setJdbcDriver(reader.queryString(JDBC_DRIVER));
		config.setPassword(reader.queryString(PASSWORD));
		config.setShowSql(reader.queryString(SHOW_SQL));
		config.setUsername(reader.queryString(USERNAME));
		config.setDbType(reader.queryString(DB_TYPE));
		config.setDbGroupId(reader.queryString(DB_GROUPID));
		config.setDbArtifactId(reader.queryString(DB_ARTIFACTID));
		return config;
	}

	/**
	 * 传入项目路径及配置好的dbConfig，更新项目的数据库配置
	 * 
	 * @param projectPath
	 * @param dbConfig
	 * @throws KoalaException
	 */
	public static void updateDBConfig(String projectPath, DBConfigVO dbConfig)
			throws KoalaException {
		String pomXml = projectPath + "/pom.xml";
		if (new File(pomXml).exists() == false)
			throw new KoalaException("指定的项目路径错误");
		dbConfig.setXmlPath(pomXml);
		XmlWriter writer = XmlWriter.getPomXmlWriter(pomXml);
		writer.update(CONNECTION_URL, dbConfig.getConnectionURL());
		writer.update(DIALECT, dbConfig.getDialect());
		writer.update(HBM2DLL, dbConfig.getHbm2ddl());
		writer.update(JDBC_DRIVER, dbConfig.getJdbcDriver());
		writer.update(PASSWORD, dbConfig.getPassword());
		writer.update(SHOW_SQL, dbConfig.getShowSql());
		writer.update(USERNAME, dbConfig.getUsername());
		writer.update(DB_TYPE, dbConfig.getDbType());
		writer.update(DB_GROUPID, dbConfig.getDbGroupId());
		writer.update(DB_ARTIFACTID, dbConfig.getDbArtifactId());
		writer.saveUpdate();
	}

	/**
	 * 传入项目路径，返回当前项目已有的测试数据库配置
	 * 
	 * @param pomFile
	 * @return
	 * @throws KoalaException
	 */
	public static DBConfigVO queryTestDBConfig(String projectPath) throws KoalaException {
		String pomXml = projectPath + "/pom.xml";
		if (new File(pomXml).exists() == false)
			throw new KoalaException("指定的项目路径错误");

		XmlReader reader = XmlReader.getPomReader(pomXml);
		DBConfigVO config = new DBConfigVO();
		config.setConnectionURL(reader.queryString(TEST_CONNECTION_URL));
		config.setDialect(reader.queryString(TEST_DIALECT));
		config.setHbm2ddl(reader.queryString(TEST_HBM2DLL));
		config.setJdbcDriver(reader.queryString(TEST_JDBC_DRIVER));
		config.setPassword(reader.queryString(TEST_PASSWORD));
		config.setShowSql(reader.queryString(TEST_SHOW_SQL));
		config.setUsername(reader.queryString(TEST_USERNAME));
		config.setDbType(reader.queryString(TEST_DB_TYPE));
		config.setDbGroupId(reader.queryString(TEST_DB_GROUPID));
		config.setDbArtifactId(reader.queryString(TEST_DB_ARTIFACTID));
		return config;
	}

	/**
	 * 传入项目路径及配置好的dbConfig，更新项目的测试数据库配置
	 * 
	 * @param projectPath
	 * @param dbConfig
	 * @throws KoalaException
	 */
	public static void updateTestDBConfig(String projectPath, DBConfigVO dbConfig) throws KoalaException {
		String pomXml = projectPath + "/pom.xml";
		if (new File(pomXml).exists() == false)
			throw new KoalaException("指定的项目路径错误");
		dbConfig.setXmlPath(pomXml);
		XmlWriter writer = XmlWriter.getPomXmlWriter(pomXml);
		writer.update(TEST_CONNECTION_URL, dbConfig.getConnectionURL());
		writer.update(TEST_DIALECT, dbConfig.getDialect());
		writer.update(TEST_HBM2DLL, dbConfig.getHbm2ddl());
		writer.update(TEST_JDBC_DRIVER, dbConfig.getJdbcDriver());
		writer.update(TEST_PASSWORD, dbConfig.getPassword());
		writer.update(TEST_SHOW_SQL, dbConfig.getShowSql());
		writer.update(TEST_USERNAME, dbConfig.getUsername());
		writer.update(TEST_DB_TYPE, dbConfig.getDbType());
		writer.update(TEST_DB_GROUPID, dbConfig.getDbGroupId());
		writer.update(TEST_DB_ARTIFACTID, dbConfig.getDbArtifactId());
		writer.saveUpdate();
	}

	public static void main(String args[]) throws KoalaException {
		String projectPath = "G:/project/foss-project/src/KoalaSecurity";
		System.out.println(DBConfigService.queryDBConfig(projectPath));
	}
}
