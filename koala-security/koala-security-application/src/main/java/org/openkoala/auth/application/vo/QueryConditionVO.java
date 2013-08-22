package org.openkoala.auth.application.vo;

import java.io.Serializable;
import java.util.List;

public class QueryConditionVO implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -5347704959625517350L;
	public static final int EQUALS = 1;
	public static final int GREATER = 2;
	public static final int GREATER_THAN = 3;
	public static final int LESS = 4;
	public static final int LESS_THAN = 5;
	public static final int LIKE = 6;

	private String objectName;
	private List<QueryItemVO> items;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public List<QueryItemVO> getItems() {
		return items;
	}

	public void setItems(List<QueryItemVO> items) {
		this.items = items;
	}

	public static String genOperatorStirng(int type) {
		switch (type) {
		case QueryConditionVO.EQUALS:
			return "=";
		case QueryConditionVO.GREATER:
			return ">";
		case QueryConditionVO.GREATER_THAN:
			return ">=";
		case QueryConditionVO.LESS:
			return "<";
		case QueryConditionVO.LESS_THAN:
			return "<=";
		case QueryConditionVO.LIKE:
			return "like";
		default:
			return "";
		}
	}

}
