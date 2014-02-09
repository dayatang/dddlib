package org.openkoala.koala.monitor.model;

import java.io.Serializable;
import java.util.Date;

public class JdbcStatementDetailsVo implements Serializable {

	private static final long serialVersionUID = 3510223848909036109L;

	private Long id;
	private int version;
	private Date beginTime;
	private Long timeConsume;
	private String sql;
	/**
	 * 类型：增删改查
	 */
	private String type;
	/**
	 * 连接id
	 */
	private Long fkConnId;
	/**
	 * 方法id
	 */
	private Long methodId;

	// 排序
	private String sortname;
	private String sortorder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Long getTimeConsume() {
		return timeConsume;
	}

	public void setTimeConsume(Long timeConsume) {
		this.timeConsume = timeConsume;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getFkConnId() {
		return fkConnId;
	}

	public void setFkConnId(Long fkConnId) {
		this.fkConnId = fkConnId;
	}

	public Long getMethodId() {
		return methodId;
	}

	public void setMethodId(Long methodId) {
		this.methodId = methodId;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

}
