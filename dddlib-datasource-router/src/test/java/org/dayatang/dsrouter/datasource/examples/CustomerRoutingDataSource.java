package org.dayatang.dsrouter.datasource.examples;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomerRoutingDataSource extends AbstractRoutingDataSource {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomerRoutingDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		return CustomerContextHolder.getCustomerType();
	}

	public Connection getConnection() throws SQLException {
		Connection conn = determineTargetDataSource().getConnection();
		if (logger.isDebugEnabled()) {
			System.err.println(conn.getClass());
			
			//当使用c3p0的时候 不能在此处调用conn.getMetaData()方法 否则读写分离失效
		}
		return conn;
	}

}
