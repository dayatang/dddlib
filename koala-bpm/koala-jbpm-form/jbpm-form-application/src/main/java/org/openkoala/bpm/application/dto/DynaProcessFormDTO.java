package org.openkoala.bpm.application.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DynaProcessFormDTO implements Serializable {
	private static final long serialVersionUID = 1314875000055721456L;

	private Long id;
	private String processId;
	private String bizName;
	private String bizDescription;
	private String active;
	DynaProcessTemplateDTO template;
	private String templateName;
	private long templateId;
	List<DynaProcessKeyDTO> processKeys;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcessId() {
		return this.processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getBizName() {
		return this.bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getBizDescription() {
		return this.bizDescription;
	}

	public void setBizDescription(String bizDescription) {
		this.bizDescription = bizDescription;
	}

	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public DynaProcessTemplateDTO getTemplate() {
		return this.template;
	}

	public void setTemplate(DynaProcessTemplateDTO template) {
		this.template = template;
	}

	public List<DynaProcessKeyDTO> getProcessKeys() {
		if (this.processKeys == null)
			this.processKeys = new ArrayList<DynaProcessKeyDTO>();
		return this.processKeys;
	}

	public void setProcessKeys(List<DynaProcessKeyDTO> processKeys) {
		this.processKeys = processKeys;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
}