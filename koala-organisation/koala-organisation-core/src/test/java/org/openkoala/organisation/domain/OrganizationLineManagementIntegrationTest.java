package org.openkoala.organisation.domain;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.utils.OrganisationUtils;

import com.dayatang.utils.DateUtils;

/**
 * OrganizationLineManagement集成测试
 * @author xmfang
 *
 */
public class OrganizationLineManagementIntegrationTest extends AbstractIntegrationTest {

	private Company company;
	private Department financial;
	private OrganizationLineManagement lineMgmt;
	private Date date = DateUtils.date(2013, 1, 1);
	
	@Before
	public void subSetup() {
		OrganisationUtils organisationUtils = new OrganisationUtils();
		company = organisationUtils.createCompany("总公司", "JG-xxx1", date);
		financial = organisationUtils.createDepartment("财务部", "JG-xxx2", date);
		lineMgmt = new OrganizationLineManagement(company, financial, date);
		lineMgmt.save();
	}
	
	@Test
	public final void testFindChildrenOfOrganization() {
		assertTrue(OrganizationLineManagement.findChildrenOfOrganization(company, date).contains(financial));
	}

	@Test
	public final void testGetParentOfOrganization() {
		assertEquals(company, OrganizationLineManagement.getParentOfOrganization(financial, date));
	}
	
	@Test
	public final void testFindAll() {
		assertTrue(OrganizationLineManagement.findAll().contains(lineMgmt));
	}
	
	@Test
	public final void testGetByResponsible() {
		assertEquals(lineMgmt, OrganizationLineManagement.getByResponsible(financial, date));
	}
	
}
