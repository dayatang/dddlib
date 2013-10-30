package org.openkoala.jbpm.application.vo;


import java.io.Serializable;

public class PublishURLVO implements Serializable {

			
	/**
	 * 
	 */
	private static final long serialVersionUID = 3632898534476369602L;


	private Long id;
	
				
	private String name;
	
				
	private String url;
	
				
		
	public void setId(Long id) { 
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	
				
		
	public void setName(String name) { 
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
				
		
	public void setUrl(String url) { 
		this.url = url;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	
}