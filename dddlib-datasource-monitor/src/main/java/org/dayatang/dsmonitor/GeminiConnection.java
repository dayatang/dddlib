package org.dayatang.dsmonitor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GeminiConnection extends DelegatingConnection {

	private long creationTime;

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
		notifyOpen();
	}

	private void notifyOpen() throws SQLException {
		for (ConnectionMonitor monitor : monitors) {
			monitor.openConnection(this);
		}
	}

	@Override
	public void close() throws SQLException {
        notifyClose();
		super.close();
	}

    private void notifyClose() throws SQLException {
        for (ConnectionMonitor monitor : monitors) {
            monitor.closeConnection(this);
        }
    }

	public long getSurvivalTime() {
        return System.currentTimeMillis() - creationTime;
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
