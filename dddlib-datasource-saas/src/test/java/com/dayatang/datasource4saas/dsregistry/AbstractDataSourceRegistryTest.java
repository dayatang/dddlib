package com.dayatang.datasource4saas.dsregistry;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.datasource4saas.DataSourceRegistry;

public abstract class AbstractDataSourceRegistryTest {
	
	private DataSourceRegistry instance;
	protected DataSource dataSource = mock(DataSource.class);
	protected DataSource dataSource2 = mock(DataSource.class);
	protected String tenant = "abc";
	protected String tenant2 = "xyz";

	@Before
	public void setUp() throws Exception {
		instance = getInstanceToBeTested();
	}

	protected abstract DataSourceRegistry getInstanceToBeTested()  throws Exception;

	@After
	public void tearDown() throws Exception {
		instance.releaseAllDataSources();
	}

	@Test
	public void getDataSourceOfTenant() {
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		assertSame(dataSource2, instance.getDataSourceOfTenant(tenant2));
	}
	
	@Test
	public void registerDataSourceForTenant() {
		assertFalse(instance.existsDataSourceOfTenant(tenant));
		instance.registerDataSourceForTenant(tenant, dataSource);
		assertTrue(instance.existsDataSourceOfTenant(tenant));
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
	}
	
	@Test
	public void registerDataSources() {
		assertFalse(instance.existsDataSourceOfTenant(tenant));
		assertFalse(instance.existsDataSourceOfTenant(tenant2));
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		dataSources.put(tenant, dataSource);
		dataSources.put(tenant2, dataSource2);
		instance.registerDataSources(dataSources);
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		assertSame(dataSource2, instance.getDataSourceOfTenant(tenant2));
	}
	
	@Test
	public void releaseDataSourceOfTenant() {
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		assertTrue(instance.existsDataSourceOfTenant(tenant));
		instance.unregisterDataSourceOfTenant(tenant);
		assertFalse(instance.existsDataSourceOfTenant(tenant));
	}
	
	@Test
	public void releaseAllDataSources() {
		instance.registerDataSourceForTenant("abc", mock(DataSource.class));
		instance.registerDataSourceForTenant("xyz", mock(DataSource.class));
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
