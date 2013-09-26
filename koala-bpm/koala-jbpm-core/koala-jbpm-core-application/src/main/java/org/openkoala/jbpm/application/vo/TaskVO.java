package org.openkoala.jbpm.application.vo;

import java.io.Serializable;

public class TaskVO implements Serializable {
	
	private long taskId;//任务ID
	
	private String actualOwner;//任务所有者
	
	private String actualName;//任务名

	private String processId;// 流程ID名称

	private long processInstanceId;// 流程实例ID

	private String processName;// 流程名称

	private String createDate;// 流程创建日期

	private String lastUpdateDate;// 流程最后更新时间

	private String creater;// 流程的创建者
	
	private String agents = "否";//是否代理任务

	private String processData;//流程变量数据，XML格式
	
	private String taskData;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6164628195347599331L;

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getActualOwner() {
		return actualOwner;
	}

	public void setActualOwner(String actualOwner) {
		this.actualOwner = actualOwner;
	}

	public String getActualName() {
		return actualName;
	}

	public void setActualName(String actualName) {
		this.actualName = actualName;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getProcessData() {
		return processData;
	}

	public void setProcessData(String processData) {
		this.processData = processData;
	}

	public String getTaskData() {
		return taskData;
	}

	public void setTaskData(String taskData) {
		this.taskData = taskData;
	}

	public String getAgents() {
	
		return agents;
	}

	public void setAgents(String agents) {
	
		this.agents = agents;
	}

}
