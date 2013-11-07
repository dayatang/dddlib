package org.openkoala.organisation.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.HasPrincipalPostYetException;
import org.openkoala.organisation.IdNumberIsExistException;
import org.openkoala.organisation.utils.OrganisationUtils;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.utils.DateUtils;

/**
 * 员工集成测试
 * @author xmfang
 *
 */
@TransactionConfiguration(transactionManager = "transactionManager_org", defaultRollback = true)
public class EmployeeIntegrationTest extends AbstractIntegrationTest {
	
	private Company company1;
	private Company company2;
	private Department department;
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
		
		Job job1 = organisationUtils.createJob("会计", "JOB-XXX1", date);
		Job job2 = organisationUtils.createJob("分公司副总经理", "JOB-XXX2", date);
		post1 = organisationUtils.createPost("会计", "POST-XXX1", job1, department, date);
		post2 = organisationUtils.createPost("广州分公司副总经理", "POST-XXX2", job2, company2, date);
		employee = organisationUtils.createEmployee("张三", "XXXXXXXX", "EMP-XXX", post2, date);
		new EmployeePostHolding(post1, employee, false, date).save();
	}

	@Test
	public void testGetPosts() {
		Set<Post> posts = employee.getPosts(now);
		assertEquals(2, posts.size());
		assertTrue(posts.contains(post1));
		assertTrue(posts.contains(post2));
	}
	
	@Test
	public void testGetPrincipalPost() {
		assertEquals(post2, employee.getPrincipalPost(now));
	}
	
	@Test
	public void getAdditionalPosts() {
		List<Post> additionalPosts = employee.getAdditionalPosts(now);
		assertEquals(1, additionalPosts.size());
		assertTrue(additionalPosts.contains(post1));
	}
	
	@Test
	public void testGetOrganization() {
		assertEquals(company2, employee.getOrganization(now));
	}
	
	@Test(expected = HasPrincipalPostYetException.class)
	public void testAssignMultiPrincipalPost() {
		Job job3 = organisationUtils.createJob("出纳", "JOB-XXX3", date);
		Post post3 = organisationUtils.createPost("广州财务部出纳", "POST-XXX3", job3, department, date);
		employee.assignPost(post3, true);
	}
	
	@Test
	public void testAssignPost() {
		Job job3 = organisationUtils.createJob("出纳", "JOB-XXX3", date);
		Post post3 = organisationUtils.createPost("广州财务部出纳", "POST-XXX3", job3, department, date);
		employee.assignPost(post3, false);
		
		Set<Post> posts = employee.getPosts(now);
		assertEquals(3, posts.size());
		assertTrue(posts.contains(post1));
		assertTrue(posts.contains(post2));
		assertTrue(posts.contains(post3));
	}
	
	@Test
	public void testAssignPosts() {
		Set<Post> posts = new HashSet<Post>();
		Job job3 = organisationUtils.createJob("出纳", "JOB-XXX3", date);
		Post post3 = organisationUtils.createPost("广州财务部出纳", "POST-XXX3", job3, department, date);
		posts.add(post3);

		Job job4 = organisationUtils.createJob("高级会计", "JOB-XXX4", date);
		Post post4 = organisationUtils.createPost("广州财务部高级会计", "POST-XXX4", job4, department, date);
		posts.add(post4);

		employee.assignPosts(posts);
		
		Set<Post> allPosts = employee.getPosts(now);
		assertEquals(4, allPosts.size());
		assertTrue(allPosts.contains(post1));
		assertTrue(allPosts.contains(post2));
		assertTrue(allPosts.contains(post3));
		assertTrue(allPosts.contains(post4));
	}
	
//	@Test(expected = EmployeeMustHaveAtLeastOnePostException.class)
//	public void testOutgoingAllPosts() {
//		Set<Post> posts = new HashSet<Post>();
//		posts.add(post1);
//		posts.add(post2);
//		employee.outgoingPosts(posts);
//	}
	
	@Test
	public void testOutgoingPosts() {
		Set<Post> posts = new HashSet<Post>();
		posts.add(post1);
		employee.outgoingPosts(posts);
		
		Set<Post> allPosts = employee.getPosts(now);
		assertEquals(1, allPosts.size());
		assertTrue(allPosts.contains(post2));
	}
	
	@Test
	public void testSaveWithPost() {
		Person person = new Person("李四");
		Employee employee2 = new Employee(person, "EMP-XXX2", date);
		employee2.saveWithPost(post1);
		
		Set<Post> allPosts = employee2.getPosts(now);
		assertEquals(1, allPosts.size());
		assertTrue(allPosts.contains(post1));
	}
	
	@Test(expected = IdNumberIsExistException.class)
	public void testSaveWithDuplicateIdNumber() {
		Employee employee2 = organisationUtils.createEmployee("李四", "XXXXXXXX", "EMP-XXX2", post1, date);
		employee2.save();
	}
	
}
