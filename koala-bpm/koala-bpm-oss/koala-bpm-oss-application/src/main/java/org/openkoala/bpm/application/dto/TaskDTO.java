package org.openkoala.bpm.application.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8839891129054465058L;

	private String creator;

	private String processName;

	private long processInstanceId;
	
	private String processId;
	
	private long taskId;

	private List<DynaProcessKeyDTO> dynamicColumns = new ArrayList<DynaProcessKeyDTO>();

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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public List<DynaProcessKeyDTO> getDynamicColumns() {
		return dynamicColumns;
	}

	public void setDynamicColumns(List<DynaProcessKeyDTO> dynamicColumns) {
		this.dynamicColumns = dynamicColumns;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

}
