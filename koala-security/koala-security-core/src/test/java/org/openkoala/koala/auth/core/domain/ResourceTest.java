/**
 * 
 */
package org.openkoala.koala.auth.core.domain;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.dayatang.utils.DateUtils;

/**
 * 资源测试
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 13, 2013 3:34:33 PM
 */
public class ResourceTest {

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#findResourceByRole(java.lang.Long)}.
	 */
	@Test
	public void testFindResourceByRole() {
		
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#findChildByParent(java.lang.Long)}.
	 */
	@Test
	public void testFindChildByParent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#findChildByParentAndUser(java.lang.Long, java.lang.String)}.
	 */
	@Test
	public void testFindChildByParentAndUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#removeResource()}.
	 */
	@Test
	public void testRemoveResource() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#assignParent(org.openkoala.koala.auth.core.domain.Resource)}.
	 */
	@Test
	public void testAssignParent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#assignChild(org.openkoala.koala.auth.core.domain.Resource)}.
	 */
	@Test
	public void testAssignChild() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#findRoleByResource(java.lang.String)}.
	 */
	@Test
	public void testFindRoleByResource() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#hasPrivilegeByRole(java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testHasPrivilegeByRole() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#hasPrivilegeByUser(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testHasPrivilegeByUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.openkoala.koala.auth.core.domain.Resource#hasChildByParent(java.lang.Long)}.
	 */
	@Test
	public void testHasChildByParent() {
		fail("Not yet implemented");
	}

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
