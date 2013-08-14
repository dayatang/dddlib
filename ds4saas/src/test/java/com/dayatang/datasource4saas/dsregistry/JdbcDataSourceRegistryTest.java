package com.dayatang.datasource4saas.dsregistry;

import static org.mockito.Mockito.*;

import com.dayatang.datasource4saas.DataSourceRegistry;
import com.dayatang.datasource4saas.dsregistry.DataSourceCreator;
import com.dayatang.datasource4saas.dsregistry.JdbcDataSourceRegistry;

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
