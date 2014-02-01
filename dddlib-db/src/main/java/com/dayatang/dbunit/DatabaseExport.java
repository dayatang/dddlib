package com.dayatang.dbunit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.util.search.SearchException;

import com.dayatang.db.PropertiesUtil;

/**
 * 功能描述:使用dbunit作单元测试,用于生成dataSet文件
 * 
 * @author <a href="malto:chencao0524@gmail.com">陈操</a> Created on 2007-9-29
 * 
 * @version $LastChangedRevision$ $LastChangedBy$ $LastChangedDate$
 * 
 */
public class DatabaseExport {

	private DatabaseExport() {
		super();
	}

	public static IDatabaseConnection createConnection() throws Exception {
		// database connection
		Class.forName(PropertiesUtil.JDBC_DRIVER);
		Connection jdbcConnection = DriverManager.getConnection(
				PropertiesUtil.JDBC_URL, PropertiesUtil.JDBC_USERNAME,
				PropertiesUtil.JDBC_PASSWD);
		return new DatabaseConnection(jdbcConnection);
	}

	public static void exportDataSet(IDataSet dataSet, String fileName)
			throws DataSetException, FileNotFoundException, IOException {
		FlatXmlDataSet.write(dataSet, new FileOutputStream(fileName));
	}

	@Deprecated
	public static void exportPartialSet(IDatabaseConnection connection)
			throws DataSetException, FileNotFoundException, IOException,
			SQLException {
		// partial database export
		QueryDataSet partialDataSet = new QueryDataSet(connection);
		partialDataSet.addTable("hello", "SELECT * FROM hello");
		// partialDataSet.addTable("author");
		FlatXmlDataSet.write(partialDataSet,
				new FileOutputStream("partial.xml"));

	}

	@Deprecated
	public static void exportFullDataSet(IDatabaseConnection connection)
			throws SQLException, DataSetException, FileNotFoundException,
			IOException {
		// full database export
		IDataSet fullDataSet = connection.createDataSet();
		FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));
	}

	@Deprecated
	public static void exportAllDependentTables(IDatabaseConnection connection)
			throws SearchException, DataSetException, FileNotFoundException,
			IOException, SQLException {
		// dependent tables database export: export table X and all tables that
		// have a PK which is a FK on X, in the right order for insertion
		String[] depTableNames = TablesDependencyHelper.getAllDependentTables(
				connection, "X");
		IDataSet depDataSet = connection.createDataSet(depTableNames);
		FlatXmlDataSet
				.write(depDataSet, new FileOutputStream("dependents.xml"));

	}
}
