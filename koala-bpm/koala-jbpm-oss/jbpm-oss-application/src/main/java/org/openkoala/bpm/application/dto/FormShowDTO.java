package org.openkoala.bpm.application.dto;

import java.io.Serializable;
import java.util.List;

import org.openkoala.jbpm.application.vo.HistoryLogVo;
import org.openkoala.jbpm.application.vo.TaskChoice;

public class FormShowDTO implements Serializable {

	private static final long serialVersionUID = -6077578716891632227L;

	/**
	 * 所有要动态显示在待办列表中的key
	 */
	private String dynaProcessKeysForShow;

	/**
	 * 所有要固定作为流程变量的key
	 */
	private String innerVariables;

	/**
	 * 表单模版html代码
	 */
	private String templateHtmlCode;
	
	/**
	 * 流程实例的历史记录
	 */
	private List<HistoryLogVo> historyLogs;
	
	/**
	 * 策略（如：通过、不通过）
	 */
	private List<TaskChoice> taskChoices;
	
	private long processInstanceId;
	
	private long taskId;

	private String processId;
	
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public List<TaskChoice> getTaskChoices() {
		return taskChoices;
	}

	public void setTaskChoices(List<TaskChoice> taskChoices) {
		this.taskChoices = taskChoices;
	}

	public List<HistoryLogVo> getHistoryLogs() {
		return historyLogs;
	}

	public void setHistoryLogs(List<HistoryLogVo> historyLogs) {
		this.historyLogs = historyLogs;
	}

	public String getInnerVariables() {
		return innerVariables;
	}

	public void setInnerVariables(String innerVariables) {
		this.innerVariables = innerVariables;
	}

	public String getDynaProcessKeysForShow() {
		return dynaProcessKeysForShow;
	}

	public void setDynaProcessKeysForShow(String dynaProcessKeysForShow) {
		this.dynaProcessKeysForShow = dynaProcessKeysForShow;
	}

	public String getTemplateHtmlCode() {
		return templateHtmlCode;
	}

	public void setTemplateHtmlCode(String templateHtmlCode) {
		this.templateHtmlCode = templateHtmlCode;
	}

}
