package org.dayatang.datasource4saas.dsregistry;

import org.dayatang.datasource4saas.DataSourceRegistry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
