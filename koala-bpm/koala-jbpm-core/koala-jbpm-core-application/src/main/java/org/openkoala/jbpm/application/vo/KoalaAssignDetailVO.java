package org.openkoala.jbpm.application.vo;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement()
public class KoalaAssignDetailVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5050906258596776443L;



	private Long id;

		

	private String processId;
	
		

	private String processName;
	
			
		

	public void setProcessId(String processId) { 
		this.processId = processId;
	}
	
	public String getProcessId() {
		return this.processId;
	}
	
			
		

	public void setProcessName(String processName) { 
		this.processName = processName;
	}
	
	public String getProcessName() {
		return this.processName;
	}
	

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
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
		KoalaAssignDetailVO other = (KoalaAssignDetailVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}