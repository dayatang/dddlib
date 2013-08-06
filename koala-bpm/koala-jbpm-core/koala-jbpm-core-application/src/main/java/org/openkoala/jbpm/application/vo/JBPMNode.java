package org.openkoala.jbpm.application.vo;

import java.io.Serializable;
/**
 * 一个流程的所有可跳转的节点集合
 * @author lingen
 *
 */
public class JBPMNode implements Serializable {
	
	private static final long serialVersionUID = -4085827415097234968L;

	private  long id;//node id
	
	private String name;//节点名称

	public JBPMNode(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public JBPMNode() {
		super();
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	@Override
	public String toString() {
		return "JBPMNode [id=" + id + ", name=" + name + "]";
	}
	
}
