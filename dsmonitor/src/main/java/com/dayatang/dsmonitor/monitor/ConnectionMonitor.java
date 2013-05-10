package com.dayatang.dsmonitor.monitor;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMonitor {

	public void openConnection(Connection conn) throws SQLException;

	public void closeConnection(Connection conn) throws SQLException;

	public void monitor() throws SQLException;
}
