package com.dayatang.dsrouter.dsregistry;

import static org.mockito.Mockito.*;

import com.dayatang.dsrouter.DataSourceRegistry;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.dsrouter.dsregistry.JdbcDataSourceRegistry;

public class JdbcDataSourceRegistryTest extends AbstractDataSourceRegistryTest {
	
	private DataSourceCreator dataSourceCreator;

	@Override
	protected DataSourceRegistry getInstanceToBeTested() {
		dataSourceCreator = mock(DataSourceCreator.class);
		JdbcDataSourceRegistry instance = new JdbcDataSourceRegistry(dataSourceCreator);
		when(dataSourceCreator.createDataSourceForTenant(tenant)).thenReturn(dataSource);
		when(dataSourceCreator.createDataSourceForTenant(tenant2)).thenReturn(dataSource2);
		return instance;
	}
}
