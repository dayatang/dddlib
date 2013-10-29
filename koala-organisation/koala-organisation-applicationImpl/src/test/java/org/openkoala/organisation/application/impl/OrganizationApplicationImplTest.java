package org.openkoala.organisation.application.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.OrganizationLineManagement;

import com.dayatang.domain.EntityRepository;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

/**
 * 组织机构应用实现层单元测试
 * @author zyb
 * @since Oct 7, 2013 10:38:02 AM
 *
 */
@Ignore
public class OrganizationApplicationImplTest {
	
//	@Mock
//	private EntityRepository repository;
//	
//	@Mock
//	private QueryChannelService queryChannelService;
//	
//	private OrganizationApplicationImpl organizationApplication = new OrganizationApplicationImpl();
//	
//	private Organization company = new Company("foreveross");
//	
//	@Before
//	public void setUp() {
//		MockitoAnnotations.initMocks(this);
//		Organization.setRepository(repository);
//		organizationApplication.setQueryChannelService(queryChannelService);
//	}
//
//	@Test
//	public void testCreateOrganization() {
//		Organization result = organizationApplication.createCompany(company);
//		assertThat(result.getName(), is("foreveross"));
//		verify(repository).save(company);
//	}
//	
//	@Test
//	public void testGetOrganization() {
//		when(repository.get(Organization.class, 1L)).thenReturn(company);
//		assertThat(organizationApplication.getOrganization(1L), is(company));
//	}
//	
//	@Test
//	public void testAssignChildOrganization() {
//		Organization child = new Company("abc");
//		organizationApplication.assignChildOrganization(company, child, new Date());
//		OrganizationLineManagement organizationLineManagement = new OrganizationLineManagement(company, child, new Date());
//		verify(repository).save(organizationLineManagement);
//	}
//	
//	@Test
//	public void testTerminateOrganization() {
//		organizationApplication.terminateOrganization(company);
//		verify(repository).save(company);
//	}
//	
//	@Test
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public void testFindEmployeesByOrganization() {
//		String jpql = "select holding.responsible from EmployeeOrganizationHolding holding join holding.commissioner commissioner where commissioner.id=?";
//		List<Employee> data = new ArrayList<Employee>();
//		data.add(new Employee("zyb"));
//		Page employees = new Page(1, 1, 10, data);
//		when(queryChannelService.queryPagedResultByPageNo(jpql, new Object[] { 1L }, 1, 10)).thenReturn(employees);
//		Page<Employee> result = organizationApplication.findEmployeesByOrganization(1L, 1, 10);
//		assertEquals(data, result.getResult());
//	}
//	
//	@Test
//	public void testGetParentOfOrganization() {
//		Date now = new Date();
//		List<Organization> parents = new ArrayList<Organization>();
//		parents.add(company);
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("organization", new Company("AA"));
//		params.put("date", now);
//		when(repository.findByNamedQuery("getParentOfOrganization", params, Organization.class)) //
//			.thenReturn(parents);
//		Organization parent = organizationApplication.getParentOfOrganization(new Company("AA"), now);
//		assertEquals(parents.get(0), parent);
//	}
//	
//	@Test
//	public void testFindChildrenOfOrganization() {
//		List<Organization> children = new ArrayList<Organization>();
//		children.add(new Company("AA"));
//		children.add(new Company("BB"));
//		
//		Date now = new Date();
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("organization", company);
//		params.put("date", now);
//		when(repository.findByNamedQuery("findChildrenOfOrganization", params, Organization.class)) //
//			.thenReturn(children);
//		
//		List<Organization> results = organizationApplication.findChildrenOfOrganization(company, now);
//		
//		assertEquals(children, results);
//	}
	
}
