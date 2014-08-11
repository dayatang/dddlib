package org.dddlib.organisation.domain;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.dddlib.organisation.utils.OrganisationUtils;
import org.junit.Test;

import org.dayatang.utils.DateUtils;

public class AccountabilityTest extends AbstractIntegrationTest {

	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindAccountabilities() {
		OrganisationUtils organisationUtils = new OrganisationUtils();
		Date date = DateUtils.date(2012, 1, 3);
		Company company = organisationUtils.createCompany("总公司", date);
		Department financial = organisationUtils.createDepartment("财务部", company, date);
		Employee employee = organisationUtils.createEmployee("张三", date);
		Employment employment = new Employment(company, employee, date);
		employment.save();
		List<Accountability> results = Accountability.findAccountabilities(Accountability.class, date);
		// 断言找到所有的子类实例。
		OrgLineMgmt lineMgmt = OrgLineMgmt.getByResponsible(financial, date);
		assertTrue(results.contains(lineMgmt));
		assertTrue(results.contains(employment));
	}

}
