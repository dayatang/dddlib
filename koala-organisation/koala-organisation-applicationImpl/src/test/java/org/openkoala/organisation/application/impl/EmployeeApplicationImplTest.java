package org.openkoala.organisation.application.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Person;

import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.QuerySettings;

/**
 * EmployeeApplicationImpl单元测试类
 * @author zyb
 * @since Oct 6, 2013 3:58:55 PM
 *
 */
public class EmployeeApplicationImplTest {

	private EmployeeApplicationImpl employeeApplicationImpl = new EmployeeApplicationImpl();
	
	private Person person = new Person("zyb");
	private Employee employee = new Employee(person, new Date());
	
	@Mock
	private EntityRepository repository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Employee.setRepository(repository);
	}
	
	@Test
	public void testGetOrganizationOfEmployee() {
//		EmployeeOrganizationHolding holding = new EmployeeOrganizationHolding(new Company("AA"), employee, false, new Date());
//		
//		List<EmployeeOrganizationHolding> holdings = new ArrayList<EmployeeOrganizationHolding>();
//		holdings.add(holding);
//		
//		Date now = new Date();
//		
//		when(repository.find(QuerySettings.create(EmployeeOrganizationHolding.class) //
//				.eq("responsible", employee) //
//				.le("fromDate", now) //
//				.gt("toDate", now))) //
//				.thenReturn(holdings);
//		
//		Organization organization = employeeApplicationImpl.getOrganizationOfEmployee(employee, now);
//		
//		assertEquals(holdings.get(0).getCommissioner(), organization);
	}
	
}
