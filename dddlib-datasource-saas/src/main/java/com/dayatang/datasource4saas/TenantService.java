package com.dayatang.datasource4saas;

/**
 *  租户服务
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface TenantService {

	/**
	 * 获得当前租户
	 * @return
	 */
	String getTenant();

}
