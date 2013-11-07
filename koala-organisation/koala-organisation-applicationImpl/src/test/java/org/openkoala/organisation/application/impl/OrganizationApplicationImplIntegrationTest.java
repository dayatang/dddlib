package org.openkoala.organisation.application.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.application.dto.OrganizationDTO;
import org.openkoala.organisation.application.impl.utils.OrganisationUtils;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Department;
import org.openkoala.organisation.domain.Organization;

import com.dayatang.utils.DateUtils;

/**
 * 组织机构应用实现集成测试
 * @author xmfang
 */
public class OrganizationApplicationImplIntegrationTest extends AbstractIntegrationTest {

	@Inject
	private OrganizationApplication organizationApplication;
	
	private Company company1;
	private Company company2;
	private Department department;
	private Date date = DateUtils.date(2013, 1, 1);
	private Date now = new Date();
	
	private OrganisationUtils organisationUtils = new OrganisationUtils();
	private boolean noTopOrganizationBeforeTest;
	
	@Before
	public void subSetup() {
		if (Organization.getTopOrganization() == null) {
			company1 = organisationUtils.createTopOrganization("总公司", "JG-XXX", date);
			noTopOrganizationBeforeTest = true;
		} else {
			company1 = organisationUtils.createCompany("总公司", "JG-XXX", date);
		}
		company2 = organisationUtils.createCompany("华南分公司", "JG-XXX2", company1, date);
		department = organisationUtils.createDepartment("财务部", "JG-XXX3", company2, date);
	}

	@Test
	public void testCreateUnder() {
		Department department2 = new Department("研发部", "JG-XXX4");
		department2.save();
		organizationApplication.assignChildOrganization(company2, department2, date);
		
		assertEquals(company2, department2.getParent(now));
		assertTrue(company2.getChildren(now).contains(department2));
	}

	@Test
	public void testGetOrganizationTree() {
		if (noTopOrganizationBeforeTest) {
			OrganizationDTO departmentDTO = OrganizationDTO.generateDtoBy(department);
			
			OrganizationDTO company2DTO = OrganizationDTO.generateDtoBy(company2);
			Set<OrganizationDTO> ChildrenOfcompany2 = new HashSet<OrganizationDTO>();
			ChildrenOfcompany2.add(departmentDTO);
			company2DTO.setChildren(ChildrenOfcompany2);
			
			OrganizationDTO company1DTO = OrganizationDTO.generateDtoBy(company1);
			Set<OrganizationDTO> ChildrenOfcompany1 = new HashSet<OrganizationDTO>();
			ChildrenOfcompany1.add(company2DTO);
			company1DTO.setChildren(ChildrenOfcompany1);
			
			OrganizationDTO organizationDTO = organizationApplication.getOrganizationTree();
			assertEquals(company1DTO, organizationDTO);
			assertEquals(company1DTO.getChildren(), organizationDTO.getChildren());
			
			OrganizationDTO childOfTop = organizationDTO.getChildren().iterator().next();
			assertEquals(company2DTO, childOfTop);
			assertEquals(company2DTO.getChildren(), childOfTop.getChildren());
			assertEquals(departmentDTO, childOfTop.getChildren().iterator().next());
		}
	}

}
