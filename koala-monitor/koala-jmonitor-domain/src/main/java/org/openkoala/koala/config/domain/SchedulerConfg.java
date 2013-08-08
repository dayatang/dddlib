/*
 * Copyright (c) openkoala 2011 All Rights Reserved
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
package org.openkoala.koala.config.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openkoala.koala.base.KmBaseLegacyEntity;


/**
 * 类    名：NpsScheduler.java<br />
 *   
 * 功能描述：缓存实体	<br />
 *  
 * 创建日期：2012-11-22下午04:44:06  <br />   
 * 
 * 版本信息：v 1.0<br />
 * 
 * 版权信息：Copyright (c) 2011 Csair All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:jiangwei@openkoala.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@Entity
@Table(name = "K_M_SCHEDULER_CONF")
public class SchedulerConfg extends KmBaseLegacyEntity{

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "TRIGGER_NAME", length = 64)
    private String triggerName;
    
    @Column(name = "SCHE_NAME", length = 64)
    private String schedulerName;

    
    @Column(name = "IS_RUNNING")
    private boolean running = false;//是否运行中
   
    @Column(name = "IS_ACTIVE")
    private boolean active = true;//是否启用
    
    @Column(name = "CRON_EXPR",length = 32)
    private String cronExpr; //
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_BEGIN_RUN_TIME")
    private Date lastBeginRunTime;//上一次运行开始时间
    
    public SchedulerConfg() {}
    
    /**
     * @param schedulerName
     * @param remark
     * @param cronExpr
     */
    public SchedulerConfg(String triggerName, String schedulerName, String cronExpr) {
        super();
        this.schedulerName = schedulerName;
        this.triggerName = triggerName;
        this.cronExpr = cronExpr;
    }


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

	public String getCronExpr() {
        return cronExpr;
    }

    public void setCronExpr(String cronExpr) {
        this.cronExpr = cronExpr;
    }

    public Date getLastBeginRunTime() {
        return lastBeginRunTime;
    }

    public void setLastBeginRunTime(Date lastBeginRunTime) {
        this.lastBeginRunTime = lastBeginRunTime;
    }

    public String toString(){
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
    }


    /*
     *@see com.dayatang.domain.Entity#getId()
     */
    @Override
    public String getId() {
        return schedulerName;
    }

	@Override
	public boolean existed() {
		return false;
	}

	@Override
	public boolean notExisted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existed(String propertyName, Object propertyValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((triggerName == null) ? 0 : triggerName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedulerConfg other = (SchedulerConfg) obj;
		if (triggerName == null) {
			if (other.triggerName != null)
				return false;
		} else if (!triggerName.equals(other.triggerName))
			return false;
		return true;
	}

	
}
