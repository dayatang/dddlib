package org.openkoala.jbpm.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.dayatang.domain.AbstractEntity;

@Entity
public class KoalaAssignInfo  extends AbstractEntity{

	private static final long serialVersionUID = -7692102858676116384L;

	private String name;
	
	private String assigner;
	
	private String assignerTo;
	
	private Date beginTime;
	
	private Date endTime;
	
	private String creater;
	
	@OneToMany(mappedBy = "assignInfo",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<KoalaAssignDetail> jbpmNames;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public String getAssignerTo() {
		return assignerTo;
	}

	public void setAssignerTo(String assignerTo) {
		this.assignerTo = assignerTo;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assigner == null) ? 0 : assigner.hashCode());
		result = prime * result
				+ ((assignerTo == null) ? 0 : assignerTo.hashCode());
		result = prime * result
				+ ((beginTime == null) ? 0 : beginTime.hashCode());
		result = prime * result + ((creater == null) ? 0 : creater.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public List<KoalaAssignDetail> getJbpmNames() {
		if(jbpmNames==null)jbpmNames = new ArrayList<KoalaAssignDetail>();
		return jbpmNames;
	}

	public void setJbpmNames(List<KoalaAssignDetail> jbpmNames) {
		this.jbpmNames = jbpmNames;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KoalaAssignInfo other = (KoalaAssignInfo) obj;
		if (assigner == null) {
			if (other.assigner != null)
				return false;
		} else if (!assigner.equals(other.assigner))
			return false;
		if (assignerTo == null) {
			if (other.assignerTo != null)
				return false;
		} else if (!assignerTo.equals(other.assignerTo))
			return false;
		if (beginTime == null) {
			if (other.beginTime != null)
				return false;
		} else if (!beginTime.equals(other.beginTime))
			return false;
		if (creater == null) {
			if (other.creater != null)
				return false;
		} else if (!creater.equals(other.creater))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public String getRelativeProcess(){
		if(this.jbpmNames==null)return null;
		else{
			String value = "";
			for(int i=0;i<this.jbpmNames.size();i++){
				value = value + jbpmNames.get(i).getProcessId();
				if(i!=this.jbpmNames.size()-1)value = value + ";";
			}
			return value;
		}
	}

	@Override
	public String toString() {
		return "KoalaAssignInfo [name=" + name + ", assigner=" + assigner
				+ ", assignerTo=" + assignerTo + ", beginTime=" + beginTime
				+ ", endTime=" + endTime + ", creater=" + creater + "]";
	}
	
}
