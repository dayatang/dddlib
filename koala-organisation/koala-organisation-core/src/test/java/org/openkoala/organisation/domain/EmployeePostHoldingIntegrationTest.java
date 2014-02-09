package org.openkoala.organisation.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.utils.OrganisationUtils;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.utils.DateUtils;

/**
 * 员工集成测试
 * @author xmfang
 *
 */
@TransactionConfiguration(transactionManager = "transactionManager_org", defaultRollback = true)
public class EmployeePostHoldingIntegrationTest extends AbstractIntegrationTest {
	
	private Company company1;
	private Company company2;
	private Department department;
	private Job job1;
	private Job job2;
	private Post post1;
	private Post post2;
	private Employee employee;
	private EmployeePostHolding employeePostHolding;
	private Date date = DateUtils.date(2013, 1, 1);
	private Date now = new Date();
	
	private OrganisationUtils organisationUtils = new OrganisationUtils();
	
	@Before
	public void subSetup() {
		company1 = organisationUtils.createCompany("总公司", "JG-XXX", date);
		company2 = organisationUtils.createCompany("华南分公司", "JG-XXX2", company1, date);
		department = organisationUtils.createDepartment("财务部", "JG-XXX3", company2, date);
		
		job1 = organisationUtils.createJob("分公司部门总监", "JOB-XXX1", date);
		job2 = organisationUtils.createJob("分公司副总经理", "JOB-XXX2", date);
		post1 = organisationUtils.createPost("广州分公司财务总监", "POST-XXX1", job1, department, date);
		post2 = organisationUtils.createPost("广州分公司副总经理", "POST-XXX2", job2, company2, date);
		employee = organisationUtils.createEmployee("张三", "XXXXXXXX", "EMP-XXX", post2, date);
		employeePostHolding = new EmployeePostHolding(post1, employee, false, date);
		employeePostHolding.save();
	}

	@Test
	public void testFindPostsOfEmployee() {
		List<Post> posts = EmployeePostHolding.findPostsOfEmployee(employee, now);
		assertEquals(2, posts.size());
		assertTrue(posts.contains(post1));
		assertTrue(posts.contains(post2));
	}
	
	@Test
	public void testGetPrincipalPostByEmployee() {
		assertEquals(post2, EmployeePostHolding.getPrincipalPostByEmployee(employee, now));
	}
	
	@Test
	public void getFindAdditionalPostsByEmployee() {
		List<Post> additionalPosts = EmployeePostHolding.findAdditionalPostsByEmployee(employee, now);
		assertEquals(1, additionalPosts.size());
		assertTrue(additionalPosts.contains(post1));
	}
	
	@Test
	public void testFindEmployeesOfPost() {
		List<Employee> employees = EmployeePostHolding.findEmployeesOfPost(post1, now);
		assertEquals(1, employees.size());
		assertTrue(employees.contains(employee));
	}
	
	@Test
	public void testFindEmployeesOfJob() {
		List<Employee> employees = EmployeePostHolding.findEmployeesOfJob(job1, now);
		assertEquals(1, employees.size());
		assertTrue(employees.contains(employee));
	}
	
	@Test
	public void testGetByPostAndEmployee() {
		assertEquals(employeePostHolding, EmployeePostHolding.getByPostAndEmployee(post1, employee, now));
	}
	
	@Test
	public void testGetByEmployee() {
		List<EmployeePostHolding> holdings = EmployeePostHolding.getByEmployee(employee, now);
		assertEquals(2, holdings.size());
		assertTrue(holdings.contains(employeePostHolding));
	}
	
	@Test
	public void testGetByPost() {
		List<EmployeePostHolding> holdings = EmployeePostHolding.getByPost(post1, now);
		assertEquals(1, holdings.size());
		assertTrue(holdings.contains(employeePostHolding));
	}
	
	@Test
	public void testGetManagerOfOrganization() {
		post1.setOrganizationPrincipal(true);
		assertEquals(employee, EmployeePostHolding.getManagerOfOrganization(department, now).get(0));
	}
	
	@Test
	public void testGetEmployeeCountOfPost() {
		assertEquals(new Long(1), EmployeePostHolding.getEmployeeCountOfPost(post1, now));
	}
	
	@Test
	public void testFindEmployeesOfOrganization() {
		organisationUtils.createEmployee("李四", "XXXXXXXX2", "EMP-XXX2", post2, date);
		assertEquals(2, EmployeePostHolding.findEmployeesOfOrganization(company1, now).size());
		assertEquals(2, EmployeePostHolding.findEmployeesOfOrganization(company2, now).size());
		assertEquals(1, EmployeePostHolding.findEmployeesOfOrganization(department, now).size());
	}
	
	@Test
	public void testGetOrganizationOfEmployee() {
		assertEquals(company2, EmployeePostHolding.getOrganizationOfEmployee(employee, now));
	}
	
	@Test
	public void testTerminateEmployeeOrganizationRelation() {
		Set<Employee> employees = new HashSet<Employee>();
		employees.add(employee);
		EmployeePostHolding.terminateEmployeeOrganizationRelation(department, employees);
		
		assertFalse(EmployeePostHolding.findEmployeesOfOrganization(department, now).contains(employee));
	}
	
}
