package org.dddlib.organisation.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.dddlib.organisation.utils.OrganisationUtils;
import org.junit.Test;

import org.dayatang.utils.DateUtils;

public class EmploymentTest extends AbstractIntegrationTest {

	@Test
	public final void testGetEmployer() {
		OrganisationUtils organisationUtils = new OrganisationUtils();
		Date date = DateUtils.date(2012, 1, 3);
		Company company = organisationUtils.createCompany("总公司", date);
		Employee zhangsan = organisationUtils.createEmployee("张三", date);
		Employee lisi = organisationUtils.createEmployee("李四", date);
		new Employment(company, zhangsan, date).save();
		assertThat("张三的雇主应该是company",
				Employment.getEmployer(zhangsan, DateUtils.date(2012, 1, 3)),
				is(company));
		assertThat("李四的雇主不是company",
				Employment.getEmployer(lisi, DateUtils.date(2012, 1, 3)),
				not(is(company)));
	}
}
