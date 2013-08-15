package com.dayatang.dsmonitor.monitor;

import java.sql.SQLException;
import java.util.Set;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.dsmonitor.datasource.GeminiConnection;

public class GeminiConnectionLogTimeoutMonitor extends
		AbstractGeminiConnectionTimeoutMonitor {

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(GeminiConnectionLogTimeoutMonitor.class);

	private static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	@Override
	public void monitor() {
		logConnections(getClosedTimeoutConnections(), "已关闭的超时数据库连接");
		logConnections(getAliveTimeoutConnections(), "活动的超时数据库连接");
	}

	private void logConnections(Set<GeminiConnection> closedTimeoutConnections,
			String title) {
		if (closedTimeoutConnections.isEmpty()) {
			info("没有【{}】，超时时间为：【{}】ms", title, getTimeout());
			return;
		}
		info("=======================================================================================");
		info("=======================================================================================");
		info("=============                                                             =============");
		info("=============            以下是{}，超时时间为：【{}】ms         =============",
				title, getTimeout());
		info("=============                                                             =============");
		info("=======================================================================================");
		info("=======================================================================================");

		for (GeminiConnection conn : closedTimeoutConnections) {
			logConnection(conn);
		}
	}

	private void logConnection(GeminiConnection conn) {
		try {
			if (conn.isClosed()) {
				info("数据库连接HashCode【{}】，创建时间【{}】，耗时【{}】", conn.hashCode(),
						formatTime(conn.getCreationTime()), conn.getStopWatch());
			} else {
				info("数据库连接HashCode【{}】，URL=【{}】，创建时间【{}】，耗时【{}】",
						conn.hashCode(), conn.getMetaData().getURL(),
						formatTime(conn.getCreationTime()), conn.getStopWatch());
			}

			StackTraceElement[] stackTraceElements = conn
					.getStackTraceElements();
			info("调用堆栈为：");
			for (StackTraceElement ste : stackTraceElements) {
				info("     " + ste);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void info(String message, Object... params) {
		LOGGER.info(message, params);
	}

	private String formatTime(long date) {
		return DateFormatUtils.format(date, DATE_PATTERN);
	}
}
