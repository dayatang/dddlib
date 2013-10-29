package org.openkoala.organisation.application;

import org.openkoala.organisation.domain.Job;

import com.dayatang.querychannel.support.Page;

/**
 * 职务应用层接口
 * @author xmfang
 *
 */
public interface JobApplication {

	/**
	 * 分页查询职务信息
	 * @param jobSearchExample
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Page<Job> pagingQueryJobs(Job jobSearchExample, int currentPage, int pageSize);
	
}
