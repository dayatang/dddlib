package org.openkoala.bpm.application.dto;

import java.io.Serializable;

import org.openkoala.jbpm.application.vo.TaskChoice;


public class TaskVerifyDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5386965756916333158L;
	/**
	 * 登陆用户
	 */
	private String user;
	/**
	 * 流程实例ID
	 */
	private long processInstanceId;
	/**
	 * 任务ID
	 */
	private long taskId;
	/**
	 * 决策（同意/不同意 等）
	 */
	private String choice;
	/**
	 * 决策（choice解析成该对象）
	 */
	private TaskChoice taskChoice;
	
	/**
	 * 决策意见
	 */
	private String comment;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public TaskChoice getTaskChoice() {
		return taskChoice;
	}

	public void setTaskChoice(TaskChoice taskChoice) {
		this.taskChoice = taskChoice;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
