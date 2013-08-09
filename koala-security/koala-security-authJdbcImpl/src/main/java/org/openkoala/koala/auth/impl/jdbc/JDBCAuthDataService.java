package org.openkoala.koala.auth.impl.jdbc;

import java.util.List;
import java.util.Map;

import org.openkoala.koala.auth.AuthDataService;
import org.openkoala.koala.auth.UserDetails;

/**
 * JDBC获取授权信息
 * @author zyb
 * 2013-7-2 下午2:49:21
 *
 */
public class JDBCAuthDataService implements AuthDataService {

	private JdbcSecurityConfig config;
	
	private SecurityManager securityManager;
	
	public List<String> getAttributes(String res) {
		return securityManager.getAllReourceAndRoles().get(res);
	}

	public UserDetails loadUserByUseraccount(String userAccount) {
		return securityManager.getUser(userAccount);
	}

	public List<String> getUserRoles(String userAccount) {
		return securityManager.getUserRoles(userAccount);
	}

	public Map<String, List<String>> getAllReourceAndRoles() {
		return securityManager.getAllReourceAndRoles();
	}

	public void setConfig(JdbcSecurityConfig config) {
		this.config = config;
	}
	
	/**
	 * 初始化SecurityManagerImpl
	 */
	public void initSecurityManager() {
		securityManager = new SecurityManagerImpl(config);
	}
	
}