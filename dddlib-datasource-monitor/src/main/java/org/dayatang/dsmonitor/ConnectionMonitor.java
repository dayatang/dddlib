package org.dayatang.dsmonitor;

import java.sql.SQLException;

public interface ConnectionMonitor {

	public void openConnection(GeminiConnection connection) throws SQLException;

	public void closeConnection(GeminiConnection connection) throws SQLException;

	public void monitor() throws SQLException;
}
