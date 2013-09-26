package org.openkoala.koala.widget; 

import java.io.Serializable;

public class SpringMVC implements Serializable{

	private static final long serialVersionUID = 1748685316375029528L;

	private String version;
	
	private boolean sample;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isSample() {
		return sample;
	}

	public void setSample(boolean sample) {
		this.sample = sample;
	}
	
	
}
