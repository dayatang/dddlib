package org.openkoala.bpm.application.dto;

import java.io.Serializable;

public class DynaProcessTemplateDTO
  implements Serializable
{
  private static final long serialVersionUID = 1314875000055721456L;
  
  private Long id;
  private String templateName;
  private String templateDescription;
  private String templateData;
  
  

  public DynaProcessTemplateDTO() {}
  
  

public DynaProcessTemplateDTO(String templateName, String templateDescription,
		String templateData) {
	super();
	this.templateName = templateName;
	this.templateDescription = templateDescription;
	this.templateData = templateData;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getTemplateName()
  {
    return this.templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getTemplateDescription() {
    return this.templateDescription;
  }

  public void setTemplateDescription(String templateDescription) {
    this.templateDescription = templateDescription;
  }

  public String getTemplateData() {
    return this.templateData;
  }

  public void setTemplateData(String templateData) {
    this.templateData = templateData;
  }
}