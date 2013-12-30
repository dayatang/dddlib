/**
 * 
 */
package org.openkoala.koala.auth.core.domain;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.utils.DateUtils;

/**
 * 资源测试
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 13, 2013 3:34:33 PM
 */
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public class ResourceTest extends KoalaBaseSpringTestCase {


	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#isNameExist()}.
	 */
	@Test
	public void testIsNameExist() {
		Resource resource = createResource();
		resource.save();
		assertNotNull(resource);
		assertTrue(resource.isNameExist());
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#isIdentifierExist()}.
	 */
	@Test
	public void testIsIdentifierExist() {
		Resource resource = createResource();
		resource.save();
		assertNotNull(resource);
		assertTrue(resource.isIdentifierExist());
		
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#disableResource()}.
	 */
	@Test
	public void testDisableResource() {
		Resource resource = createResource();
		resource.save();
		
		resource.disableResource();
		assertFalse(Resource.get(Resource.class, resource.getId()).isValid());
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#enableResource()}.
	 */
	@Test
	public void testEnableResource() {
		Resource resource = createResource();
		resource.save();
		
		resource.enableResource();
		assertTrue(Resource.get(Resource.class, resource.getId()).isValid());
	}
	
	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#assignRole()}.
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#findResourceByRole()}.
	 */
	@Test
	public void testAssignRoleAndFindResourceByRole() {
		Role role = new Role("role-name", "role-description");
		role.save();
		
		Resource resource = createResource();
		resource.save();
		assertFalse(Resource.findResourceByRole(role.getId()).contains(resource));
		
		resource.assignRole(role);
		assertTrue(Resource.findResourceByRole(role.getId()).contains(resource));
	}
	
	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#assignChild()}.
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#findChildByParent()}.
	 */
	@Test
	public void testAssignChildAndFindChildByParent() {
		Resource parent = createResource();
		parent.setName("parent-name");
		parent.setIdentifier("parent-identifier");
		parent.save();
		
		Resource child = createResource();
		child.setName("child-name");
		child.setIdentifier("child-identifier");
		child.save();
		
		assertFalse(Resource.findChildByParent(parent.getId()).contains(child));
		
		parent.assignChild(child);
		assertTrue(Resource.findChildByParent(parent.getId()).contains(child));
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#findChildByParentAndUser()}.
	 */
	@Test
	public void testFindChildByParentAndUser() {
		User user = new User("user-name", "user-account", "user-password", "user-desription");
		user.save();
		Role role = new Role("role-name", "role-description");
		role.save();
		role.assignUser(user);
		
		Resource parent = createResource();
		parent.setName("parent-name");
		parent.setIdentifier("parent-identifier");
		parent.save();
		
		Resource child = createResource();
		child.setName("child-name");
		child.setIdentifier("child-identifier");
		child.save();
		
		parent.assignChild(child);
		assertFalse(Resource.findChildByParentAndUser(parent.getId(), "user-account").contains(child));
		
		parent.assignRole(role);
		child.assignRole(role);
		assertTrue(Resource.findChildByParentAndUser(parent.getId(), "user-account").contains(child));
	}
	
	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#removeResource()}.
	 */
	@Test
	public void testRemoveResource() {
		Resource resource = createResource();
		resource.save();
		ResourceType resourceType = new ResourceType();
		resourceType.setName("resource-type-name");
		resourceType.setCreateDate(new Date());
		resourceType.setAbolishDate(DateUtils.MAX_DATE);
		resourceType.save();
		
		ResourceTypeAssignment resourceTypeAssignment = new ResourceTypeAssignment();
		resourceTypeAssignment.setResource(resource);
		resourceTypeAssignment.setResourceType(resourceType);
		resourceTypeAssignment.setCreateDate(new Date());
		resourceTypeAssignment.setAbolishDate(DateUtils.MAX_DATE);
		resourceTypeAssignment.save();
		
		assertEquals(resource, Resource.get(Resource.class, resource.getId()));
		
		resource.removeResource();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		assertEquals(dateFormat.format(new Date()), 
				dateFormat.format(Resource.get(Resource.class, resource.getId()).getAbolishDate()));
	}
	
	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#assignParent()}.
	 */
	@Test
	public void testAssignParent() {
		Resource parent = createResource();
		parent.setName("parent-name");
		parent.setIdentifier("parent-identifier");
		parent.save();
		
		Resource child = createResource();
		child.setName("child-name");
		child.setIdentifier("child-identifier");
		child.save();
		
		assertFalse(Resource.findChildByParent(parent.getId()).contains(child));
		
		child.assignParent(parent);
		assertTrue(Resource.findChildByParent(parent.getId()).contains(child));
	}
	
	@Test
	public void testFindRoleByResource() {
		Role role = new Role();
		role.setName("role-name");
		role.setRoleDesc("role-desc");
		role.setValid(true);
		role.setCreateDate(new Date());
		role.setAbolishDate(DateUtils.MAX_DATE);
		role.save();
		assertNotNull(role.getId());
		
		Resource resource = createResource();
		resource.save();
		assertNotNull(resource.getId());
		assertFalse(Resource.hasPrivilegeByRole(resource.getId(), role.getId()));
		
		resource.assignRole(role);
		assertTrue(Resource.hasPrivilegeByRole(resource.getId(), role.getId()));
		
		List<Role> roles = Resource.findRoleByResource(resource.getIdentifier());
		assertTrue(!roles.isEmpty());
		assertEquals(1, roles.size());
	}
	
	@Test
	public void testHasPrivilegeByRole() {
		Role role = new Role();
		role.setName("role-name");
		role.setRoleDesc("role-desc");
		role.setValid(true);
		role.setCreateDate(new Date());
		role.setAbolishDate(DateUtils.MAX_DATE);
		role.save();
		assertNotNull(role.getId());
		
		Resource resource = createResource();
		resource.save();
		assertNotNull(resource.getId());
		assertFalse(Resource.hasPrivilegeByRole(resource.getId(), role.getId()));
		
		resource.assignRole(role);
		assertTrue(Resource.hasPrivilegeByRole(resource.getId(), role.getId()));
	}
	
	@Test
	public void testHasChildByParent() {
		Resource parent = createResource();
		parent.setName("parent-name");
		parent.setIdentifier("parent-identifier");
		parent.save();
		
		Resource child = createResource();
		child.setName("child-name");
		child.setIdentifier("child-identifier");
		child.save();
		
		assertFalse(Resource.findChildByParent(parent.getId()).contains(child));
		
		child.assignParent(parent);
		assertTrue(Resource.findChildByParent(parent.getId()).contains(child));
		
		assertTrue(Resource.hasChildByParent(parent.getId()));
	}
	
	@Test
	public void testRemoveAll() {
		Resource resource1 = createResource();
		resource1.save();
		assertNotNull(resource1.getId());
		
		Resource resource2 = createResource();
		resource2.save();
		assertNotNull(resource2.getId());
		
		Resource.removeAll();
		
		List<Resource> resources = Resource.findAll(Resource.class);
		assertTrue(resources.isEmpty());
	}
	
	@Test
	public void testIsMenu() {
		assertFalse(Resource.isMenu(createResource()));
	}
	
	@Test
	public void testGetRootResources() {
		List<Resource> resources = Resource.getRootResources();
		System.out.println(resources);
	}
	
	/**
	 * 创建资源
	 * @return
	 */
	private Resource createResource() {
		Resource resource = new Resource();
		resource.setAbolishDate(DateUtils.MAX_DATE);
		resource.setCreateDate(new Date());
		resource.setName("testResource");
		resource.setIdentifier("testIdentifier");
		return resource;
	}
}
