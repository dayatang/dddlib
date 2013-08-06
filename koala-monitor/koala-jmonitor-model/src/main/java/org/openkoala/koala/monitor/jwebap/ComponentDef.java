package org.openkoala.koala.monitor.jwebap;



public class ComponentDef extends PropertyStorage{

	private static final long serialVersionUID = -8073531458093863775L;

	/**
	 * Component的实现类，类似org.jwebap.http.HttpComponent
	 */
	private String type = null;
	
	/**
	 * 组件定义的名称
	 */
	private String name = null;
	
	private boolean active;//是否激活
	
	
	public ComponentDef() {
		
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}


	/**
	 * 判断component定义是否相等，componentName相等则相等，如果componentName为空则不相等
	 */
	public boolean equals(Object obj){
		
		if(!(obj instanceof ComponentDef)){
			return false;
		}
		ComponentDef def=(ComponentDef)obj;
		
		if(getType()==null){
			return false;
		}else if(getType().equals(def.getType())){
			return true;
		}

		return false;
	}
	
	/**
	 * 改写super.hashCode
	 */
	public int hashCode(){
		if(getType()==null){
			return super.hashCode();
		}else {
			return getType().hashCode();
		}
	}

	@Override
	public String toString() {
		return "ComponentDef[name=" + name + ", active="+ active + ",props= "+getProperties()+"]";
	}
	
	
}