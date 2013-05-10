package com.dayatang.dsrouter.datasource;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.dsrouter.context.memory.ContextHolder;
import com.dayatang.springtest.PureSpringTestCase;

public class SimpleDataSourceRouterTest extends PureSpringTestCase {

	// private MultiTenantContext tenantContext;

	@Before
	public void setup() {
		super.setup();

		// tenantContext = createMock(MultiTenantContext.class);
	}

	@After
	public void teardown() {
		super.teardown();

		// tenantContext = null;
	}

	protected String[] springXmlPath() {
		return new String[] { "classpath:spring/simpleDataSourceContext.xml" };
	}

	@Test
	public void notNull() {
		// DataSource ds = (DataSource) context.getBean("dataSource");
		DataSource ds = InstanceFactory.getInstance(DataSource.class,
				"dataSource");
		assertNotNull(ds);
	}

	@Test
	public void getConnection() throws Exception {

		// expect(tenantContext.getUsername()).andReturn("root");
		// expect(tenantContext.getPassword()).andReturn("1234");
		// expect(tenantContext.getUrl()).andReturn("192.168.0.43");
		// expect(tenantContext.getSchema()).andReturn("test");
		// replay(tenantContext);

		ContextHolder.setContextType("1");
		printConn();

		ContextHolder.setContextType("2");
		printConn();

	}

	@Test
	public void getConnection_err() throws Exception {

		ContextHolder.setContextType("999");
		printConn();

	}

	private void printConn() throws SQLException {
		// DataSource ds = (DataSource) context.getBean("dataSource");
		DataSource ds = InstanceFactory.getInstance(DataSource.class,
				"dataSource");
		Connection connection = ds.getConnection();
		connection.setReadOnly(false);
		System.err.println(connection.getMetaData().getURL());
		System.err.println(connection.getMetaData().getURL());
		System.err.println(connection.getCatalog());

		ResultSet rs = connection.createStatement().executeQuery("select 1=1");
		rs.close();
		// rs.next();
		// System.err.println(rs.getString(1));
	}

}
