package org.openkoala.organisation.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.TerminateNotEmptyOrganizationException;
import org.openkoala.organisation.TerminateRootOrganizationException;
import org.openkoala.organisation.utils.OrganisationUtils;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.utils.DateUtils;

/**
 * 组织机构集成测试
 * @author xmfang
 */
@TransactionConfiguration(transactionManager = "transactionManager_org", defaultRollback = true)
public class OrganizationIntegrationTest extends AbstractIntegrationTest {
	
	private Company company1;
	private Company company2;
	private Department department;
	private Date date = DateUtils.date(2013, 1, 1);
	private Date now = new Date();
	
	private OrganisationUtils organisationUtils = new OrganisationUtils();
	
	@Before
	public void subSetup() {
		if (Organization.getTopOrganization() == null) {
			company1 = organisationUtils.createTopOrganization("总公司", "JG-XXX", date);
		} else {
			company1 = organisationUtils.createCompany("总公司", "JG-XXX", date);
		}
		company2 = organisationUtils.createCompany("华南分公司", "JG-XXX2", company1, date);
		department = organisationUtils.createDepartment("财务部", "JG-XXX3", company2, date);
	}

	@Test
	public void testGetFullName() {
		assertEquals("总公司/华南分公司", company2.getFullName());
		assertEquals("总公司/华南分公司/财务部", department.getFullName());
	}
	
	@Test
	public void testGetParent() {
		assertEquals(company1, company2.getParent(now));
		assertEquals(company2, department.getParent(now));
	}
	
	@Test
	public void testGetChildren() {
		List<Organization> childrenOfCompany2 = company2.getChildren(now);
		assertEquals(1, childrenOfCompany2.size());
		assertTrue(childrenOfCompany2.contains(department));
		
		List<Organization> childrenOfCompany1 = company1.getChildren(now);
		assertEquals(1, childrenOfCompany1.size());
		assertTrue(childrenOfCompany1.contains(company2));
	}
	
	@Test
	public void testGetAllChildren() {
		Set<Organization> allChildrenOfCompany1 = company1.getAllChildren(now);
		assertEquals(2, allChildrenOfCompany1.size());
		assertTrue(allChildrenOfCompany1.contains(company2));
		assertTrue(allChildrenOfCompany1.contains(department));
	}
	
	@Test
	public void testCreateUnder() {
		Department department2 = new Department("研发部", "JG-XXX4");
		department2.createUnder(company2);
		
		assertEquals(company2, department2.getParent(now));
		assertTrue(company2.getChildren(now).contains(department2));
	}
	
	@Test(expected = TerminateRootOrganizationException.class) 
	public void testTerminateTopOrganization() {
		Organization organization = Organization.getTopOrganization();
		organization.terminate(now);
	}
	
	@Test(expected = TerminateNotEmptyOrganizationException.class) 
	public void testTerminateWithEmployees() {
		Job job = organisationUtils.createJob("会计", "JOB-XXX", date);
		Post post = organisationUtils.createPost("会计", "POST-XXX", job, department, date);
		organisationUtils.createEmployee("张三", "XXXXXXXX", "EMP-XXX", post, date);
		
		department.terminate(now);
	}
	
	@Test
	public void testTerminate() {
		company2.terminate(now);
		assertTrue(!company2.isActive(now));
		assertTrue(!department.isActive(now));
	}
	
	@Test
	public void testGetPosts() {
		Job job = organisationUtils.createJob("会计", "JOB-XXX", date);
		Post post = organisationUtils.createPost("会计", "POST-XXX", job, department, date);
		
		Set<Post> posts = department.getPosts(now);
		assertEquals(1, posts.size());
		assertTrue(posts.contains(post));
	}

}
