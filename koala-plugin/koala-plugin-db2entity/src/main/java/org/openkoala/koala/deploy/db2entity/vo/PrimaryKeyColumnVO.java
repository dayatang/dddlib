package org.openkoala.koala.deploy.db2entity.vo;

/**
 * 主键VO
 * @author lingen
 *
 */@SuppressWarnings("serial")

public class PrimaryKeyColumnVO extends DBColumnVO {

	private int primarySeq;
	 
	private String pkName;
	 
	private boolean isAutoIncrement;

	public int getPrimarySeq() {
		return primarySeq;
	}

	public void setPrimarySeq(int primarySeq) {
		this.primarySeq = primarySeq;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}

	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}
	
}
