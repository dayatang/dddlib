package org.openkoala.gqc.core.domain;
/**
 * 数据源类型枚举
 *  
 * 作    者：xmfang(xinmin.fang@gmail.com)
 */
public enum DataSourceType {

	SYSTEM_DATA_SOURCE("系统数据源"),
	CUSTOM_DATA_SOURCE("自定义数据源");
	
	/**
	 * 描述
	 */
	private String description;
	
	public String getDescription() {
		return description;
	}
	
	private DataSourceType(String description) {
		this.description = description;
	}
	
}
