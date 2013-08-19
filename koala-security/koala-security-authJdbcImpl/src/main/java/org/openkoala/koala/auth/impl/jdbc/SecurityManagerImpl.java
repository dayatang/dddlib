package org.openkoala.koala.auth.impl.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.openkoala.koala.auth.UserDetails;
import org.openkoala.koala.auth.vo.JdbcCustomUserDetails;

/**
 * 获取鉴权信息实现类
 * @author zyb
 * 2013-7-2 下午12:18:27
 *
 */
public class SecurityManagerImpl implements SecurityManager {
	
	private JdbcSecurityConfig config;
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	private Connection getConnection() {
		DbUtils.loadDriver(config.getDbdriver());
		try {
			return DriverManager.getConnection(config.getDburl(), config.getDbuser(), config.getDbpassword());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserDetails getUser(String userAccount) {
		if (isSuper(userAccount)) {
			PasswordEncoder encoder = new PasswordEncoder(config.getAdminAccount(), "MD5");
			return getUserDetails(config.getAdminAccount(), //
					encoder.encode(config.getAdminPass()), //
					config.getAdminRealName(), //
					"administrator@openkoala.com", config.getAdminRealName(), //
					getUserRoles(config.getAdminAccount()), true, true);
		}
		
		Connection conn = getConnection();
		
		try {
			return getQueryRunner().query(conn, config.getQueryUser(), new ResultSetHandler<UserDetails>() {

				public UserDetails handle(ResultSet rs) throws SQLException {
					if (rs.next()) {
						return getUserDetails( //
								rs.getString("USERACCOUNT"), //
								rs.getString("PASSWORD"), //
								rs.getString("DESCRIPTION"), //
								rs.getString("EMAIL"), 
								rs.getString("REAL_NAME"), //
								getUserRoles(rs.getString("USERACCOUNT")), //
								false, //
								rs.getBoolean("isvalid")
						);
					}
					return null;
				}
				
			}, userAccount, new Timestamp(System.currentTimeMillis()));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return null;
	}

	public Map<String, List<String>> getAllReourceAndRoles() {
		Connection conn = getConnection();
		try {
			return getQueryRunner().query(conn, config.getQueryResAuth(), new ResultSetHandler<Map<String, List<String>>>() {
				public Map<String, List<String>> handle(ResultSet rs) throws SQLException {
					Map<String, List<String>> result = new HashMap<String, List<String>>();
					while (rs.next()) {
						Set<String> roles = new HashSet<String>();
						roles.add(rs.getString("role_name"));
						if (result.containsKey(rs.getString("identifier"))) {
							result.get(rs.getString("identifier")).addAll(roles);
						} else {
							result.put(rs.getString("identifier"), new ArrayList<String>(roles));
						}
					}
					return result;
				}
			}, new Timestamp(System.currentTimeMillis()));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return null;
	}


	public List<String> getUserRoles(String userAccount) {
		Connection conn = getConnection();
		
		if (userAccount.equals(config.getAdminAccount())) {
			try {
				return getQueryRunner().query(conn, config.getQueryAllAuth(), new ResultSetHandler<List<String>>() {
					@SuppressWarnings({ "serial", "unchecked" })
					public List<String> handle(final ResultSet rs) throws SQLException {
						while (rs.next()) {
							return new ArrayList<String>() {
								{
									add(rs.getString("ROLE_NAME"));
								}
							};
						}
						return Collections.EMPTY_LIST;
					}
				}, new Timestamp(System.currentTimeMillis()));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DbUtils.closeQuietly(conn);
			}
		}
		
		try {
			return getQueryRunner().query(conn, config.getQueryUserAuth(), new ResultSetHandler<List<String>>() {
				@SuppressWarnings({ "serial", "unchecked" })
				public List<String> handle(final ResultSet rs) throws SQLException {
					while (rs.next()) {
						return new ArrayList<String>() {
							{
								add(rs.getString(1));
							}
						};
					}
					return Collections.EMPTY_LIST;
				}
			}, userAccount, new Timestamp(System.currentTimeMillis()));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return null;
	}
	
	private UserDetails getUserDetails(String userAccount, String password, String desc, String email, String realName,
			List<String> authorities, boolean isSuper, boolean isEnabled) {
		JdbcCustomUserDetails userDetails = new JdbcCustomUserDetails();
		userDetails.setUseraccount(userAccount);
		userDetails.setPassword(password);
		userDetails.setDescription(desc);
		userDetails.setEmail(email);
		userDetails.setRealName(realName);
		userDetails.setAuthorities(authorities);
		userDetails.setSuper(isSuper);
		userDetails.setEnabled(isEnabled);
		return userDetails;
	}

	private QueryRunner getQueryRunner() {
		return new QueryRunner();
	}
	
	/**
	 * 是否超级用户
	 * @param userAccount
	 * @return
	 */
	private boolean isSuper(String userAccount) {
		return config.getUseAdmain().equals("true") && config.getAdminAccount().equals(userAccount);
	}

	public void setConfig(JdbcSecurityConfig config) {
		this.config = config;
	}
	
}
