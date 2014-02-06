package org.dayatang.dbunit;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.ext.oracle.OracleDataTypeFactory;

import javax.sql.DataSource;

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
