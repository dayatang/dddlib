package com.dayatang.dbunit;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;

public class MySQLDataSourceDatabaseTester extends DataSourceDatabaseTester {

	public MySQLDataSourceDatabaseTester(DataSource dataSource) {
		super(dataSource);
	}
	
	public MySQLDataSourceDatabaseTester(DataSource dataSource, String schema) {
		super(dataSource, schema);
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
