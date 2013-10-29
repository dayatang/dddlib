package org.openkoala.organisation.application.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Department;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Gender;
import org.openkoala.organisation.domain.Job;
import org.openkoala.organisation.domain.Person;
import org.openkoala.organisation.domain.Post;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * OrganizationApplicationImpl集成测试
 * 
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Oct 10, 2013 7:54:43 PM
 */
@Ignore
@TransactionConfiguration(transactionManager = "transactionManager_org", defaultRollback = true)
public class OrganizationApplicationImplIntegrationTest extends KoalaBaseSpringTestCase {

	@Inject
	private OrganizationApplication organizationApplication;
	
	private Company companyA;
	private Company companyB;
	private Department departmentC;
	private Department departmentD;
	
	private Job job1;
	private Job job2;
	private Job job3;

	private Post post1;
	private Post post2;
	private Post post3;
	
	@Before
	public void setUp() {
		initOrganizations();
		initJobs();
		initPosts();
		initEmployees();
	}

	/**
	 * 初始化机构数据
	 */
	private void initOrganizations() {
		companyA = new Company("恒拓开源", "org01");
		companyB = new Company("广州分公司", "org02");
		departmentC = new Department("项目研发部", "org03");
		departmentD = new Department("产品研发部", "org04");

		organizationApplication.createAsTopOrganization(companyA);
		
		organizationApplication.createCompany(companyA, companyB);
		organizationApplication.createDepartment(companyB, departmentC);
		organizationApplication.createDepartment(companyB, departmentD);
		
		assertNotNull(companyA.getId());
		assertNotNull(companyB.getId());
		assertNotNull(departmentC.getId());
		assertNotNull(departmentD.getId());
	}
	
	/**
	 * 初始化职务数据
	 */
	private void initJobs() {
		job1 = new Job("初级软件工程师", "zw002");
		job1.save();
		
		job2 = new Job("中级软件工程师", "zw005");
		job2.save();
		
		job3 = new Job("高级软件工程师", "zw008");
		job3.save();
	}
	
	/**
	 * 初始化岗位数据
	 */
	private void initPosts() {
		post1 = new Post("项目部初级工程师", "post001", job1, departmentC);
		post1.save();
		
		post2 = new Post("项目部中级工程师", "post002", job2, departmentC);
		post2.save();
		
		post3 = new Post("产品部高级工程师", "post003", job3, departmentD);
		post3.save();
	}
	
	/**
	 * 初始化员工数据
	 */
	private void initEmployees() {
		Person personA = new Person("张三");
		personA.setGender(Gender.MALE);
		personA.save();
		Employee employeeA = new Employee(personA, new Date());
		employeeA.setSn("YG_0001");
		employeeA.save();
		assertNotNull(employeeA.getId());
		
		Person personB = new Person("李四");
		personB.setGender(Gender.FEMALE);
		personB.save();
		Employee employeeB = new Employee(personB, new Date());
		employeeB.setSn("YG_0002");
		employeeB.save();
		assertNotNull(employeeB.getId());
		
		employeeA.assignPost(post1, true);
		employeeB.assignPost(post3, true);
	}

	@Test
	public void testGetOrganizationTree() {
//		OrganizationDTO organizationDTO = organizationApplication.getOrganizationTree();
//		assertNotNull(organizationDTO);
//		assertFalse(organizationDTO.getChildren().isEmpty());
	}
//	
//	@Test
//	public void testCreateOrganization() {
//		Organization child = new Department("EE");
//		child.save();
//		assertNotNull(child.getId());
//		organizationApplication.createDepartment(Organization.getTopOrganization(), child);
//		
//		assertTrue(Organization.findChildrenOfOrganization(Organization.getTopOrganization()).contains(child));
//	}
//	
//	@Test
//	public void testUpdateOrganization() {
//		Organization topOrganization = Organization.getTopOrganization();
//		assertEquals("AA", topOrganization.getName());
//		
//		topOrganization.setName("XX");
//		organizationApplication.updateOrganization(topOrganization);
//		
//		assertEquals("XX", Organization.getTopOrganization().getName());
//	}
//	
//	@Test 
//	public void testFindEmployeesByOrganization() {
//		Page<Employee> page = organizationApplication.findEmployeesByOrganization(Organization.getTopOrganization().getId(), 1, 2);
//		assertNotNull(page);
//		assertFalse(page.getResult().isEmpty());
//		assertEquals(2, page.getResult().size());
//	}
//	
//	@Test
//	public void testGetOrganization() {
//		Organization organization = organizationApplication.getOrganization(Organization.getTopOrganization().getId());
//		assertNotNull(organization);
//	}

}
