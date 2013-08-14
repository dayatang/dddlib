package com.dayatang.datasource4saas.dsregistry;

import static org.mockito.Mockito.*;

import javax.naming.Context;

import com.dayatang.datasource4saas.DataSourceRegistry;
import com.dayatang.datasource4saas.dsregistry.JndiMappingDataSourceRegistry;

public class JndiMappingDataSourceRegistryTest extends AbstractDataSourceRegistryTest {
	
	private String jndiPrefix = "jdbc/iexam/";

	@Override
	protected DataSourceRegistry getInstanceToBeTested() throws Exception {
		Context context = mock(Context.class);
		JndiMappingDataSourceRegistry result = new JndiMappingDataSourceRegistry();
		result.setContext(context);
		result.setJndiPrefix(jndiPrefix);
		when(context.lookup(jndiPrefix + tenant)).thenReturn(dataSource);
		when(context.lookup(jndiPrefix + tenant2)).thenReturn(dataSource2);
		return result;
	}
}
