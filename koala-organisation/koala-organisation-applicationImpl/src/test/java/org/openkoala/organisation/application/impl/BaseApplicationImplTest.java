package org.openkoala.organisation.application.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Department;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Party;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dayatang.domain.AbstractEntity;

/**
 * BaseApplicationImpl单元测试
 * @author xmfang
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AbstractEntity.class, Party.class })
public class BaseApplicationImplTest {

	private BaseApplication baseApplication = new BaseApplicationImpl();
	
	@Test
	public void testSaveParty() {
		Party party = mock(Party.class);
		baseApplication.saveParty(party);
		verify(party, times(1)).save();
	}
	
	@Test
	public void testUpdateParty() {
		Party party = mock(Party.class);
		baseApplication.updateParty(party);
		verify(party, times(1)).save();
	}
	
	@Test
	public void testTerminateParty() {
		Party party = mock(Party.class);
		baseApplication.terminateParty(party);
		verify(party, times(1)).terminate(any(Date.class));
	}
	
	@Test
	@Ignore
	public void testGetEntity() {
		PowerMockito.mockStatic(AbstractEntity.class);
		Company company = new Company("总公司", "ORG-001");
		PowerMockito.when(AbstractEntity.get(Company.class, 1L)).thenReturn(company);
		assertEquals(company, baseApplication.getEntity(Company.class, 1L));
	}
	
	@Test
	public void testTerminateParties() {
		Party party1 = mock(Party.class);
		Party party2 = mock(Party.class);
		
		Set<Party> parties = new HashSet<Party>();
		parties.add(party1);
		parties.add(party2);
		
		baseApplication.terminateParties(parties);
		verify(party1, times(1)).terminate(any(Date.class));
		verify(party2, times(1)).terminate(any(Date.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindAll() {
		PowerMockito.mockStatic(Party.class);
		Company company = new Company("总公司", "ORG-001");
		Department department = new Department("财务部", "ORG-002");
		List<Organization> organizations = new ArrayList<Organization>();
		organizations.add(company);
		organizations.add(department);
		
		PowerMockito.when(Party.findAll(any(Class.class), any(Date.class))).thenReturn(organizations);
		assertEquals(organizations, baseApplication.findAll(Organization.class));
	}
}
