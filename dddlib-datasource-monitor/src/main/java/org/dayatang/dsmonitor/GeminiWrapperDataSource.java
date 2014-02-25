package org.dayatang.dsmonitor;

import org.dayatang.utils.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GeminiWrapperDataSource extends DelegatingDataSource {

    private Set<ConnectionMonitor> monitors = new HashSet<ConnectionMonitor>();

    public GeminiWrapperDataSource(DataSource targetDataSource, Set<ConnectionMonitor> monitors) {
        super(targetDataSource);
        setMonitors(monitors);
    }

    public Connection getConnection() throws SQLException {
        return buildConnection(super.getConnection());
    }

    public Connection getConnection(String username, String password)
            throws SQLException {
        return buildConnection(super.getConnection(username, password));
    }

    public Set<ConnectionMonitor> getMonitors() {
        return Collections.unmodifiableSet(monitors);
    }

    public final void setMonitors(Set<ConnectionMonitor> monitors) {
        if (monitors != null) {
            this.monitors = monitors;
        }
    }

    public void addMonitor(ConnectionMonitor monitor) {
        Assert.notNull(monitor, "Monitor to add cannot be null!");
        monitors.add(monitor);
    }

    public void removeMonitor(ConnectionMonitor monitor) {
        Assert.notNull(monitor, "Monitor to add cannot be null!");
        monitors.remove(monitor);
    }

    private Connection buildConnection(Connection connection) throws SQLException {
        if (connection == null) {
            return null;
        }
        return new GeminiConnection(connection, monitors);
    }

}
