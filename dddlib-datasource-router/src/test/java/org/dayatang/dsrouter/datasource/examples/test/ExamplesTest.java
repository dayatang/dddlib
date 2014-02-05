package org.dayatang.dsrouter.datasource.examples.test;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.dsrouter.datasource.examples.CustomerContextHolder;
import org.dayatang.dsrouter.datasource.examples.CustomerType;
import org.dayatang.ioc.spring.factory.SpringInstanceProvider;
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
@ContextConfiguration("classpath:spring/examples/*.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ExamplesTest {

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
		DataSource ds = InstanceFactory.getInstance(DataSource.class);
		assertNotNull(ds);
	}

	@Test
	public void getConnection() throws Exception {
		CustomerContextHolder.setCustomerType(CustomerType.GOLD);
		printConn();

		CustomerContextHolder.setCustomerType(CustomerType.SILVER);
		printConn();

		CustomerContextHolder.clearCustomerType();
		printConn();
	}

	private void printConn() throws SQLException {
		// DataSource ds = (DataSource) context.getBean("dataSource");
		DataSource ds = InstanceFactory.getInstance(DataSource.class);
		Connection connection = ds.getConnection();
		connection.setReadOnly(true);
		System.err.println(connection.getMetaData().getURL());
		System.err.println(connection.getMetaData().getURL());
		System.err.println(connection.getCatalog());
	}

}
