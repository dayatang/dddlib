package com.dayatang.configuration.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Set;

import javax.sql.DataSource;

import com.dayatang.configuration.ConfigurationException;
import com.dayatang.utils.Slf4jLogger;

public class ConfigurationDbUtils {
	
	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(ConfigurationDbUtils.class);
	
	private DataSource dataSource;
	private String tableName = "SYS_CONFIG";
	private String keyColumn = "KEY_COLUMN";
	private String valueColumn = "VALUE_COLUMN";

	public ConfigurationDbUtils(DataSource dataSource, String tableName, String keyColumn, String valueColumn) {
		this.dataSource = dataSource;
		this.tableName = tableName;
		this.keyColumn = keyColumn;
		this.valueColumn = valueColumn;
	}

	private void createTableIfNotExists() {
		String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(255) PRIMARY KEY, %s VARCHAR(255))", tableName, keyColumn, valueColumn);
		executeUpdate(sql);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.configuration.WritableConfiguration#save()
	 */
	public void save(Hashtable<String, String> hTable) {
		createTableIfNotExists();
		Connection connection = null;
		PreparedStatement queryStmt = null;
		PreparedStatement updateStmt = null;
		PreparedStatement insertStmt = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			queryStmt = connection.prepareStatement(String.format("SELECT * FROM %s", tableName));
			updateStmt = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE %s = ?", tableName, valueColumn, keyColumn));
			insertStmt = connection.prepareStatement(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",  tableName, keyColumn, valueColumn));
			rs = queryStmt.executeQuery();
			Set<String> keys = hTable.keySet();
			while (rs.next()) {
				String key = rs.getString(keyColumn);
				if (keys.contains(key)) {
					updateStmt.setString(1, hTable.get(key));
					updateStmt.setString(2, key);
					updateStmt.executeUpdate();
					keys.remove(key);
				}
			}
			for (String key : keys) {
				insertStmt.setString(1, key);
				insertStmt.setString(2, hTable.get(key));
				insertStmt.executeUpdate();
			}
			connection.commit();
			
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ConfigurationException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close Resultset!");
					throw new ConfigurationException(e);
				}
			}
			if (queryStmt != null) {
				try {
					queryStmt.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close query statement!");
					throw new ConfigurationException(e);
				}
			}
			if (updateStmt != null) {
				try {
					updateStmt.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close update statement!");
					throw new ConfigurationException(e);
				}
			}
			if (insertStmt != null) {
				try {
					insertStmt.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close insert statement!");
					throw new ConfigurationException(e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close connection!");
					throw new ConfigurationException(e);
				}
			}
		}
	}

	public Hashtable<String, String> load() {
		createTableIfNotExists();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Hashtable<String, String> results = new Hashtable<String, String>();
		try {
			connection = dataSource.getConnection();
			stmt = connection.prepareStatement("SELECT * FROM " + tableName);
			rs = stmt.executeQuery();
			while (rs.next()) {
				results.put(rs.getString(keyColumn), rs.getString(valueColumn));
			}
			return results;
		} catch (SQLException e) {
			throw new ConfigurationException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close Resultset!");
					throw new ConfigurationException(e);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close query statement!");
					throw new ConfigurationException(e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close connection!");
					throw new ConfigurationException(e);
				}
			}
		}
	}

	private int executeUpdate(String sql) {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = dataSource.getConnection();
			stmt = connection.prepareStatement(sql);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new ConfigurationException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close query statement!");
					throw new ConfigurationException(e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close connection!");
					throw new ConfigurationException(e);
				}
			}
		}
	}
}
