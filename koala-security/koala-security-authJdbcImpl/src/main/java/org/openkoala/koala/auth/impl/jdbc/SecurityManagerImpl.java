package org.openkoala.koala.auth.impl.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
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
	
	private static final Logger LOGGER = Logger.getLogger("SecurityManagerImpl");
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	private Connection getConnection() {
		try {
			Class.forName(config.getDbdriver());
			return DriverManager.getConnection(config.getDburl(), config.getDbuser(), config.getDbpassword());
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			pstmt = conn.prepareStatement(config.getQueryUser());
			pstmt.setString(1, userAccount);
			pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			
			rs = pstmt.executeQuery();
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
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement(pstmt);
			closeConnection(conn);
		}
		return null;
	}

	public Map<String, List<String>> getAllReourceAndRoles() {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(config.getQueryResAuth());
			pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			
			rs = pstmt.executeQuery();
			
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
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement(pstmt);
			closeConnection(conn);
		}
		return null;
	}


	public List<String> getUserRoles(String userAccount) {
		Connection conn = getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if (userAccount.equals(config.getAdminAccount())) {
			try {
				pstmt = conn.prepareStatement(config.getQueryAllAuth());
				pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
				rs = pstmt.executeQuery();
				
				List<String> results = new ArrayList<String>();
				
				while (rs.next()) {
					results.add(rs.getString("ROLE_NAME"));
				}
				
				return results;
			} catch (SQLException e) {
				LOGGER.info(e.getMessage());
			} finally {
				closeResultSet(rs);
				closeStatement(pstmt);
				closeConnection(conn);
			}
		}
		
		try {
			pstmt = conn.prepareStatement(config.getQueryUserAuth());
			pstmt.setString(1, userAccount);
			pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			rs = pstmt.executeQuery();
			
			List<String> results = new ArrayList<String>();
			
			while (rs.next()) {
				results.add(rs.getString("ROLE_NAME"));
			}
			
			return results;
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement(pstmt);
			closeConnection(conn);
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
	
	private void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOGGER.info(e.getMessage());
			}
		}
	}

	private void closeStatement(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				LOGGER.info(e.getMessage());
			}
		}
	}

	private void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LOGGER.info(e.getMessage());
			}
		}
	}
	
}
