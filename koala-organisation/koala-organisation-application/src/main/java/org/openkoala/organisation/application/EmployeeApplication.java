package org.openkoala.organisation.application;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openkoala.organisation.application.dto.EmployeeDTO;
import org.openkoala.organisation.application.dto.ResponsiblePostDTO;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Post;

import com.dayatang.querychannel.support.Page;

/**
 * 员工应用层接口
 *
 */
public interface EmployeeApplication {

	/**
	 * 获取员工的所属机构
	 * @param employee
	 * @param date
	 * @return
	 */
	Organization getOrganizationOfEmployee(Employee employee, Date date);

	/**
	 * 分页查询员工
	 * @param example
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<EmployeeDTO> pagingQueryEmployees(EmployeeDTO example, int currentPage, int pagesize);

	/**
	 * 分页查询某个机构的直属员工
	 * @param example
	 * @param organization
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<EmployeeDTO> pagingQueryEmployeesByOrganization(EmployeeDTO example, Organization organization, int currentPage, int pagesize);

	/**
	 * 分页查询某个机构及其下属机构的员工
	 * @param example
	 * @param organization
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<EmployeeDTO> pagingQueryEmployeesByOrganizationAndChildren(EmployeeDTO example, Organization organization, int currentPage, int pagesize);
	
	/**
	 * 分页查询没有所属机构的员工
	 * @param example
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<EmployeeDTO> pagingQueryEmployeesWhoNoPost(EmployeeDTO example, int currentPage, int pagesize);
	
	/**
	 * 创建员工及其任职信息和机构关系责任信息
	 * @param employee
	 * @param job
	 * @param organization
	 */
	void createEmployeeWithPost(Employee employee, Post post);
	
	/**
	 * 调整某个员工的任职信息
	 * @param employee
	 * @param dtos
	 */
	void transformPost(Employee employee, Set<ResponsiblePostDTO> dtos);
	
	/**
	 * 获得某个员工的任职职务信息
	 * @param employee
	 * @return
	 */
	List<ResponsiblePostDTO> getPostsByEmployee(Employee employee);
	
	EmployeeDTO getEmployeeById(Long id);
}
