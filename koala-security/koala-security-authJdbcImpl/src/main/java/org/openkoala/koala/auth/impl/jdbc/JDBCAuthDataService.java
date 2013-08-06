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
	
	public List<String> getAttributes(String res) {
		return getSecurityManager().getAllReourceAndRoles().get(res);
	}

	public UserDetails loadUserByUseraccount(String userAccount) {
		return getSecurityManager().getUser(userAccount);
	}

	public List<String> getUserRoles(String userAccount) {
		return getSecurityManager().getUserRoles(userAccount);
	}

	public Map<String, List<String>> getAllReourceAndRoles() {
		return getSecurityManager().getAllReourceAndRoles();
	}

	public void setConfig(JdbcSecurityConfig config) {
		this.config = config;
	}
	
	private SecurityManager getSecurityManager() {
		return SecurityManagerFactory.getSecurityManager(config);
	}

}