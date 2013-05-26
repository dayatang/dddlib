package com.dayatang.dsrouter.springconf;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;

import com.dayatang.dsrouter.DataSourceRegistry;
import com.dayatang.dsrouter.SaasDataSource;
import com.dayatang.dsrouter.TenantService;
import com.dayatang.dsrouter.dscreator.C3P0DataSourceCreator;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.dsrouter.dsregistry.JdbcDataSourceRegistry;
import com.dayatang.dsrouter.tenantservice.ThreadLocalTenantService;

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
