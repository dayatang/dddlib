package org.openkoala.application.util;

import java.io.InputStreamReader;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.dayatang.db.PropertiesUtil;
import com.dayatang.dbunit.DataSetStrategy;
import com.dayatang.dbunit.H2DataSourceDatabaseTester;
import com.dayatang.dbunit.MySQLDataSourceDatabaseTester;
import com.dayatang.dbunit.OracleDataSourceDatabaseTester;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author chencao
 * 
 */
public class Dbunit {

	private static final Logger logger = LoggerFactory.getLogger(Dbunit.class);

	protected IDatabaseTester databaseTester;

	public Dbunit() {
		// String jdbc_url = PropertiesUtil.JDBC_URL
		// + "&sessionVariables=FOREIGN_KEY_CHECKS=0";

		String jdbc_url = PropertiesUtil.JDBC_URL;
		// System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA,
		// "LPS");
		try {
			// databaseTester = new MySQLJdbcDatabaseTester(
			// PropertiesUtil.JDBC_DRIVER, jdbc_url,
			// PropertiesUtil.JDBC_USERNAME, PropertiesUtil.JDBC_PASSWD);
			ComboPooledDataSource ds = new ComboPooledDataSource();
			ds.setDriverClass(PropertiesUtil.JDBC_DRIVER);
			ds.setUser(PropertiesUtil.JDBC_USERNAME);
			ds.setPassword(PropertiesUtil.JDBC_PASSWD);
			ds.setJdbcUrl(jdbc_url);

			if (PropertiesUtil.JDBC_DRIVER.toLowerCase().contains("mysql")) {
				logger.info("use mysql datasource tester.");
				jdbc_url = PropertiesUtil.JDBC_URL
						+ "&sessionVariables=FOREIGN_KEY_CHECKS=0";
				ds.setJdbcUrl(jdbc_url);
				databaseTester = new MySQLDataSourceDatabaseTester(ds);
			} else if (PropertiesUtil.JDBC_DRIVER.toLowerCase().contains(
					"oracle")) {
				logger.info("use oracle datasource tester.");
				databaseTester = new OracleDataSourceDatabaseTester(ds,
						PropertiesUtil.JDBC_USERNAME);
			} else if (PropertiesUtil.JDBC_DRIVER.toLowerCase().contains("h2")) {
				logger.info("use h2 datasource tester.");
				databaseTester = new H2DataSourceDatabaseTester(ds);
			} else {
				//throw new RuntimeException("不支持的数据库类型！");
				databaseTester = new DataSourceDatabaseTester(ds);
			}

		} catch (Exception ex) {
			logger.error("构造测试环境失败！");
			ex.printStackTrace();
		}

	}

	@Before
	public void setUp() throws Exception {
		if (isDataSetOK()) {
			// initialize your dataset here

			String[] dataSetPaths = getDataSetFilePaths();

			IDataSet[] dataSet = new IDataSet[dataSetPaths.length];

			for (int i = 0; i < dataSetPaths.length; i++) {

				String path = dataSetPaths[i].startsWith("/") ? dataSetPaths[i]
						: "/" + dataSetPaths[i];
				// dataSet[i] = new FlatXmlDataSet(DbUnit.class
				// .getResourceAsStream(path));
				dataSet[i] = getDataSetObject(path);
				logger.debug("载入数据库资源文件：" + path);

			}

			CompositeDataSet compositeDateSet = new CompositeDataSet(
					(IDataSet[]) dataSet);

			databaseTester.setDataSet(compositeDateSet);
			// will call default setUpOperation
			databaseTester.setSetUpOperation(setUpOp());
			databaseTester.onSetup();
		} else {
			logger.warn("没有指定数据集！");
		}

	}

	private IDataSet getDataSetObject(String path) throws Exception {
		if (getDataSetStrategy().equals(DataSetStrategy.Xml)) {
			return new XmlDataSet(Dbunit.class.getResourceAsStream(path));
		} else if (getDataSetStrategy().equals(DataSetStrategy.FlatXml)) {
			boolean enableColumnSensing = true;
			InputStreamReader inReader = new InputStreamReader(Dbunit.class
					.getResourceAsStream(path), "UTF-8");
			return new CachedDataSet(new FlatXmlProducer(new InputSource(
					inReader), true, enableColumnSensing, false));
		} else {
			return new XmlDataSet(Dbunit.class.getResourceAsStream(path));
		}
	}

	@After
	public void tearDown() throws Exception {
		if (isDataSetOK()) {
			databaseTester.setTearDownOperation(tearDownOp());
			databaseTester.onTearDown();
		}
	}

	private boolean isDataSetOK() {
		if (getDataSetFilePaths().length > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 解析dbunit数据集的格式
	 * 
	 * @return 数据集格式
	 */
	protected DataSetStrategy getDataSetStrategy() {
		return DataSetStrategy.Xml;
	}

	/**
	 * 数据集路径数组
	 * 
	 * @return 数据集路径数组
	 */
	protected String[] getDataSetFilePaths() {
		return new String[] {};
	}

	/**
	 * 单元测试方法执行前，针对数据集的操作
	 * 
	 * @return 数据集在单元测试方法执行前的操作
	 */
	protected DatabaseOperation setUpOp() {
		return DatabaseOperation.REFRESH;
	}

	/**
	 * 单元测试方法执行后，针对数据集的操作
	 * 
	 * @return 数据集在单元测试方法执行后的操作
	 */
	protected DatabaseOperation tearDownOp() {
		return DatabaseOperation.NONE;
	}

}
