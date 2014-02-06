package org.dayatang.datasource4saas.springconf;

import org.dayatang.datasource4saas.DataSourceRegistry;
import org.dayatang.datasource4saas.SaasDataSource;
import org.dayatang.datasource4saas.TenantService;
import org.dayatang.datasource4saas.dscreator.C3P0DataSourceCreator;
import org.dayatang.datasource4saas.dsregistry.DataSourceCreator;
import org.dayatang.datasource4saas.dsregistry.JdbcDataSourceRegistry;
import org.dayatang.datasource4saas.tenantservice.ThreadLocalTenantService;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
public class SpringConfiguration {
	
	@Bean
	public DataSource dataSource() {
		return new SaasDataSource(tenantService(), dataSourceRegistry());
	}

	@Bean
	public TenantService tenantService() {
		return new ThreadLocalTenantService();
	}

	@Bean
	public DataSourceRegistry dataSourceRegistry() {
		return new JdbcDataSourceRegistry(dataSourceCreator());
	}

	@Bean
	public DataSourceCreator dataSourceCreator() {
		return new C3P0DataSourceCreator();
	}

}
