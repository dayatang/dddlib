package org.openkoala.organisation.domain;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.OrganizationHasPrincipalYetException;
import org.openkoala.organisation.TheJobHasPostAccountabilityException;
import org.openkoala.organisation.utils.OrganisationUtils;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.utils.DateUtils;

/**
 * 岗位集成测试
 * @author xmfang
 *
 */
@TransactionConfiguration(transactionManager = "transactionManager_org", defaultRollback = true)
public class PostIntegrationTest extends AbstractIntegrationTest {
	
	private Company company1;
	private Company company2;
	private Department department;
	private Job job1;
	private Job job2;
	private Post post1;
	private Post post2;
	private Employee employee;
	private Date date = DateUtils.date(2013, 1, 1);
	private Date now = new Date();
	
	private OrganisationUtils organisationUtils = new OrganisationUtils();
	
	@Before
	public void subSetup() {
		company1 = organisationUtils.createCompany("总公司", "JG-XXX", date);
		company2 = organisationUtils.createCompany("华南分公司", "JG-XXX2", company1, date);
		department = organisationUtils.createDepartment("财务部", "JG-XXX3", company2, date);
		
		job1 = organisationUtils.createJob("会计", "JOB-XXX1", date);
		job2 = organisationUtils.createJob("分公司总经理", "JOB-XXX2", date);
		post1 = organisationUtils.createPost("会计", "POST-XXX1", job1, department, date);
		post2 = organisationUtils.createPost("广州分公司总经理", "POST-XXX2", job2, company2, date);
		employee = organisationUtils.createEmployee("张三", "XXXXXXXX", "EMP-XXX", post2, date);
		new EmployeePostHolding(post1, employee, false, date).save();
	}

	@Test
	public void testFindByJob() {
		List<Post> posts = Post.findByJob(job1, now);
		assertEquals(1, posts.size());
		assertTrue(posts.contains(post1));
	}
	
	@Test
	public void testFindByOrganization() {
		List<Post> posts = Post.findByOrganization(department, now);
		assertEquals(1, posts.size());
		assertTrue(posts.contains(post1));
	}
	
	@Test
	public void testGetEmployees() {
		Set<Employee> employees = post1.getEmployees(now);
		assertEquals(1, employees.size());
		assertTrue(employees.contains(employee));
	}
	
	@Test
	public void testHasPrincipalPostOfOrganization() {
		post2.setOrganizationPrincipal(true);
		assertTrue(Post.hasPrincipalPostOfOrganization(company2, now));
	}
	
	@Test(expected = OrganizationHasPrincipalYetException.class)
	public void testSaveMultiPrincipalPost() {
		post2.setOrganizationPrincipal(true);
		Post post3 = new Post("xxx", "POST-XXX3", job1, company2);
		post3.setOrganizationPrincipal(true);
		post3.save();
	}
}
