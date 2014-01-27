package org.openkoala.koala.deploy.webservice.pojo;

import org.openkoala.koala.java.JavaManagerUtil;

import japa.parser.ast.body.MethodDeclaration;

/**
 * 代表一个类其中的某一个方法
 * @author lingen
 *
 */
public class WebServiceMethod {

	private String name;//方名
	
	private MethodDeclaration method;//方法

	public WebServiceMethod(MethodDeclaration method) {
		this.method = method;
		this.name = JavaManagerUtil.methodDescription(method);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MethodDeclaration getMethod() {
		return method;
	}

	public void setMethod(MethodDeclaration method) {
		this.method = method;
	}
	
}
