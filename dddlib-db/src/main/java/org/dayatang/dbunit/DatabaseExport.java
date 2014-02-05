package org.dayatang.dbunit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dayatang.db.PropertiesUtil;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.util.search.SearchException;

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

}
