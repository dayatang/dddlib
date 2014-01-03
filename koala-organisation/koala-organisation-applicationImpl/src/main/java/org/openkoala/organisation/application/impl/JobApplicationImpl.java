package org.openkoala.organisation.application.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.organisation.application.JobApplication;
import org.openkoala.organisation.domain.Job;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional(value="transactionManager_org")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "JobApplication")
@Remote
public class JobApplicationImpl implements JobApplication {

	
	private QueryChannelService queryChannel;
	
	private QueryChannelService getQueryChannelService(){
		if(queryChannel ==null){
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class,"queryChannel_org");
		}
		return queryChannel;
	}
	
	@Override
	public Page<Job> pagingQueryJobs(Job jobSearchExample, int currentPage, int pageSize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _job from Job _job where _job.createDate <= ? and _job.terminateDate > ?");
		Date now = new Date();
		conditionVals.add(now);
		conditionVals.add(now);

		if (!StringUtils.isBlank(jobSearchExample.getName())) {
			jpql.append(" and _job.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", jobSearchExample.getName()));
		}
		if (!StringUtils.isBlank(jobSearchExample.getSn())) {
			jpql.append(" and _job.sn like ?");
			conditionVals.add(MessageFormat.format("%{0}%", jobSearchExample.getSn()));
		}
		if (!StringUtils.isBlank(jobSearchExample.getDescription())) {
			jpql.append(" and _job.description like ?");
			conditionVals.add(MessageFormat.format("%{0}%", jobSearchExample.getSn()));
		}

		return getQueryChannelService().queryPagedResultByPageNo(jpql.toString(),conditionVals.toArray(), currentPage, pageSize);
	}

}
