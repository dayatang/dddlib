package org.openkoala.organisation.domain;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.TheJobHasPostAccountabilityException;
import org.openkoala.organisation.utils.OrganisationUtils;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.utils.DateUtils;

/**
 * 职务集成测试
 * @author xmfang
 *
 */
@TransactionConfiguration(transactionManager = "transactionManager_org", defaultRollback = true)
public class JobIntegrationTest extends AbstractIntegrationTest {
	
	private Job job;
	
	private OrganisationUtils organisationUtils = new OrganisationUtils();
	
	@Before
	public void subSetup() {
		Date date = DateUtils.date(2013, 1, 1);
		Company company = organisationUtils.createCompany("总公司", "JG-XXX", date);
		job = organisationUtils.createJob("总经理", "JOB-XXX1", date);
		organisationUtils.createPost("总经理", "POST-XXX1", job, company, date);
	}

	@Test(expected = TheJobHasPostAccountabilityException.class)
	public void testTerminateJobWithPost() {
		job.terminate(new Date());
	}
	
}
