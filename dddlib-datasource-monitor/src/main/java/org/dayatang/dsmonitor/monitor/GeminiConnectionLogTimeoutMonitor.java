package org.dayatang.dsmonitor.monitor;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.dayatang.dsmonitor.GeminiConnection;
import org.dayatang.utils.Slf4JLogger;

import java.sql.SQLException;
import java.util.Set;

public class GeminiConnectionLogTimeoutMonitor extends
		AbstractGeminiConnectionTimeoutMonitor {

	private final Slf4JLogger LOGGER = Slf4JLogger
			.getLogger(GeminiConnectionLogTimeoutMonitor.class);

	private static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	@Override
	public void monitor() {
		logConnections(getAliveTimeoutConnections(), "活动的超时数据库连接");
	}

	private void logConnections(Set<GeminiConnection> connections, String title) {
		if (connections.isEmpty()) {
            LOGGER.info("没有【{}】，超时时间为：【{}】ms", title, getTimeout());
			return;
		}
        LOGGER.info("=======================================================================================");
        LOGGER.info("=======================================================================================");
        LOGGER.info("=============                                                             =============");
        LOGGER.info("=============            以下是{}，超时时间为：【{}】ms         =============", title, getTimeout());
        LOGGER.info("=============                                                             =============");
        LOGGER.info("=======================================================================================");
        LOGGER.info("=======================================================================================");

		for (GeminiConnection conn : connections) {
			logConnection(conn);
		}
	}

	private void logConnection(GeminiConnection connection) {
		try {
			if (connection.isClosed()) {
                LOGGER.info("数据库连接HashCode【{}】，创建时间【{}】，耗时【{}】毫秒", connection.hashCode(),
                        formatTime(connection.getCreationTime()), connection.getSurvivalTime());
			} else {
                LOGGER.info("数据库连接HashCode【{}】，URL=【{}】，创建时间【{}】，耗时【{}】毫秒",
                        connection.hashCode(), connection.getMetaData().getURL(),
                        formatTime(connection.getCreationTime()), connection.getSurvivalTime());
			}

            LOGGER.info("调用堆栈为：");
			for (StackTraceElement ste : connection.getStackTraceElements()) {
                LOGGER.info("     " + ste);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private String formatTime(long date) {
		return DateFormatUtils.format(date, DATE_PATTERN);
	}
}
