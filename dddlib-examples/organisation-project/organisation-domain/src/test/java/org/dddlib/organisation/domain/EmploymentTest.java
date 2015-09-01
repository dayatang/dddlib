package org.dddlib.organisation.domain;

import org.dayatang.utils.DateUtils;
import org.dddlib.organisation.utils.OrganisationUtils;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class EmploymentTest extends AbstractIntegrationTest {

    @Test
    public final void testGetEmployer() {
        OrganisationUtils organisationUtils = new OrganisationUtils();
        Date date = DateUtils.date(2012, 1, 3);
        Company company = organisationUtils.createCompany("总公司", date);
        Person zhang = organisationUtils.createPerson("三", "张");
        Employee zhangsan = organisationUtils.createEmployee(zhang, date);
        Person li = organisationUtils.createPerson("四", "李");
        Employee lisi = organisationUtils.createEmployee(li, date);
        new Employment(company, zhangsan, date).save();
        assertThat("张三的雇主应该是company",
                Employment.getEmployer(zhangsan, DateUtils.date(2012, 1, 3)),
                is(company));
        assertThat("李四的雇主不是company",
                Employment.getEmployer(lisi, DateUtils.date(2012, 1, 3)),
                not(is(company)));
    }
}
