package org.openkoala.organisation.application.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.application.JobApplication;
import org.openkoala.organisation.application.impl.utils.OrganisationUtils;
import org.openkoala.organisation.domain.Job;

import com.dayatang.utils.DateUtils;

/**
 * 职务应用实现集成测试
 * @author xmfang
 *
 */
public class JobApplicationIntegrationTest extends AbstractIntegrationTest {
	
	private Job job1;
	private Job job2;
	
	@Inject
	private JobApplication jobApplication;
	
	@Before
	public void subSetup() {
		OrganisationUtils organisationUtils = new OrganisationUtils();
		Date date = DateUtils.date(2013, 1, 1);
		job1 = organisationUtils.createJob("总经理", "JOB-XXX1", date);
		job2 = organisationUtils.createJob("副总经理", "JOB-XXX2", date);
	}

	@Test
	public void testPagingQueryJobs() {
		List<Job> jobs1 = jobApplication.pagingQueryJobs(new Job(), 1, 1).getResult();
		assertEquals(1, jobs1.size());
		
		List<Job> jobs2 = jobApplication.pagingQueryJobs(new Job(), 1, 10).getResult();
		assertTrue(jobs2.contains(job1));
		assertTrue(jobs2.contains(job2));
	}
	
}
