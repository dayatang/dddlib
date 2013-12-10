package org.openkoala.organisation.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openkoala.organisation.EmployeeMustHaveAtLeastOnePostException;
import org.openkoala.organisation.HasPrincipalPostYetException;
import org.openkoala.organisation.IdNumberIsExistException;
import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.EmployeeApplication;
import org.openkoala.organisation.application.dto.EmployeeDTO;
import org.openkoala.organisation.application.dto.ResponsiblePostDTO;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Person;
import org.openkoala.organisation.domain.Post;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.support.Page;
import com.dayatang.spring.factory.SpringInstanceProvider;

/**
 * EmployeeController单元测试
 * 
 * @author xmfang
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ EmployeeDTO.class })
public class EmployeeControllerTest {

	@Mock
	private EmployeeApplication employeeApplication;

	@Mock
	private BaseApplication baseApplication;

	@InjectMocks
	private EmployeeController employeeController = new EmployeeController();

	private Page<EmployeeDTO> employeePage;
	private Employee employee;
	private Long employeeId = 5L;
	private Post post;
	private Long postId = 3L;
	private ResponsiblePostDTO[] responsiblePostDTOs;

	@Test
	public void testPagingQuery() {
		initEmployeePage();
		when(employeeApplication.pagingQueryEmployees(new EmployeeDTO(), 1, 10))
				.thenReturn(employeePage);
		assertEquals(
				employeePage.getResult(),
				employeeController.pagingQuery(1, 10, new EmployeeDTO()).get(
						"Rows"));
	}

	@Test
	public void testPagingQueryByOrganization() {
		initEmployeePage();
		Organization organization = new Company("总公司", "ORG-888");
		organization.setId(1L);

		when(baseApplication.getEntity(Organization.class, 1L)).thenReturn(
				organization);
		when(
				employeeApplication.pagingQueryEmployeesByOrganization(
						new EmployeeDTO(), organization, 1, 10)).thenReturn(
				employeePage);
		assertEquals(employeePage.getResult(), employeeController
				.pagingQueryByOrganization(1, 10, new EmployeeDTO(), 1L, false)
				.get("Rows"));
	}

	@Test
	public void testPagingQueryByOrganizationAndChildren() {
		initEmployeePage();
		Organization organization = new Company("总公司", "ORG-888");
		organization.setId(1L);

		when(baseApplication.getEntity(Organization.class, 1L)).thenReturn(
				organization);
		when(
				employeeApplication
						.pagingQueryEmployeesByOrganizationAndChildren(
								new EmployeeDTO(), organization, 1, 10))
				.thenReturn(employeePage);
		assertEquals(employeePage.getResult(), employeeController
				.pagingQueryByOrganization(1, 10, new EmployeeDTO(), 1L, true)
				.get("Rows"));
	}

	private void initEmployeePage() {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setName("张三");
		List<EmployeeDTO> employeeDTOs = new ArrayList<EmployeeDTO>();
		employeeDTOs.add(employeeDTO);

		employeePage = new Page<EmployeeDTO>(1, 1, 10, employeeDTOs);
	}

	@Test
	public void testCreateEmployee() {
		initPost();

		when(baseApplication.getEntity(Post.class, postId)).thenReturn(post);
		employeeController.createEmployee(employee, postId);
		verify(employeeApplication, only()).createEmployeeWithPost(employee,
				post);
	}

	@Test
	public void testCatchSnIsExistExceptionWhenCreateEmployee() {
		initPost();
		initEmployee();

		when(baseApplication.getEntity(Post.class, postId)).thenReturn(post);
		doThrow(new SnIsExistException()).when(employeeApplication)
				.createEmployeeWithPost(employee, post);
		assertEquals("员工编号: " + employee.getSn() + " 已被使用！", employeeController
				.createEmployee(employee, postId).get("result"));
	}

	@Test
	public void testIdNumberIsExistExceptionWhenCreateEmployee() {
		initPost();
		initEmployee();

		when(baseApplication.getEntity(Post.class, postId)).thenReturn(post);
		doThrow(new IdNumberIsExistException()).when(employeeApplication)
				.createEmployeeWithPost(employee, post);
		assertEquals(
				"不能使用与其他人一样的证件号码！",
				employeeController.createEmployee(employee, postId).get(
						"result"));
	}

	@Test
	public void testExceptionWhenCreateEmployee() {
		initPost();
		initEmployee();

		when(baseApplication.getEntity(Post.class, postId)).thenReturn(post);
		doThrow(new RuntimeException()).when(employeeApplication)
				.createEmployeeWithPost(employee, post);
		assertEquals(
				"保存失败！",
				employeeController.createEmployee(employee, postId).get(
						"result"));
	}

	private void initPost() {
		post = new Post("总经理", "EMP-XXX");
		post.setId(postId);
	}

	private void initEmployee() {
		Person person = new Person("张三");
		employee = new Employee(person, "EMP-XXX", new Date());
		employee.setId(employeeId);
	}

	private void initResponsiblePostDTOs() {
		responsiblePostDTOs = new ResponsiblePostDTO[2];

		ResponsiblePostDTO dto1 = new ResponsiblePostDTO();
		dto1.setPostId(1L);
		dto1.setPrincipal(true);
		responsiblePostDTOs[0] = dto1;

		ResponsiblePostDTO dto2 = new ResponsiblePostDTO();
		dto2.setPostId(2L);
		dto2.setPrincipal(false);
		responsiblePostDTOs[1] = dto2;
	}

	@Test
	public void testUpdateEmployee() {
		initEmployee();

		employeeController.updateEmployee(employee);
		verify(baseApplication, only()).updateParty(employee);
	}

	@Test
	public void testSnIsExistExceptionWhenUpdateEmployee() {
		initEmployee();

		doThrow(new SnIsExistException()).when(baseApplication).updateParty(
				employee);
		assertEquals("员工编号: " + employee.getSn() + " 已被使用！", employeeController
				.updateEmployee(employee).get("result"));
	}

	@Test
	public void testIdNumberIsExistExceptionWhenUpdateEmployee() {
		initEmployee();

		doThrow(new IdNumberIsExistException()).when(baseApplication)
				.updateParty(employee);
		assertEquals("不能使用与其他人一样的证件号码！",
				employeeController.updateEmployee(employee).get("result"));
	}

	@Test
	public void testExceptionWhenUpdateEmployee() {
		initEmployee();

		doThrow(new RuntimeException()).when(baseApplication).updateParty(
				employee);
		assertEquals("修改失败！",
				employeeController.updateEmployee(employee).get("result"));
	}

	@Test
	public void testTransformPost() {
		initEmployee();
		initResponsiblePostDTOs();
		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(
				employee);

		employeeController.transformPost(employeeId, responsiblePostDTOs);
		verify(employeeApplication).transformPost(
				employee,
				new HashSet<ResponsiblePostDTO>(Arrays
						.asList(responsiblePostDTOs)));
	}

	@Test
	public void testHasPrincipalPostYetExceptionWhenTransformPost() {
		initEmployee();
		initResponsiblePostDTOs();
		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(
				employee);

		doThrow(new HasPrincipalPostYetException()).when(employeeApplication)
				.transformPost(
						employee,
						new HashSet<ResponsiblePostDTO>(Arrays
								.asList(responsiblePostDTOs)));
		assertEquals(
				"该员工已经有主任职岗位！",
				employeeController.transformPost(employeeId,
						responsiblePostDTOs).get("result"));
	}

	@Test
	public void testEmployeeMustHaveAtLeastOnePostExceptionWhenTransformPost() {
		initEmployee();
		initResponsiblePostDTOs();
		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(
				employee);

		doThrow(new EmployeeMustHaveAtLeastOnePostException()).when(
				employeeApplication).transformPost(
				employee,
				new HashSet<ResponsiblePostDTO>(Arrays
						.asList(responsiblePostDTOs)));
		assertEquals(
				"必须保证每名员工至少在一个岗位上任职！",
				employeeController.transformPost(employeeId,
						responsiblePostDTOs).get("result"));
	}

	@Test
	public void testExceptionWhenTransformPost() {
		initEmployee();
		initResponsiblePostDTOs();
		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(
				employee);

		doThrow(new RuntimeException()).when(employeeApplication)
				.transformPost(
						employee,
						new HashSet<ResponsiblePostDTO>(Arrays
								.asList(responsiblePostDTOs)));
		assertEquals(
				"调整职务失败！",
				employeeController.transformPost(employeeId,
						responsiblePostDTOs).get("result"));
	}

	@Test
	public void testGet() {
		initEmployee();
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setName("张三");
		employeeDTO.setId(employeeId);

		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(
				employee);
		PowerMockito.mockStatic(EmployeeDTO.class);
		PowerMockito.when(EmployeeDTO.generateDtoBy(employee)).thenReturn(
				employeeDTO);

		assertEquals(employeeDTO, employeeController.get(employeeId)
				.get("data"));
	}

	@Test
	public void testGetGendens() {
		Map<String, String> genders = new HashMap<String, String>();
		genders.put("MALE", "男");
		genders.put("FEMALE", "女");
		assertEquals(genders, employeeController.getGendens().get("data"));
	}

	@Test
	public void testGetPostsByEmployee() {
		initEmployee();
		List<ResponsiblePostDTO> dtos = new ArrayList<ResponsiblePostDTO>();
		ResponsiblePostDTO dto = new ResponsiblePostDTO();
		dto.setPostId(postId);
		dto.setPrincipal(true);
		dtos.add(dto);

		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(
				employee);
		when(employeeApplication.getPostsByEmployee(employee)).thenReturn(dtos);
		assertEquals(dtos, employeeController.getPostsByEmployee(employeeId)
				.get("data"));
	}

	@Test
	public void testTerminateEmployee() {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setName("张三");
		dto.setSn("EMP-XXX");
		dto.setId(employeeId);

		employeeController.terminateEmployee(dto);
		verify(baseApplication, only()).terminateParty(
				dto.transFormToEmployee());
	}

	@Test
	public void testTerminateEmployees() {
		EmployeeDTO[] dtos = new EmployeeDTO[2];

		EmployeeDTO dto1 = new EmployeeDTO();
		dto1.setName("张三");
		dto1.setSn("EMP-XXX");
		dto1.setId(employeeId);
		dtos[0] = dto1;

		EmployeeDTO dto2 = new EmployeeDTO();
		dto2.setName("李四");
		dto2.setSn("EMP-XXX1");
		dto2.setId(6L);
		dtos[1] = dto2;

		Set<Employee> employees = new HashSet<Employee>();
		for (EmployeeDTO employeeDTO : dtos) {
			employees.add(employeeDTO.transFormToEmployee());
		}

		employeeController.terminateEmployees(dtos);
		verify(baseApplication, only()).terminateParties(employees);
	}

}
