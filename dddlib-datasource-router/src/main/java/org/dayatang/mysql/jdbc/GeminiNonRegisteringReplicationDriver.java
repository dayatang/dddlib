package org.dayatang.mysql.jdbc;

import com.mysql.jdbc.NonRegisteringDriver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class GeminiNonRegisteringReplicationDriver extends NonRegisteringDriver {
	
	public GeminiNonRegisteringReplicationDriver() throws SQLException {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
	 */
	public Connection connect(String url, Properties info) throws SQLException {
		Properties parsedProps = parseURL(url, info);

		if (parsedProps == null) {
			return null;
		}

		Properties masterProps = (Properties) parsedProps.clone();
		Properties slavesProps = (Properties) parsedProps.clone();

		// Marker used for further testing later on, also when
		// debugging
		slavesProps.setProperty("com.mysql.jdbc.ReplicationConnection.isSlave",
				"true");

		String hostValues = parsedProps.getProperty(HOST_PROPERTY_KEY);

		if (hostValues != null) {
			StringTokenizer st = new StringTokenizer(hostValues, ",");

			StringBuffer masterHost = new StringBuffer();
			StringBuffer slaveHosts = new StringBuffer();

			if (st.hasMoreTokens()) {
				String[] hostPortPair = parseHostPortPair(st.nextToken());

				if (hostPortPair[HOST_NAME_INDEX] != null) {
					masterHost.append(hostPortPair[HOST_NAME_INDEX]);
				}

				if (hostPortPair[PORT_NUMBER_INDEX] != null) {
					masterHost.append(":");
					masterHost.append(hostPortPair[PORT_NUMBER_INDEX]);
				}
			}

			boolean firstSlaveHost = true;

			while (st.hasMoreTokens()) {
				String[] hostPortPair = parseHostPortPair(st.nextToken());

				if (!firstSlaveHost) {
					slaveHosts.append(",");
				} else {
					firstSlaveHost = false;
				}

				if (hostPortPair[HOST_NAME_INDEX] != null) {
					slaveHosts.append(hostPortPair[HOST_NAME_INDEX]);
				}

				if (hostPortPair[PORT_NUMBER_INDEX] != null) {
					slaveHosts.append(":");
					slaveHosts.append(hostPortPair[PORT_NUMBER_INDEX]);
				}
			}

//			if (slaveHosts.length() == 0) {
//				throw SQLError
//						.createSQLException(
//								"Must specify at least one slave host to connect to for master/slave replication load-balancing functionality",
//								SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE);
//			}

			masterProps.setProperty(HOST_PROPERTY_KEY, masterHost.toString());
			slavesProps.setProperty(HOST_PROPERTY_KEY, slaveHosts.toString());
		}

		return new GeminiReplicationConnection(masterProps, slavesProps);
	}

	//For JDK 7 compatability
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
}