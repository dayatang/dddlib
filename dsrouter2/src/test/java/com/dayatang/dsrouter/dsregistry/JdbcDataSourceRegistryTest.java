package com.dayatang.dsrouter.dsregistry;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.dsrouter.dsregistry.JdbcDataSourceRegistry;

public class JdbcDataSourceRegistryTest {
	
	private JdbcDataSourceRegistry instance;
	private DataSourceCreator dataSourceCreator;
	private DataSource dataSource;
	private DataSource dataSource2;
	private String tenant = "abc";
	private String tenant2 = "xyz";

	@Before
	public void setUp() throws Exception {
		dataSourceCreator = mock(DataSourceCreator.class);
		instance = new JdbcDataSourceRegistry(dataSourceCreator);
		dataSource = mock(DataSource.class);
		dataSource2 = mock(DataSource.class);
		when(dataSourceCreator.createDataSourceForTenant(tenant)).thenReturn(dataSource);
		when(dataSourceCreator.createDataSourceForTenant(tenant2)).thenReturn(dataSource2);
		instance.releaseAllDataSources();
	}

	@After
	public void tearDown() throws Exception {
		instance.releaseAllDataSources();
	}

	@Test
	public void getDataSourceOfTenant() {
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		verify(dataSourceCreator).createDataSourceForTenant(tenant);
		assertSame(dataSource2, instance.getDataSourceOfTenant(tenant2));
		verify(dataSourceCreator).createDataSourceForTenant(tenant2);
		reset(dataSourceCreator);
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		verify(dataSourceCreator, never()).createDataSourceForTenant(tenant);
		assertSame(dataSource2, instance.getDataSourceOfTenant(tenant2));
		verify(dataSourceCreator, never()).createDataSourceForTenant(tenant2);
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		assertEquals(2, instance.size());
		instance.releaseAllDataSources();
		assertEquals(0, instance.size());
	}
	
	@Test
	public void registerDataSource() {
		assertFalse(instance.exists(tenant));
		instance.registerDataSource(tenant, dataSource);
		assertTrue(instance.exists(tenant));
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		verify(dataSourceCreator, never()).createDataSourceForTenant(tenant);
	}
	
	@Test
	public void releaseDataSourceOfTenant() {
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		assertTrue(instance.exists(tenant));
		instance.unregisterDataSource(tenant);
		assertFalse(instance.exists(tenant));
	}
	
	@Test
	public void releaseAllDataSources() {
		instance.registerDataSource("abc", mock(DataSource.class));
		instance.registerDataSource("xyz", mock(DataSource.class));
		assertEquals(2, instance.size());
		instance.releaseAllDataSources();
		assertTrue(instance.size() == 0);
	}
	
	@Test
	public void getLastAccessTimeOfTenant() throws InterruptedException {
		assertNull(instance.getLastAccessTimeOfTenant(tenant));
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		Date lastAccess = instance.getLastAccessTimeOfTenant(tenant);
		assertTrue(System.currentTimeMillis() - lastAccess.getTime() < 100);
		TimeUnit.SECONDS.sleep(2);
		assertTrue(System.currentTimeMillis() - lastAccess.getTime() > 1000);
	}
}
