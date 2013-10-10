package org.openkoala.auth.application.vo;

import java.util.Date;

public class AccountabilityVO extends TimeIntervalEntityVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -930168703600551176L;
	
	private PartyVO commissioner;
	private PartyVO responsible;
	private Date scheduledAbolishDate;
	private String remark;

	public PartyVO getCommissioner() {
		return commissioner;
	}

	public void setCommissioner(PartyVO commissioner) {
		this.commissioner = commissioner;
	}

	public PartyVO getResponsible() {
		return responsible;
	}

	public void setResponsible(PartyVO responsible) {
		this.responsible = responsible;
	}

	public Date getScheduledAbolishDate() {
		return scheduledAbolishDate;
	}

	public void setScheduledAbolishDate(Date scheduledAbolishDate) {
		this.scheduledAbolishDate = scheduledAbolishDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
