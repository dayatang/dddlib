package org.openkoala.organisation.application;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openkoala.organisation.application.dto.OrganizationDTO;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Department;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Organization;

/**
 * 组织机构应用层接口
 *
 */
public interface OrganizationApplication {

	
	boolean isTopOrganizationExists();
	
	void createAsTopOrganization(Company company);
	
	/**
	 * 创建组织机构
	 * @param organization
	 * @return
	 */
	Company createCompany(Company parent, Company company);
	
	/**
	 * 在组织机构下创建下级机构
	 * @param parent
	 * @param child
	 * @return
	 */
	Department createDepartment(Organization parent, Department department);
	
	/**
	 * 为组织机构分配子机构
	 * @param parent
	 * @param child
	 * @param date
	 */
	void assignChildOrganization(Organization parent, Organization child, Date date);

	/**
	 * 获取组织机构的父机构
	 * @param organization
	 * @param date
	 * @return
	 */
	Organization getParentOfOrganization(Organization organization, Date date);
	
	/**
	 * 查找组织机构的所有子机构
	 * @param organization
	 * @param date
	 * @return
	 */
	List<Organization> findChildrenOfOrganization(Organization organization, Date date);
	
	/**
	 * 获取机构树
	 * @return
	 */
	OrganizationDTO getOrganizationTree();
	
	/**
	 * 撤销某个机构与一批员工的责任关系
	 * @param organization
	 * @param employees
	 */
	void terminateEmployeeOrganizationRelation(Organization organization, Set<Employee> employees);
	
	/**
	 * 根据id获得机构信息
	 * @param id
	 * @return
	 */
	OrganizationDTO getOrganizationById(Long id);
	
}
