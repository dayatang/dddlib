package org.openkoala.bpm.KoalaBPM.web.controller.organisation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.TerminateRootOrganizationException;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.application.dto.EmployeeDTO;
import org.openkoala.organisation.application.dto.OrganizationDTO;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Department;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Organization;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * OrganizationController单元测试
 * @author xmfang
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OrganizationDTO.class})
public class OrganizationControllerTest {
	
	@Mock
	private OrganizationApplication organizationApplication;

	@Mock
	private BaseApplication baseApplication;

	@InjectMocks
	private OrganizationController organizationController = new OrganizationController();
	
	@Test
	public void testCreateCompany() {
		Long parentId = 1L;
		Company parent = new Company("总公司公司", "COM-XXX1");
		parent.setId(parentId);
		Company company = new Company("广州分公司", "COM-XXX2");
		
		when(baseApplication.getEntity(Company.class, parentId)).thenReturn(parent);
		organizationController.createCompany(parentId, company);
		verify(organizationApplication, only()).createCompany(parent, company);
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenCreateCompany() {
		Long parentId = 1L;
		Company parent = new Company("总公司公司", "COM-XXX1");
		parent.setId(parentId);
		Company company = new Company("广州分公司", "COM-XXX2");
		
		when(baseApplication.getEntity(Company.class, parentId)).thenReturn(parent);
		doThrow(new SnIsExistException()).when(organizationApplication).createCompany(parent, company);
		assertEquals("机构编码: " + company.getSn() + " 已被使用！", organizationController.createCompany(parentId, company).get("result"));
	}
	
	@Test
	public void testCatchExceptionWhenCreateCompany() {
		Long parentId = 1L;
		Company parent = new Company("总公司公司", "COM-XXX1");
		parent.setId(parentId);
		Company company = new Company("广州分公司", "COM-XXX2");
		
		when(baseApplication.getEntity(Company.class, parentId)).thenReturn(parent);
		doThrow(new RuntimeException()).when(organizationApplication).createCompany(parent, company);
		assertEquals("创建公司失败！", organizationController.createCompany(parentId, company).get("result"));
	}
	
	@Test
	public void testCreateDepartment() {
		Long parentId = 1L;
		Company parent = new Company("总公司公司", "COM-XXX1");
		parent.setId(parentId);
		Department department = new Department("财务部", "DEP-XXX2");
		
		when(baseApplication.getEntity(Organization.class, parentId)).thenReturn(parent);
		organizationController.createDepartment(parentId, "", department);
		verify(organizationApplication, only()).createDepartment(parent, department);
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenCreateDepartment() {
		Long parentId = 1L;
		Company parent = new Company("总公司公司", "COM-XXX1");
		parent.setId(parentId);
		Department department = new Department("财务部", "DEP-XXX2");
		
		when(baseApplication.getEntity(Organization.class, parentId)).thenReturn(parent);
		doThrow(new SnIsExistException()).when(organizationApplication).createDepartment(parent, department);
		assertEquals("机构编码: " + department.getSn() + " 已被使用！", organizationController.createDepartment(parentId, "", department).get("result"));
	}
	
	@Test
	public void testExceptionWhenCreateDepartment() {
		Long parentId = 1L;
		Company parent = new Company("总公司公司", "COM-XXX1");
		parent.setId(parentId);
		Department department = new Department("财务部", "DEP-XXX2");
		
		when(baseApplication.getEntity(Organization.class, parentId)).thenReturn(parent);
		doThrow(new RuntimeException()).when(organizationApplication).createDepartment(parent, department);
		assertEquals("创建部门失败！", organizationController.createDepartment(parentId, "", department).get("result"));
	}
	
	@Test
	public void testUpdateCompany() {
		Company company = new Company("广州分公司", "COM-XXX2");
		organizationController.updateCompany(company);
		verify(baseApplication, only()).updateParty(company);
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenUpdateCompany() {
		Company company = new Company("广州分公司", "COM-XXX2");
		doThrow(new SnIsExistException()).when(baseApplication).updateParty(company);
		assertEquals("机构编码: " + company.getSn() + " 已被使用！", organizationController.updateCompany(company).get("result"));
	}
	
	@Test
	public void testExceptionWhenUpdateCompany() {
		Company company = new Company("广州分公司", "COM-XXX2");
		doThrow(new RuntimeException()).when(baseApplication).updateParty(company);
		assertEquals("修改公司信息失败！", organizationController.updateCompany(company).get("result"));
	}
	
	@Test
	public void testUpdateDepartment() {
		Department department = new Department("财务部", "DEP-XXX2");
		organizationController.updateDepartment(department);
		verify(baseApplication, only()).updateParty(department);
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenUpdateDepartment() {
		Department department = new Department("财务部", "DEP-XXX2");
		doThrow(new SnIsExistException()).when(baseApplication).updateParty(department);
		assertEquals("机构编码: " + department.getSn() + " 已被使用！", organizationController.updateDepartment(department).get("result"));
	}
	
	@Test
	public void testExceptionWhenUpdateDepartment() {
		Department department = new Department("财务部", "DEP-XXX2");
		doThrow(new RuntimeException()).when(baseApplication).updateParty(department);
		assertEquals("修改部门信息失败！", organizationController.updateDepartment(department).get("result"));
	}

	@Test
	public void testGetOrgTree() {
		OrganizationDTO organizationDTO = new OrganizationDTO(1L, "总公司");
		organizationDTO.setSn("xxx");
		
		when(organizationApplication.getOrganizationTree()).thenReturn(organizationDTO);
		assertEquals(organizationDTO, organizationController.getOrgTree().get("orgTree"));
	}
	
	@Test
	public void testGetOrganization() {
		Company company = new Company("广州分公司", "COM-XXX2");
		Long organizationId = 1L;
		OrganizationDTO organizationDTO = new OrganizationDTO(organizationId, "总公司");
		organizationDTO.setSn("xxx");
		
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(company);
		PowerMockito.mockStatic(OrganizationDTO.class);
		PowerMockito.when(OrganizationDTO.generateDtoBy(company)).thenReturn(organizationDTO);
		
		assertEquals(organizationDTO, organizationController.getOrganization(organizationId).get("org"));
	}
	
	@Test
	public void testTerminateEmployeeOrganizationRelation() {
		Company company = new Company("广州分公司", "COM-XXX2");
		Long organizationId = 1L;
		company.setId(organizationId);
		
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setName("张三");
		employeeDTO.setSn("XXX");
		
		EmployeeDTO[] employeeDTOs = new EmployeeDTO[1];
		employeeDTOs[0] = employeeDTO;
		
		Set<Employee> employees = new HashSet<Employee>();
		employees.add(employeeDTO.transFormToEmployee());
		
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(company);
		organizationController.terminateEmployeeOrganizationRelation(employeeDTOs, organizationId);
		verify(organizationApplication, only()).terminateEmployeeOrganizationRelation(company, employees);
	}
	
	@Test
	public void testTerminateCompany() {
		Company company = new Company("广州分公司", "COM-XXX2");
		organizationController.terminateCompany(company);
		verify(baseApplication, only()).terminateParty(company);
	}
	
	@Test
	public void testCatchTerminateRootOrganizationExceptionWhenTerminateCompany() {
		Company company = new Company("广州分公司", "COM-XXX2");
		doThrow(new TerminateRootOrganizationException()).when(baseApplication).terminateParty(company);
		assertEquals("不能撤销根机构！", organizationController.terminateCompany(company).get("result"));
	}
	
	@Test
	public void testTerminateDepartment() {
		Department department = new Department("财务部", "DEP-XXX2");
		organizationController.terminateDepartment(department);
		verify(baseApplication, only()).terminateParty(department);
	}
	
}
