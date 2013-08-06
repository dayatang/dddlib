package org.openkoala.koala.deploy.ejb.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地接口定义POJO
 * @author lingen
 *
 */
public class LocalInterface implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4861335702192847467L;
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
