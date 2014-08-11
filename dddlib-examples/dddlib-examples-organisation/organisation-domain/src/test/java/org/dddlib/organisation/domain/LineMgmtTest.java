package org.dddlib.organisation.domain;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.dddlib.organisation.utils.OrganisationUtils;
import org.junit.Test;

import org.dayatang.utils.DateUtils;

public class LineMgmtTest extends AbstractIntegrationTest {

	@Test
	public final void testFindResponsiblesOfOrganization() {
		OrganisationUtils organisationUtils = new OrganisationUtils();
		Date date = DateUtils.date(2012, 1, 3);
		Company company = organisationUtils.createCompany("总公司", date);
		Department financial = organisationUtils.createDepartment("财务部", date);
		OrgLineMgmt lineMgmt = new OrgLineMgmt(company, financial, date);
		lineMgmt.save();
		System.out.println(OrgLineMgmt.findAll().size());
		assertTrue(OrgLineMgmt.findAll().contains(lineMgmt));
		assertTrue(OrgLineMgmt.findChildrenOfOrganization(company, date).contains(financial));
	}

}
