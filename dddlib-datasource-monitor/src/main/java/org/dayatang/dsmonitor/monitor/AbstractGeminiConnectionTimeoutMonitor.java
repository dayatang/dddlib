package org.dayatang.dsmonitor.monitor;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.dayatang.dsmonitor.datasource.GeminiConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractGeminiConnectionTimeoutMonitor implements ConnectionMonitor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGeminiConnectionTimeoutMonitor.class);

	private static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static Set<GeminiConnection> aliveConnections = Collections.synchronizedSet(new HashSet<GeminiConnection>());

	private static Set<GeminiConnection> closedTimeoutConnections = Collections.synchronizedSet(new HashSet<GeminiConnection>());

	/**
	 * 单位：毫秒，默认10000毫秒，即10s
	 */
	private long timeout = 1000;

	public void openConnection(Connection conn) throws SQLException {
		GeminiConnection connection = (GeminiConnection) conn;
		debug("开启数据库连接HashCode【{}】，URL=【{}】，创建时间【{}】", connection.hashCode(), connection.getMetaData().getURL(),
				formatTime(connection.getCreationTime()));
	
		aliveConnections.add(connection);
	}

	public void closeConnection(Connection conn) throws SQLException {
		GeminiConnection connection = (GeminiConnection) conn;

		if (isTimeout(connection)) {
			closedTimeoutConnections.add(connection);
		}
		debug("关闭数据库连接HashCode【{}】，创建时间【{}】，耗时【{}】", connection.hashCode(), formatTime(connection.getCreationTime()), connection.getStopWatch());

		aliveConnections.remove(connection);
	}

	private boolean isTimeout(GeminiConnection conn) {
		if (conn.getTime() > timeout) {
			return true;
		}
		return false;
	}

	/**
	 * 单位：毫秒，默认10000毫秒，即10s
	 * 
	 * @return 超时时间
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * 设置超时时间
	 * 
	 * @param timeout 超时时间
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * 获取活动的超时连接集合
	 * 
	 * @return 活动的超时连接集合
	 */
	public Set<GeminiConnection> getAliveTimeoutConnections() {
		Set<GeminiConnection> aliveTimeoutConnections = new HashSet<GeminiConnection>();
		for (GeminiConnection conn : aliveConnections) {
			if (isTimeout(conn)) {
				aliveTimeoutConnections.add(conn);
			}
		}
		return aliveTimeoutConnections;
	}

	/**
	 * 获取已关闭的超时连接集合
	 * 
	 * @return 已关闭的超时连接集合
	 */
	public Set<GeminiConnection> getClosedTimeoutConnections() {
		return closedTimeoutConnections;
	}

	/**
	 * 获取所有的超时连接集合（包括活动的和已关闭的）
	 * 
	 * @return 所有的超时连接集合（包括活动的和已关闭的）
	 */
	public Set<GeminiConnection> getTimeoutConnections() {
		Set<GeminiConnection> timeoutConnections = new HashSet<GeminiConnection>();
		timeoutConnections.addAll(getClosedTimeoutConnections());
		timeoutConnections.addAll(getAliveTimeoutConnections());
		return timeoutConnections;
	}

	public abstract void monitor();

	private void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}

	private String formatTime(long date) {
		return DateFormatUtils.format(date, DATE_PATTERN);
	}
}
