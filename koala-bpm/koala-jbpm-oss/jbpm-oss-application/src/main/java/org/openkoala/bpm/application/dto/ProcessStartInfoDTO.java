package org.openkoala.bpm.application.dto;

import java.util.List;

public class ProcessStartInfoDTO {
	/**
	 * 流程ID
	 */
	private String processId;
	/**
	 * 流程的创建者
	 */
	private String creater;
	/**
	 * 流程实例ID
	 */
	long processInstanceId;

	/**
	 * 值诸如：@KJ_keyId@@@keyName##showOrder##innerVariable##keyValue###@KJ_keyId@@@keyName##showOrder##innerVariable##keyValue###...
	 * innerVariable是指：是否作为流程变量
	 */
	private String dynaProcessKeysForShow;
	
	/**
	 * 作为流程变量的key放入该列表
	 */
	private List<DynaProcessKeyDTO> innerVariables;
	
	/**
	 * 表单所有的key
	 */
	private List<DynaProcessKeyDTO> allDynaProcessKeys;
	

	public List<DynaProcessKeyDTO> getAllDynaProcessKeys() {
		return allDynaProcessKeys;
	}

	public void setAllDynaProcessKeys(List<DynaProcessKeyDTO> allDynaProcessKeys) {
		this.allDynaProcessKeys = allDynaProcessKeys;
	}

	public List<DynaProcessKeyDTO> getInnerVariables() {
		return innerVariables;
	}

	public void setInnerVariables(List<DynaProcessKeyDTO> innerVariables) {
		this.innerVariables = innerVariables;
	}

	public String getDynaProcessKeysForShow() {
		return dynaProcessKeysForShow;
	}

	public void setDynaProcessKeysForShow(String dynaProcessKeysForShow) {
		this.dynaProcessKeysForShow = dynaProcessKeysForShow;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

}
