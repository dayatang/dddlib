package org.dayatang.dsrouter.datasource;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Inject;

import javax.sql.DataSource;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.dsrouter.context.memory.ContextHolder;
import org.dayatang.spring.factory.SpringInstanceProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/simpleDataSourceContext.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class SimpleDataSourceRouterTest {

    @Inject
    private ApplicationContext ctx;

    @Before
    public void beforeTest() {
        InstanceFactory.setInstanceProvider(new SpringInstanceProvider(ctx));
    }

    @After
    public void afterTest() {
        InstanceFactory.setInstanceProvider(null);
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
