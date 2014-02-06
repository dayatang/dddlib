package org.dayatang.mysql.jdbc;

import com.mysql.jdbc.NonRegisteringDriver;
import com.mysql.jdbc.ReplicationConnection;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;

public class GeminiReplicationConnection extends ReplicationConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeminiReplicationConnection.class);

	public GeminiReplicationConnection(Properties masterProperties, Properties slaveProperties) throws SQLException {
		NonRegisteringDriver driver = new NonRegisteringDriver();

		StringBuffer masterUrl = new StringBuffer("jdbc:mysql://");
		StringBuffer slaveUrl = new StringBuffer("jdbc:mysql://");

		String masterHost = masterProperties.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY);
		if (masterHost != null) {
			masterUrl.append(masterHost);
		}

		String slaveHost = slaveProperties.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY);
		if (slaveHost != null) {
			slaveUrl.append(slaveHost);
		}

		String masterDb = masterProperties.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY);
		masterUrl.append("/");
		if (masterDb != null) {
			masterUrl.append(masterDb);
		}

		String slaveDb = slaveProperties.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY);
		slaveUrl.append("/");
		if (slaveDb != null) {
			slaveUrl.append(slaveDb);
		}

		slaveProperties.setProperty("roundRobinLoadBalance", "true");

		this.masterConnection = (com.mysql.jdbc.Connection) driver.connect(masterUrl.toString(), masterProperties);

		if (StringUtils.isBlank(slaveHost) && slaveUrl.toString().contains("///")) {
			info(" ----- the salveUrl contains the '///', that means there is no slaver, make slavesConnection = masterConnection --");
			slavesConnection = masterConnection;
		} else {
			this.slavesConnection = (com.mysql.jdbc.Connection) driver.connect(slaveUrl.toString(), slaveProperties);
			this.slavesConnection.setReadOnly(true);
		}

		this.currentConnection = this.masterConnection;
	}

	//For JDK 7 compatability
	public void setSchema(String schema) throws SQLException {
	}

	//For JDK 7 compatability
	public String getSchema() throws SQLException {
		return null;
	}

	//For JDK 7 compatability
	public void abort(Executor executor) throws SQLException {
	}

	//For JDK 7 compatability
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
	}

	//For JDK 7 compatability
	public int getNetworkTimeout() throws SQLException {
		return 0;
	}

	private void info(String message, Object... params) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(message, params);
		}
	}
}
