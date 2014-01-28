package org.openkoala.bpm.KoalaBPM.web.controller.organisation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.JobApplication;
import org.openkoala.organisation.domain.Job;

import com.dayatang.querychannel.support.Page;

/**
 * JobController单元测试
 * @author xmfang
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class JobControllerTest {
	
	@Mock
	private JobApplication jobApplication;

	@Mock
	private BaseApplication baseApplication;

	@InjectMocks
	private JobController jobController = new JobController();
	
	@Test
	public void testPagingQuery() {
		Page<Job> jobPage = new Page<Job>(1, 2, 10, generateJobs());
		
		when(jobApplication.pagingQueryJobs(new Job(), 1, 10)).thenReturn(jobPage);
		assertEquals(jobPage.getResult(), jobController.pagingQuery(1, 10, new Job()).get("Rows"));
	}

	@Test
	public void testQueryAllJobs() {
		List<Job> jobs = generateJobs();
		when(baseApplication.findAll(Job.class)).thenReturn(jobs);
		assertEquals(jobs, jobController.queryAllJobs().get("data"));
	}
	
	private List<Job> generateJobs() {
		List<Job> jobs = new ArrayList<Job>();
		Job job1 = new Job("总公司总经理", "JOB-XXXXX1");
		Job job2 = new Job("总公司副总经理", "JOB-XXXXX2");
		jobs.add(job1);
		jobs.add(job2);
		return jobs;
	}
	
	@Test
	public void testCreateJob() {
		Job job = new Job("总公司总经理", "JOB-XXXXX1");
		jobController.createJob(job);
		verify(baseApplication, only()).saveParty(job);
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenCreateJob() {
		Job job = new Job("总公司总经理", "JOB-XXXXX1");
		doThrow(new SnIsExistException()).when(baseApplication).saveParty(job);
		assertEquals("职务编码: " + job.getSn() + " 已被使用！", jobController.createJob(job).get("result"));
	}
	
	@Test
	public void testExceptionWhenCreateJob() {
		Job job = new Job("总公司总经理", "JOB-XXXXX1");
		doThrow(new RuntimeException()).when(baseApplication).saveParty(job);
		assertEquals("保存失败！", jobController.createJob(job).get("result"));
	}
	
	@Test
	public void testUpdateJob() {
		Job job = new Job("总公司总经理", "JOB-XXXXX1");
		jobController.updateJob(job);
		verify(baseApplication, only()).updateParty(job);
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenUpdateJob() {
		Job job = new Job("总公司总经理", "JOB-XXXXX1");
		doThrow(new SnIsExistException()).when(baseApplication).updateParty(job);
		assertEquals("职务编码: " + job.getSn() + " 已被使用！", jobController.updateJob(job).get("result"));
	}
	
	@Test
	public void testExceptionWhenUpdateJob() {
		Job job = new Job("总公司总经理", "JOB-XXXXX1");
		doThrow(new RuntimeException()).when(baseApplication).updateParty(job);
		assertEquals("修改失败！", jobController.updateJob(job).get("result"));
	}
	
	@Test
	public void testGet() {
		Job job = new Job("总公司总经理", "JOB-XXXXX1");
		Long jobId = 6L;
		job.setId(jobId);
		
		when(baseApplication.getEntity(Job.class, jobId)).thenReturn(job);
		assertEquals(job, jobController.get(jobId).get("data"));
	}
	
	@Test
	public void testTerminateEmployee() {
		Job job = new Job("总公司总经理", "JOB-XXXXX1");
		
		jobController.terminateJob(job);
		verify(baseApplication, only()).terminateParty(job);
	}
	
	@Test
	public void testTerminateJobs() {
		Job[] jobs = new Job[2];
		int i = 0;
		for (Job job : generateJobs()) {
			jobs[i] = job;
			i ++;
		}
		
		jobController.terminateJobs(jobs);
		verify(baseApplication, only()).terminateParties(new HashSet<Job>(Arrays.asList(jobs)));
	}
}
