package org.openkoala.jbpm.application.vo;


import java.util.Date;

import java.io.Serializable;

public class KoalaAssignInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	private Long id;

		

	private String assignerTo;
	
		

	private String name;
	
		

	private Date beginTime;
	

	private Date beginTimeEnd;
		

	private String creater;
	
		

	private String assigner;
	
		

	private Date endTime;
	

	private Date endTimeEnd;
	
	private String relativeProcess;
		

	public void setAssignerTo(String assignerTo) { 
		this.assignerTo = assignerTo;
	}
	
	public String getAssignerTo() {
		return this.assignerTo;
	}
	
			
		

	public void setName(String name) { 
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
			
		

	public void setBeginTime(Date beginTime) { 
		this.beginTime = beginTime;
	}
	
	public Date getBeginTime() {
		return this.beginTime;
	}
	
		
	public void setBeginTimeEnd(Date beginTimeEnd) { 
		this.beginTimeEnd = beginTimeEnd;
	}
	
	public Date getBeginTimeEnd() {
		return this.beginTimeEnd;
	}
			
		

	public void setCreater(String creater) { 
		this.creater = creater;
	}
	
	public String getCreater() {
		return this.creater;
	}

	public void setAssigner(String assigner) { 
		this.assigner = assigner;
	}
	
	public String getAssigner() {
		return this.assigner;
	}

	public void setEndTime(Date endTime) { 
		this.endTime = endTime;
	}
	
	public Date getEndTime() {
		return this.endTime;
	}
	
		
	public void setEndTimeEnd(Date endTimeEnd) { 
		this.endTimeEnd = endTimeEnd;
	}
	
	public Date getEndTimeEnd() {
		return this.endTimeEnd;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getRelativeProcess() {
		return relativeProcess;
	}

	public void setRelativeProcess(String relativeProcess) {
		this.relativeProcess = relativeProcess;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KoalaAssignInfoVO other = (KoalaAssignInfoVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}