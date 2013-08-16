package org.openkoala.koala.auth.impl.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcManager {
	private JdbcSecurityConfig secConfig;
	private String url;
	private Connection con;
	PreparedStatement stmt;

	public JdbcManager(JdbcSecurityConfig secConfig) {
		this.secConfig = secConfig;
		this.url = secConfig.getDburl();
		try {
			Class.forName(secConfig.getDbdriver());
			con = DriverManager.getConnection(this.url, secConfig.getDbuser(), secConfig.getDbpassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet getUser(String useraccount) {
		try {
			stmt = con.prepareStatement(secConfig.getQueryUser());
			stmt.setString(1, useraccount);
			stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet getAllRes() {
		try {
			stmt = con.prepareStatement(secConfig.getQueryAllRes());
			stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet getUserAuth(String useraccount) {
		try {
			stmt = con.prepareStatement(secConfig.getQueryUserAuth());
			stmt.setString(1, useraccount);
			stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet getAllAuth() {
		try {
			stmt = con.prepareStatement(secConfig.getQueryAllAuth());
			stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet getResAuthByUseraccount(String userAccount, String res) {
		try {
			stmt = con.prepareStatement(secConfig.getQueryPrivilege());
			stmt.setString(1, userAccount);
			stmt.setString(2, res);
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet getResAuth(String res) {
		try {
			stmt = con.prepareStatement(secConfig.getQueryResAuth());
			stmt.setString(1, res);
			stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JdbcSecurityConfig getSecConfig() {
		return secConfig;
	}

	public void setSecConfig(JdbcSecurityConfig secConfig) {
		this.secConfig = secConfig;
	}

	public Connection getCon() {
		return con;
	}

	public ResultSet executeQuery(String sql) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public boolean executeUpdate(String sql) {
		try {
			return stmt.executeUpdate(sql) > 0 ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void destroy() {
		try {
			stmt.close();
			con.close();
			secConfig = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
