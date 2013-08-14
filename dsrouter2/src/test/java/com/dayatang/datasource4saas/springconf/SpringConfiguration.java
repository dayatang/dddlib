package com.dayatang.datasource4saas.springconf;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;

import com.dayatang.datasource4saas.DataSourceRegistry;
import com.dayatang.datasource4saas.SaasDataSource;
import com.dayatang.datasource4saas.TenantService;
import com.dayatang.datasource4saas.dscreator.C3P0DataSourceCreator;
import com.dayatang.datasource4saas.dsregistry.DataSourceCreator;
import com.dayatang.datasource4saas.dsregistry.JdbcDataSourceRegistry;
import com.dayatang.datasource4saas.tenantservice.ThreadLocalTenantService;

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
