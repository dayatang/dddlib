package org.dayatang.dsmonitor.monitor;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.dayatang.dsmonitor.ConnectionMonitor;
import org.dayatang.dsmonitor.GeminiConnection;
import org.dayatang.utils.Slf4JLogger;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractGeminiConnectionTimeoutMonitor implements ConnectionMonitor {

	private final Slf4JLogger LOGGER = Slf4JLogger.getLogger(AbstractGeminiConnectionTimeoutMonitor.class);

	private static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private Set<GeminiConnection> aliveConnections = Collections.synchronizedSet(new HashSet<GeminiConnection>());

    private int connectionCount;

    /**
	 * 单位：毫秒，默认10000毫秒，即10s
	 */
	private long timeout = 1000;

	public void openConnection(GeminiConnection connection) throws SQLException {
        LOGGER.debug("开启数据库连接HashCode【{}】，URL=【{}】，创建时间【{}】",
                connection.hashCode(), connection.getMetaData().getURL(),
                formatTime(connection.getCreationTime()));
		aliveConnections.add(connection);
        connectionCount++;
	}

	public void closeConnection(GeminiConnection connection) throws SQLException {
        LOGGER.debug("关闭数据库连接HashCode【{}】，创建时间【{}】，耗时【{}】ms",
                connection.hashCode(), formatTime(connection.getCreationTime()),
                connection.getSurvivalTime());
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
     * 获取活动的连接集合
     *
     * @return 活动的连接集合
     */
    public Set<GeminiConnection> getAliveConnections() {
        return Collections.unmodifiableSet(aliveConnections);
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    private String formatTime(long date) {
		return DateFormatUtils.format(date, DATE_PATTERN);
	}
}
