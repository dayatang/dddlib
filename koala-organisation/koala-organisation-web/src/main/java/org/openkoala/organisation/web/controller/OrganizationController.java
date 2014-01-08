package org.openkoala.organisation.web.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.TerminateNotEmptyOrganizationException;
import org.openkoala.organisation.TerminateRootOrganizationException;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.application.dto.EmployeeDTO;
import org.openkoala.organisation.application.dto.OrganizationDTO;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Department;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Organization;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 机构管理Controller
 * @author xmfang
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController extends BaseController {
	
    /**
     * 组织机构应用接口
     */
    @Inject
    private OrganizationApplication organizationApplication;
    
    /**
     * 在某个公司下创建一个分公司
     * @param parentId
     * @param company
     * @return
     */
    @ResponseBody
    @RequestMapping("/create-company")
    public Map<String, Object> createCompany(Long parentId, Company company) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
    		Company parent = getBaseApplication().getEntity(Company.class, parentId);
        	organizationApplication.createCompany(parent, company);
        	dataMap.put("result", "success");
    	} catch (SnIsExistException exception) {
    		dataMap.put("result", "机构编码: " + company.getSn() + " 已被使用！");
    	} catch (Exception exception) {
    		dataMap.put("result", "创建公司失败！");
    	}
    	
    	return dataMap;
    }
    
    /**
     * 在某个机构下创建一个部门
     * @param parentId
     * @param parentType
     * @param department
     * @return
     */
    @ResponseBody
    @RequestMapping("/create-department")
    public Map<String, Object> createDepartment(Long parentId, String parentType, Department department) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
    		Organization parent = getBaseApplication().getEntity(Organization.class, parentId);
        	organizationApplication.createDepartment(parent, department);
        	dataMap.put("result", "success");
    	} catch (SnIsExistException exception) {
    		dataMap.put("result", "机构编码: " + department.getSn() + " 已被使用！");
    	} catch (Exception exception) {
    		dataMap.put("result", "创建部门失败！");
    	}
    	
    	return dataMap;
    }
    
    /**
     * 更新公司信息
     * @param company
     * @return
     */
    @ResponseBody
    @RequestMapping("/update-company")
    public Map<String, Object> updateCompany(Company company) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
    		getBaseApplication().updateParty(company);
        	dataMap.put("result", "success");
    	} catch (SnIsExistException exception) {
    		dataMap.put("result", "机构编码: " + company.getSn() + " 已被使用！");
    	} catch (Exception exception) {
    		dataMap.put("result", "修改公司信息失败！");
    	}
    	
    	return dataMap;
    }
    
    /**
     * 更新部门信息
     * @param department
     * @return
     */
    @ResponseBody
    @RequestMapping("/update-department")
    public Map<String, Object> updateDepartment(Department department) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
    		getBaseApplication().updateParty(department);
        	dataMap.put("result", "success");
    	} catch (SnIsExistException exception) {
    		dataMap.put("result", "机构编码: " + department.getSn() + " 已被使用！");
    	} catch (Exception exception) {
    		dataMap.put("result", "修改部门信息失败！");
    	}
    	
    	return dataMap;
    }
   
    /**
     * 获取组织机构树
     * @return
     */
    @ResponseBody
    @RequestMapping("/orgTree")
    public Map<String, Object> getOrgTree() {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	dataMap.put("orgTree", organizationApplication.getOrganizationTree());
    	return dataMap;
    }
    
    /**
     * 根据ID获得机构
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/getOrg")
    public Map<String, Object> getOrganization(long id) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	OrganizationDTO organizationDTO = organizationApplication.getOrganizationById(id);
    	dataMap.put("org", organizationDTO);
    	return dataMap;
    }
	
	/**
	 * 撤销某个机构与某些员工的责任关系
	 * @param employeeDtos
	 * @param organizationId
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/terminate_eoRelations", method = RequestMethod.POST, consumes = "application/json")
	public Map<String, Object> terminateEmployeeOrganizationRelation(@RequestBody EmployeeDTO[] employeeDtos, Long organizationId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Organization organization = getBaseApplication().getEntity(Organization.class, organizationId);
		
		Set<Employee> employees = new HashSet<Employee>();
		for (EmployeeDTO employeeDTO : employeeDtos) {
			employees.add(employeeDTO.transFormToEmployee());
		}
		
		organizationApplication.terminateEmployeeOrganizationRelation(organization, employees);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 撤销一个公司
	 * @param company
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/terminate-company")
	public Map<String, Object> terminateCompany(Company company) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			getBaseApplication().terminateParty(company);
			dataMap.put("result", "success");
		} catch (TerminateRootOrganizationException exception) {
			dataMap.put("result", "不能撤销根机构！");
		} catch (TerminateNotEmptyOrganizationException exception) {
			dataMap.put("result", "该机构下还有员工，不能撤销！");
		}
		return dataMap;
	}

	/**
	 * 撤销一个部门
	 * @param department
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/terminate-department")
	public Map<String, Object> terminateDepartment(Department department) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			getBaseApplication().terminateParty(department);
			dataMap.put("result", "success");
		} catch (TerminateNotEmptyOrganizationException exception) {
			dataMap.put("result", "该机构下还有员工，不能撤销！");
		}
		
		return dataMap;
	}

}
