package org.openkoala.koala.deploy.ejb.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 一个实现对象的定义
 * @author lingen
 *
 */
public class ImplObj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9169477562032529686L;
	
	private String implModuleName;
	
	private String interfaceModuleName;

	//实现类的在项目中的位置名  如 org/openkoala/inter/Impl.java
	private String name;
	
	//接口类的项目中的位置名 org/openkoala/application/Interface.java
	private String interfaceName;
	
	private List<String> methods;
	
	private LocalInterface localInterface;
	
	private RemoteInterface remoteInterface;

	public ImplObj(String implModuleName,String name, String interfaceModuleName,String interfaceName, List<String> methods) {
		super();
		this.implModuleName = implModuleName;
		this.interfaceModuleName = interfaceModuleName;
		this.name = name;
		this.interfaceName = interfaceName;
		this.methods = methods;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public LocalInterface getLocalInterface() {
		return localInterface;
	}

	public void setLocalInterface(LocalInterface localInterface) {
		this.localInterface = localInterface;
	}

	public RemoteInterface getRemoteInterface() {
		return remoteInterface;
	}

	public void setRemoteInterface(RemoteInterface remoteInterface) {
		this.remoteInterface = remoteInterface;
	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
	public String getApplicationName(){
		String applicationName = this.interfaceName.substring(interfaceName.lastIndexOf("/")+1,interfaceName.lastIndexOf(".java"));
		return applicationName;
	}

	public String getFullApplicationName(){
		String applicationName = this.interfaceName.substring(0,interfaceName.lastIndexOf(".java")).replaceAll("/", ".");
		return applicationName;
	}

	public String getImplModuleName() {
		return implModuleName;
	}

	public void setImplModuleName(String implModuleName) {
		this.implModuleName = implModuleName;
	}

	public String getInterfaceModuleName() {
		return interfaceModuleName;
	}

	public void setInterfaceModuleName(String interfaceModuleName) {
		this.interfaceModuleName = interfaceModuleName;
	}
	
}
