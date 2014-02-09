package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.utils.DateUtils;

/**
 * 角色测试
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 12, 2013 5:14:56 PM
 */
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public class RoleTest extends KoalaBaseSpringTestCase {

	/**
	 * 分配用户
	 */
	@Test
	public void testAssignUser() {
		User user = new User("admin", "admin", "admin", "admin");
		user.save();
		assertNotNull(user.getId());
		
		Role role = new Role("adminRole", "adminRole");
		role.save();
		assertNotNull(role);
		
		role.assignUser(user);
		
		List<RoleUserAuthorization> roleUserAuthorizations = RoleUserAuthorization.findUserAuthorizationByRole(role.getId());
		Set<User> users = extractUserFrom(roleUserAuthorizations);
		assertTrue(users.contains(user));
	}
	
	private Set<User> extractUserFrom(List<RoleUserAuthorization> roleUserAuthorizations) {
		Set<User> users = new HashSet<User>();
		for (RoleUserAuthorization each : roleUserAuthorizations) {
			users.add(each.getUser());
		}
		return users;
	}

	/**
	 * 分配资源
	 */
	@Test
	public void testAssgnResource() {
		Resource resource = new Resource();
		resource.setAbolishDate(DateUtils.MAX_DATE);
		resource.setCreateDate(new Date());
		resource.setIdentifier("test");
		resource.setName("test");
		resource.save();
		assertNotNull(resource);
		
		Role role = new Role("adminRole", "adminRole");
		role.save();
		assertNotNull(role);
		
		role.assignResource(resource);
		
		List<IdentityResourceAuthorization> identityResourceAuthorizations = IdentityResourceAuthorization.findAuthorizationByRole(role.getId());
		
		Set<Resource> resources = extractResourceFrom(identityResourceAuthorizations);
		assertTrue(resources.contains(resource));
	}
	
	private Set<Resource> extractResourceFrom(List<IdentityResourceAuthorization> identityResourceAuthorizations) {
		Set<Resource> resources = new HashSet<Resource>();
		for (IdentityResourceAuthorization each : identityResourceAuthorizations) {
			resources.add(each.getResource());
		}
		return resources;
	}

	/**
	 * 废除资源
	 */
	@Test
	public void testAbolishResource() {
		Resource resource = new Resource();
		resource.setAbolishDate(DateUtils.MAX_DATE);
		resource.setCreateDate(new Date());
		resource.setIdentifier("test");
		resource.setName("test");
		resource.save();
		assertNotNull(resource);
		
		Resource resource1 = new Resource();
		resource1.setAbolishDate(DateUtils.MAX_DATE);
		resource1.setCreateDate(new Date());
		resource1.setIdentifier("test");
		resource1.setName("test");
		resource1.save();
		assertNotNull(resource1);
		
		Role role = new Role("adminRole", "adminRole");
		role.save();
		assertNotNull(role);
		
		role.assignResource(resource);
		role.assignResource(resource1);
		
		role.abolishResource(resource);
		role.abolishResource(resource1);
		
		assertTrue(role.getIdentityResourceAuthorizations().isEmpty());
	}
	
	/**
	 * 根据角色名查找角色 
	 */
	@Test
	public void testFindRoleByName() {
		Role role = new Role("adminRole", "adminRole");
		role.save();
		assertNotNull(role);
		
		assertEquals("adminRole", Role.findRoleByName("adminRole").getName());
	}
	
	/**
	 * 根据用户账号查找角色 
	 */
	@Test
	public void testFindRoleByUserAccount() {
		User user = new User("admin", "admin", "admin", "admin");
		user.save();
		assertNotNull(user.getId());
		
		Role role = new Role("adminRole", "adminRole");
		role.save();
		assertNotNull(role);
		
		Role role2 = new Role("testRole", "testRole");
		role2.save();
		assertNotNull(role2);
		
		user.assignRole(role);
		user.assignRole(role2);
		
		List<Role> roles = Role.findRoleByUserAccount("admin");
		assertTrue(roles.contains(role));
		assertTrue(roles.contains(role2));
		
	}
	
	/**
	 * 角色是否已经存在
	 */
	@Test
	public void testIsRoleExist() {
		Role role = new Role("adminRole", "adminRole");
		role.save();
		assertNotNull(role);
		
		assertTrue(role.isRoleExist());
	}
	
}
