package com.dayatang.dbunit;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.ext.oracle.OracleDataTypeFactory;

public class OracleDataSourceDatabaseTester extends DataSourceDatabaseTester {

	public OracleDataSourceDatabaseTester(DataSource dataSource) {
		super(dataSource);
	}
	
	public OracleDataSourceDatabaseTester(DataSource dataSource, String schema) {
		super(dataSource, schema);
	}
	
	@Override
	public IDatabaseConnection getConnection() throws Exception {

		IDatabaseConnection conn = super.getConnection();
		
		DefaultDataTypeFactory datatypeFactory = new OracleDataTypeFactory();
		conn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
				datatypeFactory);
		return conn;
	}

}
