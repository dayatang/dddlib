package org.openkoala.koala.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.dbunit.IDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;

public abstract class KoalaJdbcPropertiesDBUnitTestCase extends
		KoalaDBUnitTestCase {

	private Properties props;

	@Override
	public abstract String getDataXmlFile();

	@Override
	public IDatabaseTester getIDatabaseTester() {
		props = loadProperties();
		initJdbcPropertieEnv();
		try {
			return new PropertiesBasedJdbcDatabaseTester();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void initJdbcPropertieEnv() {
		String[] properties = new String[]{PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
				PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
				PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
				PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
				PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA};
		for(String property:properties){
			 System.setProperty(property,props.getProperty(property));
		}
	}

	private Properties loadProperties() {
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream(getPropertiesFile());
		try {
			props.load(input);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("加载配置文件失败："+getPropertiesFile(),e);
		}
		return props;
	}

	/**
	 * 返回Properties的位置
	 * 
	 * @return
	 */
	public abstract String getPropertiesFile();
}
