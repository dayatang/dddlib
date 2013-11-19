/*
 * Copyright (c) Koala 2012-2014 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.koala.monitor.model;

import java.io.Serializable;
import java.util.Date;

import org.openkoala.koala.monitor.common.KoalaDateUtils;

/**
 * 功能描述：<br />
 * 
 * 创建日期：2013-7-3 上午9:44:44 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class ScheduleConfVo implements Serializable {

	private static final long serialVersionUID = -6039862341237713870L;

	private String triggerName;

	private String schedulerName;

	private boolean running;// 是否运行中

	private boolean active;// 是否启用
	private String activeAsString;//

	private String cronExpr; //执行间隔表达式

	private Date lastBeginRunTime;// 上一次运行开始时间
	
	private int interval;//执行间隔

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}


	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getActiveAsString() {
		return activeAsString;
	}

	public void setActiveAsString(String activeAsString) {
		this.activeAsString = activeAsString;
	}

	public String getCronExpr() {
		if(cronExpr == null || "".equals(cronExpr.trim())){
			if(interval<=0)return null;
			//0 0/2 * * * ?
			if(interval<60){
				cronExpr = "0/"+interval+" * * * * ?";
			}else if(interval>60 && interval < 3600){
				cronExpr = "0 0/"+(interval/60)+" * * * ?";
			}
		}
		return cronExpr;
	}

	public void setCronExpr(String cronExpr) {
		this.cronExpr = cronExpr;
	}

	public String getLastBeginRunTime() {
		return KoalaDateUtils.format(lastBeginRunTime);
	}

	public void setLastBeginRunTime(Date lastBeginRunTime) {
		this.lastBeginRunTime = lastBeginRunTime;
	}

	public int getInterval() {
		if(interval == 0 && cronExpr != null){
			//0 0/2 * * * ?
			//0/2 * * * * ?
			String[] splits = cronExpr.split("\\s+");
			if(splits[0].contains("0/")){
				interval = Integer.parseInt(splits[0].replace("0/", ""));
			}else if(splits[1].contains("0/")){
				interval = Integer.parseInt(splits[1].replace("0/", "")) * 60;
			}
		}
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
}
