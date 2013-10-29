package org.openkoala.bpm.application.dto;

public class DynaProcessHistoryValueDTO {
	private Long id;
	private long processInstanceId;
	private String keyValue;
	private DynaProcessKeyDTO dynaProcessKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public DynaProcessKeyDTO getDynaProcessKey() {
		return dynaProcessKey;
	}

	public void setDynaProcessKey(DynaProcessKeyDTO dynaProcessKey) {
		this.dynaProcessKey = dynaProcessKey;
	}

}
