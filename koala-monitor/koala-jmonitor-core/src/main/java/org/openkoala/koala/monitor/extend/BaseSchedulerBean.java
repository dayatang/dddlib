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
package org.openkoala.koala.monitor.extend;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.openkoala.koala.config.domain.SchedulerConfg;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.InstanceFactory;

/**
 * 类    名：BaseSchedulerBean.java<br />
 *   
 * 功能描述：定时任务基类  <br />
 *  
 * 创建日期：2012-2-13上午11:04:13  <br />   
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
public abstract class BaseSchedulerBean {
    private static final Logger logger = LoggerFactory.getLogger(BaseSchedulerBean.class);

    protected String triggerName;//

    protected String schedulerName;//

    protected String cronExpression;

    protected Scheduler scheduler;

    @Inject
    @Qualifier(value="km_transactionTemplate")
    protected TransactionTemplate transactionTemplate;
    
    @Inject
    @Qualifier(value="km_repository")
    protected EntityRepository repository;

    protected Scheduler getScheduler() {
        if (scheduler == null)
            scheduler = InstanceFactory.getInstance(Scheduler.class);
        return scheduler;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public void execute() {
        String latestConExpr = null;
        boolean runing = false;
        try {
            latestConExpr = checkExecutable();
            if (latestConExpr == null)
                return;
            updateExecutable(true);
            runing = true;
            //执行
            doJob();
            //检查数据库是否更新了执行策略
            checkConExpr(latestConExpr);
        } catch (Exception e) {
            logger.error(schedulerName + "定时任务[" + triggerName + "]运行错误", e);
        } finally {
            if (runing) {
                updateExecutable(false);
            }
        }
    }

    public void checkConExpr(String latestConExpr) {
        try {
            //check cronExpr valid
            CronTriggerBean trigger = (CronTriggerBean) getScheduler().getTrigger(triggerName, Scheduler.DEFAULT_GROUP);
            String originConExpression = trigger.getCronExpression();
            //判断任务时间是否更新过
            if (!originConExpression.equalsIgnoreCase(latestConExpr)) {
                trigger.setCronExpression(latestConExpr);
                getScheduler().rescheduleJob(triggerName, Scheduler.DEFAULT_GROUP, trigger);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 检测当前是否可用(确保当前及其集群环境仅有一个线程在运行该任务)<br>
     * generate by: vakin jiang
     *                    at 2012-2-13
     * @return
     */
    protected String checkExecutable() {
        try {
            SchedulerConfg scheduler = repository.get(SchedulerConfg.class, triggerName);
            if (scheduler == null)
                return cronExpression;
            if (!scheduler.isActive()) {
                if (logger.isDebugEnabled())
                    logger.debug(schedulerName + "定时任务[{}]已禁用,终止执行", triggerName);
                return null;
            }
            if (scheduler.isRunning()) {
                int excInterval = getExcInterval();
                long actualInterval = getDiffMinutes(scheduler.getLastBeginRunTime(), new Date());
                if (actualInterval > excInterval) {
                    if (logger.isDebugEnabled())
                        logger.debug(schedulerName + "定时任务[{}]计划执行间隔时间[" + excInterval + "]分钟,已经[" + actualInterval
                                + "]分钟未执行，重新开始执行", triggerName);
                    cronExpression = scheduler.getCronExpr();
                    return cronExpression;
                }
                if (logger.isDebugEnabled())
                    logger.debug(schedulerName + "定时任务[{}]正在执行,终止当前线程任务", triggerName);
                return null;
            }
            cronExpression = scheduler.getCronExpr();
        } catch (Exception e) {}
        return cronExpression;
    }

    /**
     * 更新运行状态<br>
     * generate by: vakin jiang
     *                    at 2012-2-13
     * @param runing
     */
    @Transactional
    protected void updateExecutable(final boolean runing) {
    	transactionTemplate.execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				String hql = "";
		        Object[] params = null;
		        if (runing) {
		            hql = "update SchedulerConfg set running = ?,lastBeginRunTime = ? where triggerName = ?";
		            params = new Object[] {true, new Date(), triggerName };
		        } else {
		            hql = "update SchedulerConfg set running = ? where triggerName = ?";
		            params = new Object[] {false, triggerName };
		        }
		        repository.executeUpdate(hql, params);
				return null;
			}
		});
    }


    public void resumeTrigger() {
        try {
            getScheduler().resumeTriggerGroup(Scheduler.DEFAULT_GROUP);
            logger.info("当前运行状态：" + getScheduler().isStarted());
        } catch (SchedulerException e) {
            throw new RuntimeException("重新运行定时任务失败，异常信息：" + e.getMessage());
        }
    }

    /**
     * 解析执行间隔时间<br>
     * generate by: vakin jiang
     *                    at 2012-12-6
     * @return
     */
    private int getExcInterval() {
        //
        return 60;
    }


    @PostConstruct
    public void onStart() {
        if(StringUtils.isBlank(triggerName))return;
        transactionTemplate.execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				SchedulerConfg scheduler = repository.get(SchedulerConfg.class, triggerName);
		        if (scheduler == null) {
		            scheduler = new SchedulerConfg(triggerName, schedulerName, cronExpression);
		        }
		        scheduler.setRunning(false);
				repository.save(scheduler);
				return null;
			}
		});
        
        logger.info("初始化" + schedulerName + "定时任务[{}]OK", triggerName);
    }

    @PreDestroy
    public void onStop() {
        updateExecutable(false);
    }
    
    
    /**
	 * 比较两个时间相差多少分钟
	 * */
	public static long getDiffMinutes(Date d1, Date d2) {
		long between = Math.abs((d2.getTime() - d1.getTime()) / 1000);
		long min = between / 60;// 转换为分
		return min;
	}

    public abstract void doJob() throws Exception;

}
