package com.dayatang.dsrouter.dsregistry;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.naming.Context;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JndiMappingDataSourceRegistryTest {
	
	private JndiMappingDataSourceRegistry instance;
	private Context context;
	private DataSource dataSource;
	private DataSource dataSource2;
	private String tenant = "abc";
	private String tenant2 = "xyz";
	private String jndiPrefix = "jdbc/iexam/";

	@Before
	public void setUp() throws Exception {
		context = mock(Context.class);
		instance = new JndiMappingDataSourceRegistry();
		instance.setContext(context);
		instance.setJndiPrefix(jndiPrefix);
		dataSource = mock(DataSource.class);
		dataSource2 = mock(DataSource.class);
		when(context.lookup(jndiPrefix + tenant)).thenReturn(dataSource);
		when(context.lookup(jndiPrefix + tenant2)).thenReturn(dataSource2);
		instance.releaseAllDataSources();
	}

	@After
	public void tearDown() throws Exception {
		instance.releaseAllDataSources();
	}

	@Test
	public void getDataSourceOfTenant() throws Exception {
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		verify(context).lookup(jndiPrefix + tenant);
		assertSame(dataSource2, instance.getDataSourceOfTenant(tenant2));
		verify(context).lookup(jndiPrefix + tenant2);
		reset(context);
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		verify(context, never()).lookup(jndiPrefix + tenant);
		assertSame(dataSource2, instance.getDataSourceOfTenant(tenant2));
		verify(context, never()).lookup(jndiPrefix + tenant2);
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		assertEquals(2, instance.size());
		instance.releaseAllDataSources();
		assertEquals(0, instance.size());
	}
	
	@Test
	public void registerDataSource() throws Exception {
		assertFalse(instance.exists(tenant));
		instance.registerDataSource(tenant, dataSource);
		assertTrue(instance.exists(tenant));
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		verify(context, never()).lookup(jndiPrefix + tenant);
	}
	
	@Test
	public void releaseDataSourceOfTenant() {
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		assertTrue(instance.exists(tenant));
		instance.releaseDataSourceOfTenant(tenant);
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
