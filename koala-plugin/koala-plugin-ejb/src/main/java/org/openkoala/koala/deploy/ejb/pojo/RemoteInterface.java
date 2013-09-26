package org.openkoala.koala.deploy.ejb.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 远程接口定义
 * @author lingen
 *
 */
public class RemoteInterface implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5509769767748601080L;
	private List<String> methods;

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}
	
	public void addMethod(String method){
		if(this.methods==null)this.methods = new ArrayList<String>();
		methods.add(method);
	}
}
