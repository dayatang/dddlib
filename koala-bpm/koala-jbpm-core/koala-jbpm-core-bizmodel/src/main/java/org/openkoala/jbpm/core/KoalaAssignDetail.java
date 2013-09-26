package org.openkoala.jbpm.core;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dayatang.domain.AbstractEntity;

@Entity
public class KoalaAssignDetail extends AbstractEntity {

	private static final long serialVersionUID = 9037655291650063741L;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade = CascadeType.PERSIST)
	@JoinColumn(name="assign_info_id") 
	private KoalaAssignInfo assignInfo;
	
	public KoalaAssignDetail() {
		super();
	}

	public KoalaAssignDetail(String processId) {
		super();
		this.processId = processId;
	}
	
	

	public KoalaAssignDetail(KoalaAssignInfo assignInfo, String processId) {
		super();
		this.assignInfo = assignInfo;
		this.processId = processId;
	}



	private String processId;
	
	public KoalaAssignInfo getAssignInfo() {
		return assignInfo;
	}

	public void setAssignInfo(KoalaAssignInfo assignInfo) {
		this.assignInfo = assignInfo;
	}

	public String getProcessId() {
	
		return processId;
	}

	public void setProcessId(String processId) {
	
		this.processId = processId;
	}

	@Override
	public String toString() {
		return "KoalaAssignDetail [assignInfo=" + assignInfo + ", processId="
				+ processId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assignInfo == null) ? 0 : assignInfo.hashCode());
		result = prime * result
				+ ((processId == null) ? 0 : processId.hashCode());
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
		KoalaAssignDetail other = (KoalaAssignDetail) obj;
		if (assignInfo == null) {
			if (other.assignInfo != null)
				return false;
		} else if (!assignInfo.equals(other.assignInfo))
			return false;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		return true;
	}
	
}
