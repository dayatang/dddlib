package org.openkoala.businesslog.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openkoala.koala.springmvc.JsonDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


public class DefaultBusinessLogDTO implements Serializable {

	private Long id;

		
	private String category;
	
		
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date time;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date timeEnd;
		
	private String log;
	
		
	private String user;
	
		
	private String ip;
	
			
		

	public void setCategory(String category) { 
		this.category = category;
	}

	public String getCategory() {
		return this.category;
	}
	
			
		

	public void setTime(Date time) { 
		this.time = time;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getTime() {
		return this.time;
	}
	
	public void setTimeEnd(Date timeEnd) { 
		this.timeEnd = timeEnd;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getTimeEnd() {
		return this.timeEnd;
	}
			
		

	public void setLog(String log) { 
		this.log = log;
	}

	public String getLog() {
		return this.log;
	}
	
			
		

	public void setUser(String user) { 
		this.user = user;
	}

	public String getUser() {
		return this.user;
	}
	
			
		

	public void setIp(String ip) { 
		this.ip = ip;
	}

	public String getIp() {
		return this.ip;
	}
	

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultBusinessLogDTO other = (DefaultBusinessLogDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}