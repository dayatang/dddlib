package org.dayatang.dbunit;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;

import javax.sql.DataSource;

public class H2DataSourceDatabaseTester extends DataSourceDatabaseTester {

	public H2DataSourceDatabaseTester(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public IDatabaseConnection getConnection() throws Exception {

		IDatabaseConnection conn = super.getConnection();

		DefaultDataTypeFactory datatypeFactory = new HsqldbDataTypeFactory();
		conn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
				datatypeFactory);
		return conn;
	}

}
