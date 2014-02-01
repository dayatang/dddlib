package com.dayatang.dbunit;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;

/**
 * DBUnit模板类。用于创建数据连接、将数据连接传递给DbUnitCallback使用和关闭数据连接。 
 */
public class DbUnitTemplate {
	
	private DataSource dataSource;

	public DbUnitTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void execute(DbUnitCallback callback) {
		IDatabaseConnection connection = null;
		try {
			connection = createConnection();
			callback.doInDbUnit(connection);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			closeConnection(connection);
		}
	}
	
	private IDatabaseConnection createConnection() {
		try {
			return new DatabaseConnection(dataSource.getConnection());
		} catch (Exception e) {
			throw new RuntimeException("Cannot create database connection.", e);
		}
	}

	private void closeConnection(IDatabaseConnection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
