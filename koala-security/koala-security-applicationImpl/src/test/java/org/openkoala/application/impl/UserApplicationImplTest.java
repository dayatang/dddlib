package org.openkoala.application.impl;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.junit.Test;
import org.openkoala.auth.application.RoleApplication;
import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;
import com.dayatang.querychannel.support.Page;

/**
 * UserApplicationImpl测试类
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 13, 2013 4:01:54 PM
 */
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public class UserApplicationImplTest extends KoalaBaseSpringTestCase {
	
	@Inject
	private UserApplication userApplication;
	
	@Inject
	private RoleApplication roleApplication;

	@Test
	public void testGetUser() {
		UserVO userVO = createUserVO();
		userApplication.saveUser(userVO);
		assertNotNull(userVO.getId());
		UserVO currentUserVO = userApplication.getUser(userVO.getId());
		assertNotNull(currentUserVO);
	}

	@Test
	public void testSaveUser() {
		UserVO userVO = createUserVO();
		userApplication.saveUser(userVO);
		assertNotNull(userVO.getId());
	}

	@Test
	public void testUpdateUser() {
		UserVO userVO = createUserVO();
		userApplication.saveUser(userVO);
		assertNotNull(userVO.getId());
		userVO.setName("updateName");
		userApplication.updateUser(userVO);
		assertEquals("updateName", userApplication.getUser(userVO.getId()).getName());
	}

	@Test
	public void testUpdatePassword() {
		UserVO userVO = createUserVO();
		userApplication.saveUser(userVO);
		assertNotNull(userVO.getId());
		userVO.setUserPassword("111111");
		userApplication.updatePassword(userVO, "testUser");
		assertEquals("111111", userApplication.getUser(userVO.getId()).getUserPassword());
	}

	@Test
	public void testRemoveUser() {
		UserVO userVO = createUserVO();
		userApplication.saveUser(userVO);
		assertNotNull(userVO.getId());
		userApplication.removeUser(userVO.getId());
	}

	@Test
	public void testFindAllUser() {
		for (int i = 0; i < 5; i++) {
			UserVO userVO = createUserVO();
			userVO.setUserAccount(String.valueOf(i));
			userApplication.saveUser(userVO);
			assertNotNull(userVO.getId());
		}
		List<UserVO> results = userApplication.findAllUser();
		assertNotNull(results);
		assertTrue(results.size() > 0);
	}

	@Test
	public void testPageQueryUser() {
		for (int i = 0; i < 50; i++) {
			UserVO userVO = createUserVO();
			userVO.setUserAccount(String.valueOf(i));
			userApplication.saveUser(userVO);
			assertNotNull(userVO.getId());
		}
		Page<UserVO> page = userApplication.pageQueryUser(1, 10);
		assertNotNull(page);
		assertNotNull(page.getResult());
		assertTrue(page.getTotalCount() >= 50);
		assertTrue(page.getTotalPageCount() >= 5);
	}

	@Test
	public void testFindByUserAccount() {
		UserVO userVO = createUserVO();
		userApplication.saveUser(userVO);
		assertNotNull(userVO.getId());
		assertNotNull(userApplication.findByUserAccount("testUser"));
	}

	@Test
	public void testAssignRole() {
		RoleVO roleVO = new RoleVO();
		roleVO.setName("testRole");
		roleVO.setRoleDesc("testRole");
		roleVO.setValid(true);
		roleApplication.saveRole(roleVO);
		assertNotNull(roleVO.getId());
		
		UserVO userVO = createUserVO();
		userApplication.saveUser(userVO);
		assertNotNull(userVO.getId());
		
		userApplication.assignRole(userVO, roleVO);
	}

	@Test
	public void testAssignRoles() {
		RoleVO roleVO = new RoleVO();
		roleVO.setName("testRole");
		roleVO.setRoleDesc("testRole");
		roleVO.setValid(true);
		roleApplication.saveRole(roleVO);
		assertNotNull(roleVO.getId());
		
		RoleVO roleVO2 = new RoleVO();
		roleVO2.setName("adminRole");
		roleVO2.setRoleDesc("adminRole");
		roleVO2.setValid(true);
		roleApplication.saveRole(roleVO2);
		assertNotNull(roleVO2.getId());
		
		List<RoleVO> roleVOs = new ArrayList<RoleVO>();
		roleVOs.add(roleVO);
		roleVOs.add(roleVO2);
		
		UserVO userVO = createUserVO();
		userApplication.saveUser(userVO);
		assertNotNull(userVO.getId());
		
		userApplication.assignRole(userVO, roleVOs);
		
	}
	
	private UserVO createUserVO() {
		UserVO userVO = new UserVO();
		userVO.setUserAccount("testUser");
		userVO.setName("testUser");
		userVO.setUserPassword("testUser");
		return userVO;
	}

}
