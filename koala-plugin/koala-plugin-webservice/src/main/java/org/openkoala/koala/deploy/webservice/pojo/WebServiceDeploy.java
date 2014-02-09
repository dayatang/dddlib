package org.openkoala.koala.deploy.webservice.pojo;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.pojo.MavenProject;

/**
 * WebService的POJO类
 * @author lingen
 *
 */
public class WebServiceDeploy {

	/**
	 * 项目路径
	 */
	private String path;
	
	private MavenProject project;
	
	private String version = "v1";
	
	/**
	 * 需要公开的接口列表
	 */
	private List<InterfaceObj> interfaces;
	
	/**
	 * 需要公开的值对象列表
	 */
	private List<ValueObj> valueObjs;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<InterfaceObj> getInterfaces() {
		if(interfaces==null)interfaces = new ArrayList<InterfaceObj>();
		return interfaces;
	}

	public void setInterfaces(List<InterfaceObj> interfaces) {
		this.interfaces = interfaces;
	}

	public List<ValueObj> getValueObjs() {
		if(valueObjs==null)valueObjs = new ArrayList<ValueObj>();
		return valueObjs;
	}

	public void setValueObjs(List<ValueObj> valueObjs) {
		this.valueObjs = valueObjs;
	}

	public MavenProject getProject() {
		return project;
	}

	public void setProject(MavenProject project) {
		this.project = project;
	}
	
}
