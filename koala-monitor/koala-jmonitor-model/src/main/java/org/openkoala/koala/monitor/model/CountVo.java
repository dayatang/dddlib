/**
 * 统计类(统计http、method、jdbc)
 */
package org.openkoala.koala.monitor.model;

import java.io.Serializable;

public class CountVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 监控系统
	 */
	private String system;

	/**
	 * 时段
	 */
	private String dateStr;

	/**
	 * http请求次数
	 */
	private int httpCount;

	/**
	 * 方法调用次数
	 */
	private int methodCount;

	/**
	 * 方法调用出错次数
	 */
	private int methodExceptionCount;

	/**
	 * 方法
	 */
	private String method;

	/**
	 * Connection打开数量
	 */
	private int connectionOpenCount;

	/**
	 * Connection关闭数量
	 */
	private int connectionCloseCount;

	/**
	 * PreparedStatement创建数量
	 */
	private int preparedStatementCreateCount;

	/**
	 * PreparedStatement关闭数量
	 */
	private int preparedStatementCloseCount;

	/**
	 * Statement创建数量
	 */
	private int statementCreateCount;

	/**
	 * Statement关闭数量
	 */
	private int statementCloseCount;

	/**
	 * 平均耗时（单位：毫秒）
	 */
	private long avgTimeConsume;

	public int getMethodExceptionCount() {
		return methodExceptionCount;
	}

	public void setMethodExceptionCount(int methodExceptionCount) {
		this.methodExceptionCount = methodExceptionCount;
	}

	public long getAvgTimeConsume() {
		return avgTimeConsume;
	}

	public void setAvgTimeConsume(long avgTimeConsume) {
		this.avgTimeConsume = avgTimeConsume;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public int getHttpCount() {
		return httpCount;
	}

	public void setHttpCount(int httpCount) {
		this.httpCount = httpCount;
	}

	public int getMethodCount() {
		return methodCount;
	}

	public void setMethodCount(int methodCount) {
		this.methodCount = methodCount;
	}

	public int getConnectionOpenCount() {
		return connectionOpenCount;
	}

	public void setConnectionOpenCount(int connectionOpenCount) {
		this.connectionOpenCount = connectionOpenCount;
	}

	public int getConnectionCloseCount() {
		return connectionCloseCount;
	}

	public void setConnectionCloseCount(int connectionCloseCount) {
		this.connectionCloseCount = connectionCloseCount;
	}

	public int getPreparedStatementCreateCount() {
		return preparedStatementCreateCount;
	}

	public void setPreparedStatementCreateCount(int preparedStatementCreateCount) {
		this.preparedStatementCreateCount = preparedStatementCreateCount;
	}

	public int getPreparedStatementCloseCount() {
		return preparedStatementCloseCount;
	}

	public void setPreparedStatementCloseCount(int preparedStatementCloseCount) {
		this.preparedStatementCloseCount = preparedStatementCloseCount;
	}

	public int getStatementCreateCount() {
		return statementCreateCount;
	}

	public void setStatementCreateCount(int statementCreateCount) {
		this.statementCreateCount = statementCreateCount;
	}

	public int getStatementCloseCount() {
		return statementCloseCount;
	}

	public void setStatementCloseCount(int statementCloseCount) {
		this.statementCloseCount = statementCloseCount;
	}

}
