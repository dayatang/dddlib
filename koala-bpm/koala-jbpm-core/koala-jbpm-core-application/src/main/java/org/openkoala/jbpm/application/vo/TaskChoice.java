package org.openkoala.jbpm.application.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 节点的抉择策略
 * @author lingen
 *
 */
@XmlRootElement()
public class TaskChoice implements Serializable {

	
	private static final long serialVersionUID = -779664629281934749L;

	/**
	 * 影响到的流程中的KEY值
	 */
	private String key;
	
	/**
	 * 值的类型
	 * Int
	 * Boolean
	 */
	private String valueType;
	
	/**
	 * 中文描述
	 */
	private String name;
	
	/**
	 * 填写的VALUE值
	 */
	private String value;
	

	public TaskChoice() {
		super();
	}


	public TaskChoice(String key, String valueType, String name, String value) {
		super();
		this.key = key;
		this.valueType = valueType;
		this.name = name;
		this.value = value;
	}

	

	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getValueType() {
		return valueType;
	}


	public void setValueType(String valueType) {
		this.valueType = valueType;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
}
