package org.openkoala.koala.monitor.def;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class NodeDef implements Serializable, Cloneable{

	private static final long serialVersionUID = -3451267566331657691L;
	/**
	 * plugin定义引用
	 * 
	 * @see PluginDefRef
	 */
    private String id;
	private String uri;
	private String name; 
	private int maxCacheSize = 5000;//最大缓存数
	private int cacheExpireTime = 15;//缓存超时时间（单位：分钟）
	
	private List<ComponentDef> components = null;
	
	private List<TaskDef> tasks = null;
	
	private DataPolicyDef dataPolicy;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getMaxCacheSize() {
		return maxCacheSize;
	}

	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}

	public int getCacheExpireTime() {
		return cacheExpireTime;
	}

	public void setCacheExpireTime(int cacheExpireTime) {
		this.cacheExpireTime = cacheExpireTime;
	}

	public NodeDef() {
		components = new ArrayList<ComponentDef>();
	}

	public List<ComponentDef> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentDef> components) {
		this.components = components;
	}
	
	public DataPolicyDef getDataPolicy() {
		return dataPolicy;
	}

	public void setDataPolicy(DataPolicyDef dataPolicy) {
		this.dataPolicy = dataPolicy;
	}

	/**
	 * 增加Component定义
	 * @param name
	 * @param component
	 */
	public void addComponentDef(ComponentDef component) {
		components.add(component);
	}

	/**
	 * 获得Component定义
	 * @param name
	 * @return
	 */
	public ComponentDef getComponentDef(String type) {
		for (ComponentDef c : components) {
			if(c.getType().equals(type))return c;
		}
		return null;
	}


	public List<TaskDef> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskDef> tasks) {
		this.tasks = tasks;
	}

	public Object clone(){
		NodeDef defCopy=new NodeDef();
		
		return defCopy;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append("\n\tid=" + id );
		sb.append("\n\tmaxCacheSize=" + maxCacheSize);
		sb.append("\n\tcacheExpireTime=" + cacheExpireTime);
		sb.append("\n\tdataPolicy=" + dataPolicy.getType());
		sb.append("\n\tComponent{");
		for (ComponentDef c : components) {
			sb.append("\n\t\t"+c);
		}
		sb.append("\n\t}");
		sb.append("\n]");
		return sb.toString();
	}
	
	
}
