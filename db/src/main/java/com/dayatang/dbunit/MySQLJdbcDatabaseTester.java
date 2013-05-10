package com.dayatang.dbunit;

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;

public class MySQLJdbcDatabaseTester extends JdbcDatabaseTester {

	public MySQLJdbcDatabaseTester(String driverClass, String connectionUrl)
			throws ClassNotFoundException {
		super(driverClass, connectionUrl);
	}

	public MySQLJdbcDatabaseTester(String driverClass, String connectionUrl,
			String username, String password) throws ClassNotFoundException {
		super(driverClass, connectionUrl, username, password, null);
	}

	public MySQLJdbcDatabaseTester(String driverClass, String connectionUrl,
			String username, String password, String schema)
			throws ClassNotFoundException {
		super(driverClass, connectionUrl, username, password, schema);
	}

	@Override
	public IDatabaseConnection getConnection() throws Exception {

		IDatabaseConnection conn = super.getConnection();

		DefaultDataTypeFactory datatypeFactory = new MySqlDataTypeFactory();
		conn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
				datatypeFactory);

		return conn;
	}

}
