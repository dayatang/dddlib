package com.dayatang.dsrouter;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SaasDataSourceTest {
	
	private SaasDataSource instance;
	private TenantService tenantService; 
	private DataSourceRegistry dataSourceRegistry;
	private DataSource actualDataSource;

	@Before
	public void setUp() throws Exception {
		tenantService = mock(TenantService.class);
		dataSourceRegistry = mock(DataSourceRegistry.class);
		instance = new SaasDataSource(tenantService, dataSourceRegistry);
		actualDataSource = mock(DataSource.class);
		when(tenantService.getTenant()).thenReturn("abc");
		when(dataSourceRegistry.getDataSourceOfTenant("abc")).thenReturn(actualDataSource);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLogWriter() throws SQLException {
		PrintWriter printWriter = mock(PrintWriter.class);
		when(actualDataSource.getLogWriter()).thenReturn(printWriter);
		assertSame(printWriter, instance.getLogWriter());
	}

	@Test
	public void testGetLoginTimeout() throws SQLException {
		int timeout = 1000;
		when(actualDataSource.getLoginTimeout()).thenReturn(timeout);
		assertEquals(timeout, actualDataSource.getLoginTimeout());
	}

	@Test
	public void testSetLogWriter() throws SQLException {
		PrintWriter printWriter = mock(PrintWriter.class);
		instance.setLogWriter(printWriter);
		verify(actualDataSource).setLogWriter(printWriter);
	}

	@Test
	public void testSetLoginTimeout() throws SQLException {
		int seconds = 100;
		instance.setLoginTimeout(seconds);
		verify(actualDataSource).setLoginTimeout(seconds);
	}

	@Test
	public void testIsWrapperFor() throws SQLException {
		Class<String> stringClass = String.class;
		Class<Long> longClass = Long.class;
		when(actualDataSource.isWrapperFor(stringClass)).thenReturn(true);
		assertTrue(instance.isWrapperFor(stringClass));
		when(actualDataSource.isWrapperFor(longClass)).thenReturn(false);
		assertFalse(instance.isWrapperFor(longClass));
	}

	@Test
	public void testUnwrap() throws SQLException {
		String result = "abcddddd";
		when(actualDataSource.unwrap(String.class)).thenReturn(result);
		assertSame(result, instance.unwrap(String.class));
	}

	@Test
	public void testGetParentLogger() throws SQLException {
		Double jdkVersion = Double.valueOf(System.getProperty("java.specification.version"));
		if (jdkVersion > 1.6) {
			System.out.println("1.7");
			Logger logger = mock(Logger.class);
			when(actualDataSource.getParentLogger()).thenReturn(logger);
			assertSame(logger, instance.getParentLogger());
		} else {
			assertNull(instance.getParentLogger());
		}
	}

	@Test
	public void testGetConnection() throws SQLException {
		Connection result = mock(Connection.class);
		when(actualDataSource.getConnection()).thenReturn(result);
		assertSame(result, instance.getConnection());
	}

	@Test
	public void testGetConnectionStringString() throws SQLException {
		String username = "zhangsan";
		String password = "pwd";
		Connection result = mock(Connection.class);
		when(actualDataSource.getConnection(username, password)).thenReturn(result);
		assertSame(result, instance.getConnection(username, password));
	}

}