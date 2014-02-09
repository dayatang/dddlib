package org.openkoala.organisation.application.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Department;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.EmployeePostHolding;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.OrganizationLineManagement;
import org.openkoala.organisation.domain.Person;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 职务应用接口实现单元测试
 * @author xmfang
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ OrganizationLineManagement.class, EmployeePostHolding.class })
public class OrganizationApplicationImplTest {

	private OrganizationApplication organizationApplication = new OrganizationApplicationImpl();
	
	@Test
	public void testCreateAsTopOrganization() {
		Company company = mock(Company.class);
		organizationApplication.createAsTopOrganization(company);
		verify(company, only()).createAsTopOrganization();
	}
	
	@Test
	public void testCreateCompany() {
		Company parent = new Company("总公司", "COM-001");
		Company company = mock(Company.class);
		organizationApplication.createCompany(parent, company);
		verify(company, only()).createUnder(parent);
	}
	
	@Test
	public void testGetParentOfOrganization() {
		Date date = new Date();
		Company parent = new Company("总公司", "COM-001");
		Department child = new Department("财务部", "DEP-001");
		
		PowerMockito.mockStatic(OrganizationLineManagement.class);
		PowerMockito.when(OrganizationLineManagement.getParentOfOrganization(child, date)).thenReturn(parent);
		assertEquals(parent, organizationApplication.getParentOfOrganization(child, date));
	}
	
	@Test
	public void testFindChildrenOfOrganization() {
		Date date = new Date();
		Company parent = new Company("总公司", "COM-001");
		Department child1 = new Department("财务部", "DEP-001");
		Department child2 = new Department("行政部", "DEP-002");
		
		List<Organization> children = new ArrayList<Organization>();
		children.add(child1);
		children.add(child2);
		
		PowerMockito.mockStatic(OrganizationLineManagement.class);
		PowerMockito.when(OrganizationLineManagement.findChildrenOfOrganization(parent, date)).thenReturn(children);
		assertEquals(children, organizationApplication.findChildrenOfOrganization(parent, date));
	}
	
	@Test
	public void testCreateDepartment() {
		Company parent = new Company("总公司", "COM-001");
		Department department = mock(Department.class);
		
		organizationApplication.createDepartment(parent, department);
		verify(department, only()).createUnder(parent);
	}
	
	@Test
	public void testTerminateEmployeeOrganizationRelation() {
		Date date = new Date();
		Person person1 = new Person("张三");
		Employee employee1 = new Employee(person1, date);
		
		Person person2 = new Person("李四");
		Employee employee2 = new Employee(person2, date);
		
		Set<Employee> employees = new HashSet<Employee>();
		employees.add(employee1);
		employees.add(employee2);
		
		Company company = new Company("总公司", "COM-001");
		
		PowerMockito.mockStatic(EmployeePostHolding.class);
		
		organizationApplication.terminateEmployeeOrganizationRelation(company, employees);
		PowerMockito.verifyStatic(only());
		EmployeePostHolding.terminateEmployeeOrganizationRelation(company, employees);
	}
	
//	@Test
//	public void testAssignChildOrganization() throws Exception {
//		Date now = new Date();
//		Company parent = new Company("总公司", "COM-001");
//		Department child = new Department("财务部", "DEP-001");
//		
//		OrganizationLineManagement organizationLineManagement = PowerMockito.mock(OrganizationLineManagement.class);
//		PowerMockito.whenNew(OrganizationLineManagement.class).withArguments(parent, child, now).thenReturn(organizationLineManagement);
//		
//		organizationApplication.assignChildOrganization(parent, child, now);
//		PowerMockito.verifyNew(OrganizationLineManagement.class).withArguments(parent, child, now);
//		verify(organizationLineManagement, only()).save();
//	}
	
}
