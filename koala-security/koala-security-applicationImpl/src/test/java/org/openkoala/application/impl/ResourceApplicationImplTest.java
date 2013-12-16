package org.openkoala.application.impl;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.auth.application.ResourceApplication;
import org.openkoala.auth.application.ResourceTypeApplication;
import org.openkoala.auth.application.vo.ResourceTypeVO;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Assert;

/**
 * ResourceApplicationImpl测试
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 14, 2013 11:05:57 AM
 */
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public class ResourceApplicationImplTest extends KoalaBaseSpringTestCase {
	
	@Inject
	private ResourceApplication resourceApplication;
	
	@Inject
	private ResourceTypeApplication resourceTypeApplication;
	
	
	private ResourceVO resourceVO;
	
	private ResourceTypeVO resourceTypeVO;
	

	public void setUp() {
		resourceVO = new ResourceVO();
		resourceVO.setName("testResource");
		resourceVO.setDesc("testResource");
		resourceVO.setIdentifier("testIdentifier");
		
		resourceTypeVO = new ResourceTypeVO();
		resourceTypeVO.setName("testResourceType");
		resourceTypeApplication.save(resourceTypeVO);
		assertNotNull(resourceTypeVO.getId());
		
		resourceVO.setTypeId(resourceTypeVO.getId());
		resourceApplication.saveResource(resourceVO);
		assertNotNull(resourceVO.getId());
	}
	
	@Test
	public void testIsResourceEmpty(){
		Assert.isTrue(resourceApplication.isResourceEmpty());
		setUp();
		Assert.isTrue(resourceApplication.isResourceEmpty()==false);
	}

	@Test
	public void testAssignAndFindResourceTree() {
		setUp();
		ResourceVO child = new ResourceVO();
		child.setName("child");
		child.setDesc("child");
		child.setIdentifier("child");
		child.setTypeId(resourceTypeVO.getId());
		resourceApplication.saveResource(child);
		assertNotNull(child.getId());
		
		resourceApplication.assign(resourceVO, child);
		
		assertNotNull(resourceApplication.findResourceTree());
	}

	@Test
	public void testGetResource() {
		setUp();
		ResourceVO result = resourceApplication.getResource(resourceVO.getId());
		assertNotNull(result);
	}

	@Test
	public void testUpdateResource() {
		setUp();
		resourceVO.setName("updateName");
		resourceApplication.updateResource(resourceVO);
		assertEquals("updateName", resourceApplication.getResource(resourceVO.getId()).getName());
	}

	@Test
	public void testRemoveResource() {
		setUp();
		resourceApplication.removeResource(resourceVO.getId());
	}

	@Test
	public void testIsNameExist() {
		setUp();
		assertTrue(resourceApplication.isNameExist(resourceVO));
	}

	@Test
	public void testIsIdentifierExist() {
		setUp();
		assertTrue(resourceApplication.isIdentifierExist(resourceVO));
	}

}
