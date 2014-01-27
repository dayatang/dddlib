package org.openkoala.gqc.core.domain;

/**
 * 查询操作枚举
 * @author:xmfang(xinmin.fang@gmail.com)
 */
public enum QueryOperation {
	
	EQ("="),
	GE(">="),
	GT(">"),
	NE("!="),
	LE("<="),
	LT("<"),
	LIKE("like"),
	IN("in"),
	BETWEEN("between");
	
	/**
	 * 操作
	 */
	private String operator;
	
	private QueryOperation(String operator) {
		this.operator = operator;
	}
	
	public String getOperator() {
		return operator;
	}

}
