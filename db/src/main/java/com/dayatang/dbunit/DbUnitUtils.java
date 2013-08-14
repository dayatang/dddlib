package com.dayatang.dbunit;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.stream.IDataSetProducer;
import org.dbunit.dataset.stream.StreamingDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.xml.sax.InputSource;

import com.dayatang.JdbcConstants;
import com.dayatang.utils.PropertiesReader;
import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * DBUnit实用工具类。用于的在数据库和XML数据文件之间的相互输入输出。
 * XML数据文件只支持FlatXml格式，则数据库中的每行数据表示为一个XML元素，
 * 每个列成为该元素的一个属性。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class DbUnitUtils {
	
	private DataSource dataSource;
	
	/**
	 * 从类路径属性文件中读入JDBC连接信息
	 * @param resourceFile
	 * @return
	 */
	public static DbUnitUtils configFromClasspath(String resourceFile) {
		Properties jdbcProperties = PropertiesReader.readPropertiesFromClasspath(resourceFile);
		return new DbUnitUtils(createDataSource(jdbcProperties));
	}

	/**
	 * 从磁盘文件中读入JDBC连接信息
	 * @param configFile
	 * @return
	 */
	public static DbUnitUtils configFromFile(String configFile) {
		Properties jdbcProperties = PropertiesReader.readPropertiesFromFile(configFile);
		return new DbUnitUtils(createDataSource(jdbcProperties));
	}

	public DbUnitUtils(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 从XML数据文件中读入数据集，写入到数据库。数据库表中的原有数据将被清除。
	 * @param flatXmlDataFile XML数据文件
	 */
	public void importDataFromClasspath(final String flatXmlDataFile) {
		new DbUnitTemplate(dataSource).execute(new DbUnitCallback() {
			@Override
			public void doInDbUnit(IDatabaseConnection connection) throws Exception {
				DatabaseOperation.CLEAN_INSERT.execute(connection, getDatasetFromFile(flatXmlDataFile));
			}
		});
	}

	/**
	 * 将数据库中的数据导出到XML文件中。
	 * @param dir 文件存放目录
	 * @param fileName 生成的XML数据文件名。
	 */
	public void exportData(final String dir, final String fileName) {
		new DbUnitTemplate(dataSource).execute(new DbUnitCallback() {
			@Override
			public void doInDbUnit(IDatabaseConnection connection) throws Exception {
				IDataSet dataSet = new FilteredDataSet(new DatabaseSequenceFilter(connection), 
						connection.createDataSet());
				File parent = new File(dir);
				if (!parent.exists()) {
					parent.mkdirs();
				}
				OutputStream out = new FileOutputStream(new File(dir, fileName));
				FlatXmlDataSet.write(dataSet, out);
			}
		});
	}

	/**
	 * 读取数据库表结构，生成XML数据文件的DTD文件
	 * @param dir 文件存放目录
	 * @param fileName 生成的XML数据文件名。
	 */
	public void exportDtd(final String dir, final String fileName) {
		new DbUnitTemplate(dataSource).execute(new DbUnitCallback() {
			@Override
			public void doInDbUnit(IDatabaseConnection connection) throws Exception {
				IDataSet dataSet = new FilteredDataSet(new DatabaseSequenceFilter(connection), 
						connection.createDataSet());
				File parent = new File(dir);
				if (!parent.exists()) {
					parent.mkdirs();
				}
		        Writer out = new OutputStreamWriter(new FileOutputStream(new File(dir, fileName)));
		        FlatDtdDataSet.write(dataSet, out);
			}
		});
	}
	
	/**
	 * 从XML数据文件中读入数据集，写入到数据库。数据库表中的原有数据将被清除。
	 * @param flatXmlDataFile XML数据文件
	 */
	public void refreshData(final String flatXmlDataFile) {
		new DbUnitTemplate(dataSource).execute(new DbUnitCallback() {
			@Override
			public void doInDbUnit(IDatabaseConnection connection) throws Exception {
				InputStream in = getClass().getResourceAsStream(flatXmlDataFile);
				IDataSetProducer producer = new FlatXmlProducer(new InputSource(in), false);
				IDataSet dataSet = new StreamingDataSet(producer);
				DatabaseOperation.REFRESH.execute(connection, dataSet);
			}
		});
	}
	
	private IDataSet getDatasetFromFile(String flatXmlDataFile) {
		return getDatasetFromInputStream(getClass().getResourceAsStream(flatXmlDataFile));
	}

	private IDataSet getDatasetFromInputStream(InputStream in) {
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(in), false);
		try {
			return new CachedDataSet(producer);
		} catch (DataSetException e) {
			throw new RuntimeException("Cannot get dataset.", e);
		}
	}

	private static DataSource createDataSource(Properties jdbcProperties) {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(jdbcProperties.getProperty(JdbcConstants.JDBC_DRIVER));
		} catch (PropertyVetoException e) {
			throw new RuntimeException("Cannot create C3P0 data source", e);
		}
		dataSource.setJdbcUrl(jdbcProperties.getProperty(JdbcConstants.JDBC_URL));
		dataSource.setUser(jdbcProperties.getProperty(JdbcConstants.JDBC_USERNAME));
		dataSource.setPassword(jdbcProperties.getProperty(JdbcConstants.JDBC_PASSWORD));
		return dataSource;
	}
}
