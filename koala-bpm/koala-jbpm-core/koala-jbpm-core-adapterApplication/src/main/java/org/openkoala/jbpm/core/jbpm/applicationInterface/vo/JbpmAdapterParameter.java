package org.openkoala.jbpm.core.jbpm.applicationInterface.vo;

import java.io.Serializable;
import java.util.List;

public class JbpmAdapterParameter  implements Serializable{
	
	private static final long serialVersionUID = -4630862934376383354L;
	
	private String name;
	
	private byte[] params;
	
	private List<String> strings;

	public JbpmAdapterParameter(String name, byte[] params,
			List<String> strings) {
		super();
		this.name = name;
		this.params = new byte[params.length];
		System.arraycopy(params, 0, this.params, 0, params.length);
		
		this.strings = strings;
	}

	public JbpmAdapterParameter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public byte[] getParams() {
		return params;
	}

	public void setParams(byte[] params) {
		this.params = new byte[params.length];
		System.arraycopy(params, 0, this.params, 0, params.length);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getStrings() {
		return strings;
	}

	public void setStrings(List<String> strings) {
		this.strings = strings;
	}
	
}
