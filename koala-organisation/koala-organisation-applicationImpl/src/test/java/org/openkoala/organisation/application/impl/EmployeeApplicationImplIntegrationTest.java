package org.openkoala.organisation.application.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.application.EmployeeApplication;
import org.openkoala.organisation.application.dto.EmployeeDTO;
import org.openkoala.organisation.application.dto.ResponsiblePostDTO;
import org.openkoala.organisation.application.impl.utils.OrganisationUtils;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Department;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.EmployeePostHolding;
import org.openkoala.organisation.domain.Job;
import org.openkoala.organisation.domain.Person;
import org.openkoala.organisation.domain.Post;

import com.dayatang.utils.DateUtils;

/**
 * 员工应用实现集成测试
 * @author xmfang
 *
 */
public class EmployeeApplicationImplIntegrationTest extends AbstractIntegrationTest {
	
	private Company company1;
	private Company company2;
	private Department department;
	private Post post1;
	private Post post2;
	private Employee employee1;
	private Employee employee2;
	private Date date = DateUtils.date(2013, 1, 1);
	private Date now = new Date();
	
	private OrganisationUtils organisationUtils = new OrganisationUtils();
	
	@Inject
	private EmployeeApplication employeeApplication;
	
	@Before
	public void subSetup() {
		company1 = organisationUtils.createCompany("总公司", "JG-XXX", date);
		company2 = organisationUtils.createCompany("华南分公司", "JG-XXX2", company1, date);
		department = organisationUtils.createDepartment("财务部", "JG-XXX3", company2, date);
		
		Job job1 = organisationUtils.createJob("会计", "JOB-XXX1", date);
		Job job2 = organisationUtils.createJob("分公司副总经理", "JOB-XXX2", date);
		post1 = organisationUtils.createPost("会计", "POST-XXX1", job1, department, date);
		post2 = organisationUtils.createPost("广州分公司副总经理", "POST-XXX2", job2, company2, date);
		employee1 = organisationUtils.createEmployee("张三", "XXXXXXXX1", "EMP-XXX1", post2, date);
		new EmployeePostHolding(post1, employee1, false, date).save();
		employee2 = organisationUtils.createEmployee("李四", "XXXXXXXX2", "EMP-XXX2", post1, date);
	}

	@Test
	public void testGetOrganizationOfEmployee() {
		assertEquals(company2, employeeApplication.getOrganizationOfEmployee(employee1, now));
	}
	
	@Test
	public void testPagingQueryEmployees() {
		List<EmployeeDTO> employeeDTOs = employeeApplication.pagingQueryEmployees(new EmployeeDTO(), 1, 1).getResult();
		assertEquals(1, employeeDTOs.size());
		
		List<EmployeeDTO> employeeDTOs2 = employeeApplication.pagingQueryEmployees(new EmployeeDTO(), 1, 10).getResult();
		assertEquals(2, employeeDTOs2.size());
		assertTrue(employeeDTOs2.contains(EmployeeDTO.generateDtoBy(employee1)));
		assertTrue(employeeDTOs2.contains(EmployeeDTO.generateDtoBy(employee2)));
	}
	
	@Test
	public void testCreateEmployeeWithPost() {
		Person person = new Person("王五");
		Employee employee3 = new Employee(person, "EMP-XXX3", date);
		employeeApplication.createEmployeeWithPost(employee3, post1);
		
		Set<Post> allPosts = employee3.getPosts(now);
		assertEquals(1, allPosts.size());
		assertTrue(allPosts.contains(post1));
	}
	
	@Test
	public void testPagingQueryEmployeesByOrganization() {
		List<EmployeeDTO> employeeDTOs = employeeApplication.pagingQueryEmployeesByOrganization(new EmployeeDTO(), department, 1, 1).getResult();
		assertEquals(1, employeeDTOs.size());
		
		List<EmployeeDTO> employeeDTOs2 = employeeApplication.pagingQueryEmployeesByOrganization(new EmployeeDTO(), department, 1, 10).getResult();
		assertEquals(2, employeeDTOs2.size());
		assertTrue(employeeDTOs2.contains(EmployeeDTO.generateDtoBy(employee1)));
		assertTrue(employeeDTOs2.contains(EmployeeDTO.generateDtoBy(employee2)));
	}
	
	@Test
	public void testPagingQueryEmployeesByOrganizationAndChildren() {
		List<EmployeeDTO> employeeDTOs = employeeApplication.pagingQueryEmployeesByOrganizationAndChildren(new EmployeeDTO(), company1, 1, 1).getResult();
		assertEquals(1, employeeDTOs.size());
		
		List<EmployeeDTO> employeeDTOs2 = employeeApplication.pagingQueryEmployeesByOrganizationAndChildren(new EmployeeDTO(), company1, 1, 10).getResult();
		assertEquals(2, employeeDTOs2.size());
		assertTrue(employeeDTOs2.contains(EmployeeDTO.generateDtoBy(employee1)));
		assertTrue(employeeDTOs2.contains(EmployeeDTO.generateDtoBy(employee2)));
	}
	
	@Test
	public void testPagingQueryEmployeesWhoNoPost() {
		Employee employee3 = organisationUtils.createEmployee("王五", "XXXXXX3", "EMP-XXX3", date);
		Employee employee4 = organisationUtils.createEmployee("朱八", "XXXXXX4", "EMP-XXX4", date);
		
		List<EmployeeDTO> employeeDTOs = employeeApplication.pagingQueryEmployeesWhoNoPost(new EmployeeDTO(), 1, 10).getResult();
		assertEquals(2, employeeDTOs.size());
		
		assertTrue(employeeDTOs.contains(EmployeeDTO.generateDtoBy(employee3)));
		assertTrue(employeeDTOs.contains(EmployeeDTO.generateDtoBy(employee4)));
	}
	
	@Test
	public void testTransformPost() {
		ResponsiblePostDTO dto1 = new ResponsiblePostDTO();
		dto1.setPostId(post1.getId());
		dto1.setPrincipal(false);
		
		ResponsiblePostDTO dto2 = new ResponsiblePostDTO();
		dto2.setPostId(post2.getId());
		dto2.setPrincipal(true);
		
		Set<ResponsiblePostDTO> dtos = new HashSet<ResponsiblePostDTO>();
		dtos.add(dto1);
		dtos.add(dto2);
		
		employeeApplication.transformPost(employee2, dtos);
		
		Set<Post> posts = employee2.getPosts(now);
		assertEquals(2, posts.size());
		assertTrue(posts.contains(post1));
		assertTrue(posts.contains(post2));
	}
	
	@Test
	public void testGetPostsByEmployee() {
		List<ResponsiblePostDTO> dtos = employeeApplication.getPostsByEmployee(employee1);
		assertEquals(2, dtos.size());
		assertTrue(dtos.contains(ResponsiblePostDTO.generateByPost(post1, false)));
		assertTrue(dtos.contains(ResponsiblePostDTO.generateByPost(post2, true)));
	}
	
}
