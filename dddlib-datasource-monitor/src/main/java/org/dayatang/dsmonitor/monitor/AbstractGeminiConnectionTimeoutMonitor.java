package org.dayatang.dsmonitor.monitor;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.dayatang.dsmonitor.datasource.ConnectionMonitor;
import org.dayatang.dsmonitor.datasource.GeminiConnection;
import org.dayatang.utils.Slf4JLogger;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractGeminiConnectionTimeoutMonitor implements ConnectionMonitor {

	private final Slf4JLogger LOGGER = Slf4JLogger.getLogger(AbstractGeminiConnectionTimeoutMonitor.class);

	private static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static Set<GeminiConnection> aliveConnections = Collections.synchronizedSet(new HashSet<GeminiConnection>());

	private static Set<GeminiConnection> closedTimeoutConnections = Collections.synchronizedSet(new HashSet<GeminiConnection>());

	/**
	 * 单位：毫秒，默认10000毫秒，即10s
	 */
	private long timeout = 1000;

	public void openConnection(GeminiConnection connection) throws SQLException {
        LOGGER.debug("开启数据库连接HashCode【{}】，URL=【{}】，创建时间【{}】",
                connection.hashCode(), connection.getMetaData().getURL(),
                formatTime(connection.getCreationTime()));
		aliveConnections.add(connection);
	}

	public void closeConnection(GeminiConnection connection) throws SQLException {
		if (isTimeout(connection)) {
			closedTimeoutConnections.add(connection);
		}
        LOGGER.debug("关闭数据库连接HashCode【{}】，创建时间【{}】，耗时【{}】",
                connection.hashCode(), formatTime(connection.getCreationTime()),
                connection.getStopWatch());
		aliveConnections.remove(connection);
	}

	private boolean isTimeout(GeminiConnection connection) {
        return connection.getSurvivalTime() > timeout;
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
	 * 设置超时时间，以毫秒为单位
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
		Set<GeminiConnection> results = new HashSet<GeminiConnection>();
		for (GeminiConnection conn : aliveConnections) {
			if (isTimeout(conn)) {
				results.add(conn);
			}
		}
		return results;
	}

	/**
	 * 获取已关闭的超时连接集合
	 * 
	 * @return 已关闭的超时连接集合
	 */
	public Set<GeminiConnection> getClosedTimeoutConnections() {
		return Collections.unmodifiableSet(closedTimeoutConnections);
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

	private String formatTime(long date) {
		return DateFormatUtils.format(date, DATE_PATTERN);
	}
}
