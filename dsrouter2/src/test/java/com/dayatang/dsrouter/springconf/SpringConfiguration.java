package com.dayatang.dsrouter.springconf;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;

import com.dayatang.dsrouter.DataSourceRegistry;
import com.dayatang.dsrouter.SaasDataSource;
import com.dayatang.dsrouter.TenantService;
import com.dayatang.dsrouter.dscreator.C3P0DataSourceCreator;
import com.dayatang.dsrouter.dscreator.JdbcUrlTranslator;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.dsrouter.dsregistry.JdbcDataSourceRegistry;
import com.dayatang.dsrouter.mappingstrategy.DbNameBasedMappingStrategy;
import com.dayatang.dsrouter.tenantservice.ThreadLocalTenantService;
import com.dayatang.dsrouter.urltranslator.DbMappingStrategy;
import com.dayatang.dsrouter.urltranslator.MySqlUrlTranslator;
import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;

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
		return new C3P0DataSourceCreator(urlTranslator(), dsConfiguration());
	}

	@Bean
	public JdbcUrlTranslator urlTranslator() {
		return new MySqlUrlTranslator(mappingStrategy());
	}

	@Bean
	public DbMappingStrategy mappingStrategy() {
		return new DbNameBasedMappingStrategy(mappingConfiguration());
	}

	@Bean
	public ConfigurationFactory configurationFactory() {
		return new ConfigurationFactory();
	}
	
	@Bean
	public com.dayatang.configuration.Configuration mappingConfiguration() {
		return configurationFactory().fromUrl("http://www.dayatang.com/tenant-db-mapping.properties");
	}

	@Bean
	public Configuration dsConfiguration() {
		return configurationFactory().fromClasspath("/ds-config.properties");
	}

}
