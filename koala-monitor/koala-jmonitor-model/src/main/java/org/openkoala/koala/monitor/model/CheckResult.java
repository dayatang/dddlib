package org.openkoala.koala.monitor.model;

import java.io.Serializable;


/**
 * 服务检查结果
 * @author vakinge
 *
 */
public class CheckResult implements Serializable {

	private static final long serialVersionUID = 5467764276552272583L;
	
	private boolean success;
	
	//服务本身是否可用
	private boolean unavailable = true;
	
	private long timeConsuming;//耗时时间（单位：毫秒）
	
	private String details;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isUnavailable() {
		return unavailable;
	}

	public void setUnavailable(boolean unavailable) {
		this.unavailable = unavailable;
	}

	public long getTimeConsuming() {
		return timeConsuming;
	}

	public void setTimeConsuming(long timeConsuming) {
		this.timeConsuming = timeConsuming;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
