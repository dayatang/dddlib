package org.dayatang.mysql.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.logging.Logger;

public class GeminiReplicationDriver extends GeminiNonRegisteringReplicationDriver implements java.sql.Driver {
	// ~ Static fields/initializers
	// ---------------------------------------------

	//
	// Register ourselves with the DriverManager
	//
	static {
		try {

			Enumeration<Driver> drivers = DriverManager.getDrivers();
			while (drivers.hasMoreElements()) {
				Driver d = drivers.nextElement();
				DriverManager.deregisterDriver(d);
			}

			java.sql.DriverManager.registerDriver(new GeminiNonRegisteringReplicationDriver());
		} catch (Exception E) {
			throw new RuntimeException("Can't registerSubscriber driver!");
		}
	}

	// ~ Constructors
	// -----------------------------------------------------------

	/**
	 * Construct a new driver and registerSubscriber it with DriverManager
	 * 
	 * @throws SQLException
	 *             if a database error occurs.
	 */
	public GeminiReplicationDriver() throws SQLException {
		// Required for Class.forName().newInstance()
	}

	//For JDK 7 compatability
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

}
