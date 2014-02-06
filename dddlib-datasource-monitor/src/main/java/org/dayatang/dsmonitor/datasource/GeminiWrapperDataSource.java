package org.dayatang.dsmonitor.datasource;

import org.dayatang.dsmonitor.monitor.ConnectionMonitor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class GeminiWrapperDataSource extends DelegatingDataSource {

	private Set<ConnectionMonitor> monitors = new HashSet<ConnectionMonitor>();

	public GeminiWrapperDataSource(DataSource targetDataSource, Set<ConnectionMonitor> monitors) {
		super(targetDataSource);
		this.monitors = monitors;
	}

	public Connection getConnection() throws SQLException {
		Connection connection = super.getConnection();
		return buildConnection(connection);

	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		Connection connection = super.getConnection(username, password);
		return buildConnection(connection);
	}

	public Set<ConnectionMonitor> getMonitors() {
		return monitors;
	}

	public void setMonitors(Set<ConnectionMonitor> monitors) {
		this.monitors = monitors;
	}

	protected Connection buildConnection(Connection connection)
			throws SQLException {
		if (connection == null) {
			return null;
		}

		GeminiConnection wrapperConnection = new GeminiConnection(connection,
				monitors);

		return wrapperConnection;

	}

}
