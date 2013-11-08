package org.openkoala.koala.auth.impl.ejb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class JdbcManager {
	private JdbcSecurityConfig secConfig;
	private String url;
	private Connection con;
	private PreparedStatement stmt;
	
	private static final Logger LOGGER = Logger.getLogger("JdbcManager");

	public JdbcManager(JdbcSecurityConfig secConfig) {
		this.secConfig = secConfig;
		this.url = secConfig.getDburl();
		try {
			Class.forName(secConfig.getDbdriver());
			con = DriverManager.getConnection(this.url, secConfig.getDbuser(), secConfig.getDbpassword());
		} catch (ClassNotFoundException e) {
			LOGGER.info(e.getMessage());
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		}
	}

	public ResultSet getUser(String useraccount) {
		try {
			stmt = con.prepareStatement(secConfig.getQueryUser());
			stmt.setString(1, useraccount);
			stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			return stmt.executeQuery();
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		}
		return null;
	}

	public ResultSet getAllRes() {
		try {
			stmt = con.prepareStatement(secConfig.getQueryResAuth());
			stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			return stmt.executeQuery();
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
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
			LOGGER.info(e.getMessage());
		}
		return null;
	}

	public ResultSet getAllAuth() {
		try {
			stmt = con.prepareStatement(secConfig.getQueryAllAuth());
			stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			return stmt.executeQuery();
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
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
			LOGGER.info(e.getMessage());
		}
		return rs;
	}

	public boolean executeUpdate(String sql) {
		try {
			return stmt.executeUpdate(sql) > 0 ? true : false;
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		}
		return false;
	}

	public void destroy() {
		try {
			stmt.close();
			con.close();
			secConfig = null;
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		}
	}

}
