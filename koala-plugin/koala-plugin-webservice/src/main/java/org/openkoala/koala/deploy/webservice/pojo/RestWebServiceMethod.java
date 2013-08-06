package org.openkoala.koala.deploy.webservice.pojo;

import java.util.Set;

import japa.parser.ast.body.MethodDeclaration;

public class RestWebServiceMethod extends WebServiceMethod {
	
	private WebServiceType type;//方法类型
	
	private String uriPath;
	
	private ParamType paramType;

	private Set<MediaType> ProducesMediaTypes;

	private Set<MediaType> consumesMediaTypes;
	
	public RestWebServiceMethod(MethodDeclaration method, WebServiceType type) {
		super(method);
		this.type = type;
	}

	public WebServiceType getType() {
		return type;
	}

	public void setType(WebServiceType type) {
		this.type = type;
	}

	public String getUriPath() {
		return uriPath;
	}

	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}

	public ParamType getParamType() {
		return paramType;
	}

	public void setParamType(ParamType paramType) {
		this.paramType = paramType;
	}

	public Set<MediaType> getProducesMediaTypes() {
		return ProducesMediaTypes;
	}

	public void setProducesMediaTypes(Set<MediaType> producesMediaTypes) {
		ProducesMediaTypes = producesMediaTypes;
	}

	public Set<MediaType> getConsumesMediaTypes() {
		return consumesMediaTypes;
	}

	public void setConsumesMediaTypes(Set<MediaType> consumesMediaTypes) {
		this.consumesMediaTypes = consumesMediaTypes;
	}

}
