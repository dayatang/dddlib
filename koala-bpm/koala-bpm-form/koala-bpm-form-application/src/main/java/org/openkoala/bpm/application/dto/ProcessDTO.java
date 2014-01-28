package org.openkoala.bpm.application.dto;

import java.io.Serializable;

public class ProcessDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String processId;
	
	private String processName;

	public ProcessDTO() {}

	public ProcessDTO(String processId, String processName) {
		super();
		this.processId = processId;
		this.processName = processName;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

}
