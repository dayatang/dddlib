package org.openkoala.koala.widget;

import java.io.Serializable;

import org.openkoala.koala.annotation.ObjectFunctionCreate;

@ObjectFunctionCreate
public class Security implements Serializable {

	private static final long serialVersionUID = 9195935564812042299L;

	// @ 后续提供 EJB,LDAP的实现
	private String connType = "JDBC";

	// 提供三种首页模板的选择
	private String template;
	
	// 缓存类型,提供ehCache及memcached两种选择方式
	private String cacheType;
	
	public Security() {
		super();
	}

	public Security(String connType, String template, String cacheType) {
		super();
		this.connType = connType;
		this.template = template;
		this.cacheType = cacheType;
	}

	public String getConnType() {
		return connType;
	}

	public void setConnType(String connType) {
		this.connType = connType;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getCacheType() {
		return cacheType;
	}

	public void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}
	
}
