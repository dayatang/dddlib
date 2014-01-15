package org.openkoala.koala.auth.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.utils.DateUtils;

/**
 * 用户测试类
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 12, 2013 10:05:35 AM
 */
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public class UserTest extends KoalaBaseSpringTestCase {
	
	/**
	 * 保存方法
	 */
	@Test
	public void testSave() {
		User user = createUser();
		user.save();
		Assert.assertNotNull(user.getId());
	}

	/**
	 * 更新方法
	 */
	@Test
	public void testUpdate() {
		User user = createUser();
		user.setUserAccount("test");
		user.save();
		Assert.assertNotNull(user.getId());
		Assert.assertEquals("test", user.getUserAccount());
	}
	
	/**
	 * 删除方法
	 */
	@Test
	public void testRemove() {
		User user = createUser();
		user.save();
		Assert.assertNotNull(user.getId());
		user.remove();
		User removedUser = User.get(User.class, user.getId());
		Assert.assertNull(removedUser);
	}
	
	/**
	 * get方法
	 */
	@Test
	public void testGet() {
		User user = createUser();
		user.save();
		Assert.assertNotNull(user.getId());
		User currentUser = User.get(User.class, user.getId());
		Assert.assertNotNull(currentUser);
	}
	
	/**
	 * load方法
	 */
	@Test
	public void testLoad() {
		User user = createUser();
		user.save();
		Assert.assertNotNull(user.getId());
		User currentUser = User.load(User.class, user.getId());
		Assert.assertNotNull(currentUser);
	}
	
	/**
	 * 根据用户查找用户信息
	 */
	@Test
	public void testFindByUserAccount() {
		User user = createUser();
		user.save();
		Assert.assertNotNull(user.getId());
		User currentUser = User.findByUserAccount("zyb");
		Assert.assertNotNull(currentUser);
		Assert.assertEquals("zyb", currentUser.getUserAccount());
	}
	
	/**
	 * 用户账号是否已经存
	 */
	@Test
	public void testIsExist() {
		User user = createUser();
		user.save();
		Assert.assertNotNull(user.getId());
		Assert.assertTrue(user.isAccountExisted());
	}
	
	/**
	 * 重置密码
	 */
	@Test
	public void testResetPassword() {
		User user = createUser();
		user.save();
		Assert.assertNotNull(user.getId());
		user.resetPassword();
		Assert.assertEquals("abcd", user.getUserPassword());
	}
	
	/**
	 * 为用户分配单个角色 
	 */
	@Test
	public void testAssignSingleRole() {
		Role role = new Role();
		role.setName("admin");
		role.setAbolishDate(DateUtils.MAX_DATE);
		role.setCreateDate(new Date());
		role.setValid(true);
		role.setRoleDesc("admin");
		role.save();
		Assert.assertNotNull(role.getId());
		User user = createUser();
		user.save();
		Assert.assertNotNull(user.getId());
		user.assignRole(role);
		List<RoleUserAuthorization> authorizations = RoleUserAuthorization.findAuthorizationByUser(user);
		Assert.assertEquals(1, authorizations.size());
		Assert.assertEquals("admin", authorizations.get(0).getRole().getName());
	}
	
	/**
	 * 为用户分配多个角色 
	 */
	@Test
	public void testAssignRoles() {
		Role adminRole = new Role();
		adminRole.setName("admin");
		adminRole.setAbolishDate(DateUtils.MAX_DATE);
		adminRole.setCreateDate(new Date());
		adminRole.setValid(true);
		adminRole.setRoleDesc("admin");
		adminRole.save();
		Assert.assertNotNull(adminRole);
		
		Role testRole = new Role();
		testRole.setName("test");
		testRole.setAbolishDate(DateUtils.MAX_DATE);
		testRole.setCreateDate(new Date());
		testRole.setValid(true);
		testRole.setRoleDesc("test");
		testRole.save();
		Assert.assertNotNull(adminRole);
		
		List<Role> roles = new ArrayList<Role>();
		roles.add(adminRole);
		roles.add(testRole);
		
		User user = createUser();
		user.save();
		Assert.assertNotNull(user);
		
		user.assignRole(roles);
		
		List<RoleUserAuthorization> authorizations = RoleUserAuthorization.findAuthorizationByUser(user);
		Set<Role> roles2 = extractRolesFrom(authorizations);
		Assert.assertTrue(roles2.containsAll(roles));
	}
	
	private Set<Role> extractRolesFrom(List<RoleUserAuthorization> authorizations) {
		Set<Role> results = new HashSet<Role>();
		for (RoleUserAuthorization each : authorizations) {
			results.add(each.getRole());
		}
		return results;
	}

	/**
	 * 废除角色
	 */
	@Test
	public void testAbolishRole() {
		Role adminRole = new Role();
		adminRole.setName("admin");
		adminRole.setAbolishDate(DateUtils.MAX_DATE);
		adminRole.setCreateDate(new Date());
		adminRole.setValid(true);
		adminRole.setRoleDesc("admin");
		adminRole.save();
		Assert.assertNotNull(adminRole);
		
		User user = createUser();
		user.save();
		Assert.assertNotNull(user);
		
		user.assignRole(adminRole);
		
		List<RoleUserAuthorization> authorizations = RoleUserAuthorization.findAuthorizationByUser(user);
		Assert.assertEquals(1, authorizations.size());
		Assert.assertEquals("admin", authorizations.get(0).getRole().getName());
		
		user.abolishRole(adminRole);
		Assert.assertEquals(0, RoleUserAuthorization.findAuthorizationByUser(user).size());
	}
	
	/**
	 * 创建一个用户
	 * @return
	 */
	private User createUser() {
		User user = new User();
		user.setUserAccount("zyb");
		user.setUserPassword("zyb");
		user.setUserDesc("test");
		user.setName("zyb");
		user.setLastLoginTime(new Date());
		user.setUserPassword("zyb");
		user.setCreateDate(new Date());
		user.setAbolishDate(DateUtils.MAX_DATE);
		return user;
	}
	
}
