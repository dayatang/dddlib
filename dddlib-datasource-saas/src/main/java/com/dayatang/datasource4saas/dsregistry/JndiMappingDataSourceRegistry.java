package com.dayatang.datasource4saas.dsregistry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.utils.Slf4jLogger;

/**
 * 数据源注册表实现，将租户数据源映射到JNDI
 * 
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 * 
 */
public class JndiMappingDataSourceRegistry extends MapBasedDataSourceRegistry {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(JndiMappingDataSourceRegistry.class);
	private Context context;
	private String jndiPrefix;
	private String jndiSuffix;

	private Context getContext() {
		if (context == null) {
			try {
				context = new InitialContext();
			} catch (NamingException e) {
				throw new DataSourceRegistryException("Cannot initiate JNDI environment!", e);
			}
		}
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setJndiPrefix(String jndiPrefix) {
		this.jndiPrefix = jndiPrefix;
	}

	public void setJndiSuffix(String jndiSuffix) {
		this.jndiSuffix = jndiSuffix;
	}

	/**
	 * 根据租户名称，拼装上相应的前缀和后缀作为数据源JNDI名称，从应用服务器上获取已部署的数据源。
	 */
	@Override
	public DataSource findOrCreateDataSourceForTenant(String tenant) {
		DataSource result = null;
		String dataSourceJndi = StringUtils.defaultString(jndiPrefix) + tenant + StringUtils.defaultString(jndiSuffix);
		try {
			result = (DataSource) getContext().lookup(dataSourceJndi);
		} catch (NamingException e) {
			String message = "Lookup jndi: " + dataSourceJndi + " failed!";
			LOGGER.error(message, e);
			throw new DataSourceRegistryException(message, e);
		}
		if (result == null) {
			throw new DataSourceRegistryException("There's no data source prepared for tenant " + tenant);
		}
		LOGGER.info("Found JNDI " + dataSourceJndi + " for tenant {}", tenant);
		return result;
	}
}
