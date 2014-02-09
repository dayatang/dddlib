package org.openkoala.application.impl;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.auth.application.RoleApplication;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * RoleApplicationImpl测试
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 14, 2013 10:26:16 AM
 */
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public class RoleApplicationImplTest extends KoalaBaseSpringTestCase {
	
	@Inject
	private RoleApplication roleApplication;
	
	private RoleVO roleVO;
	
	@Before
	public void setUp() {
		roleVO = createRoleVO();
		roleApplication.saveRole(roleVO);
		assertNotNull(roleVO.getId());
	}

	@Test
	public void testGetRole() {
		RoleVO result = roleApplication.getRole(roleVO.getId());
		assertNotNull(result);
	}

	@Test
	public void testUpdateRole() {
		roleVO.setName("adminRole");
		roleApplication.updateRole(roleVO);
		RoleVO result = roleApplication.getRole(roleVO.getId());
		assertEquals("adminRole", result.getName());
	}

	@Test
	public void testRemoveRole() {
		roleApplication.removeRole(roleVO.getId());
	}

	@Test
	public void testFindAllRole() {
		batchSave();
		List<RoleVO> results = roleApplication.findAllRole();
		assertNotNull(results);
		assertTrue(results.size() > 0);
	}

	private RoleVO createRoleVO() {
		RoleVO roleVO = new RoleVO();
		roleVO.setName("testRole");
		roleVO.setRoleDesc("testRole");
		return roleVO;
	}
	
	/**
	 * 批量保存数据
	 */
	private void batchSave() {
		for (int i = 0; i < 50; i++) {
			RoleVO roleVO = new RoleVO();
			roleVO.setName(String.valueOf(i));
			roleVO.setRoleDesc(String.valueOf(i));
			roleApplication.saveRole(roleVO);
			assertNotNull(roleVO.getId());
		}
	}

}
