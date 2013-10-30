package org.openkoala.jbpm.application.vo;


import java.io.Serializable;

public class KoalaJbpmVariableVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7710368419492505035L;



	private Long id;

		

	private String scope;
	
		

	private String value;
	
		

	private String type;
	
		

	private String key;
	
	
	private String scopeType;
	
			
		

	public void setScope(String scope) { 
		this.scope = scope;
	}
	
	public String getScope() {
		return this.scope;
	}
	
			
		

	public void setValue(String value) { 
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
			
		

	public void setType(String type) { 
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
			
		

	public void setKey(String key) { 
		this.key = key;
	}
	
	public String getKey() {
		return this.key;
	}
	

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	

    public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
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
		KoalaJbpmVariableVO other = (KoalaJbpmVariableVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}