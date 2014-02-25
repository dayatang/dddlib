package org.dayatang.dsmonitor.datasource;

import org.apache.commons.lang3.time.StopWatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GeminiConnection extends DelegatingConnection {

	private long creationTime;

    private long closeTime;

	private StackTraceElement[] stackTraceElements = new StackTraceElement[] {};

	private Set<ConnectionMonitor> monitors = new HashSet<ConnectionMonitor>();

	public GeminiConnection(Connection targetConnection,
			Set<ConnectionMonitor> monitors) throws SQLException {
		super(targetConnection);
        if (monitors != null) {
            this.monitors = new HashSet<ConnectionMonitor>();
        }
        this.monitors = new HashSet<ConnectionMonitor>(monitors);
		this.creationTime = System.currentTimeMillis();
		notifyOpenConnection();
	}

	private void notifyOpenConnection() throws SQLException {
		for (ConnectionMonitor monitor : monitors) {
			monitor.openConnection(this);
		}
	}

	private void notifyCloseConnection() throws SQLException {
		for (ConnectionMonitor monitor : monitors) {
			monitor.closeConnection(this);
		}
	}

	@Override
	public void close() throws SQLException {
        closeTime = System.currentTimeMillis();
        notifyCloseConnection();
		super.close();
	}

	public long getSurvivalTime() {
        return System.currentTimeMillis() - creationTime;
	}

	public Set<ConnectionMonitor> getMonitors() {
		return Collections.unmodifiableSet(monitors);
	}

	public void setMonitors(Set<ConnectionMonitor> monitors) {
		this.monitors = new HashSet<ConnectionMonitor>(monitors);
	}

    public void addMonitor(ConnectionMonitor monitor) {
        monitors.add(monitor);
    }

    public void removeMonitor(ConnectionMonitor monitor) {
        monitors.remove(monitor);
    }

	public long getCreationTime() {
		return creationTime;
	}

	public StackTraceElement[] getStackTraceElements() {
		return Arrays.copyOf(stackTraceElements, stackTraceElements.length);
	}

	public void setStackTraceElements(StackTraceElement[] stackTraceElements) {
		this.stackTraceElements = Arrays.copyOf(stackTraceElements, stackTraceElements.length);
	}
}
