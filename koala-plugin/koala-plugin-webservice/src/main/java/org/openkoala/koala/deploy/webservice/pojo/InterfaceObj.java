package org.openkoala.koala.deploy.webservice.pojo;

import java.util.List;

/**
 * 需要发布成WebService的接口
 * @author lingen
 *
 */
public class InterfaceObj {

	private String name;
	
	private String qualifiedName;
	
	private List<WebServiceMethod> methods;
	
	private List<String> selectedMethods;
	
	private String implName;
	
	private String qualifiedImplName;
	
	public InterfaceObj(String name, String qualifiedName, String implName,
			String qualifiedImplName,List<WebServiceMethod> methods) {
		super();
		this.name = name;
		this.qualifiedName = qualifiedName;
		this.implName = implName;
		this.methods = methods;
	}

	public InterfaceObj() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}
	
	public List<WebServiceMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<WebServiceMethod> methods) {
		this.methods = methods;
	}

	public String getImplName() {
		return implName;
	}

	public void setImplName(String implName) {
		this.implName = implName;
	}

	public String getQualifiedImplName() {
		return qualifiedImplName;
	}

	public void setQualifiedImplName(String qualifiedImplName) {
		this.qualifiedImplName = qualifiedImplName;
	}

	public String getSimpleName(){
		String simpleName = this.name;
		simpleName = name.substring(name.lastIndexOf("/")+1,name.lastIndexOf(".java"));
		return simpleName;
	}
	
	public String getImplAppName(){
		String implAppName = this.implName;
		implAppName = this.implName.substring(0,implName.lastIndexOf(".java")).replaceAll("/", "\\.");
		return implAppName;
	}

	public List<String> getSelectedMethods() {
		return selectedMethods;
	}

	public void setSelectedMethods(List<String> selectedMethods) {
		this.selectedMethods = selectedMethods;
	}
	
	public String getApplicationName(){
		String applicationName = this.name.substring(name.lastIndexOf("/")+1,name.lastIndexOf(".java"));
		return applicationName;
	}

	public String getFullApplicationName(){
		String applicationName = this.name.substring(0,name.lastIndexOf(".java")).replaceAll("/", ".");
		return applicationName;
	} 
}
