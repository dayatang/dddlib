package org.dayatang.dsmonitor.datasource;

import org.dayatang.dsmonitor.datasource.GeminiConnection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMonitor {

	public void openConnection(GeminiConnection connection) throws SQLException;

	public void closeConnection(GeminiConnection connection) throws SQLException;

	public void monitor() throws SQLException;
}
