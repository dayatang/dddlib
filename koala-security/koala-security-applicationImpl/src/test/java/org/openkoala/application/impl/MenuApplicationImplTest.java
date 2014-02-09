package org.openkoala.application.impl;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.auth.application.MenuApplication;
import org.openkoala.auth.application.ResourceTypeApplication;
import org.openkoala.auth.application.vo.ResourceTypeVO;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.exception.extend.ApplicationException;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.querychannel.support.Page;

/**
 * MenuApplicationImpl测试
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 15, 2013 9:13:23 AM
 */
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public class MenuApplicationImplTest extends KoalaBaseSpringTestCase {
	
	@Inject
	private MenuApplication menuApplication;
	
	@Inject 
	private ResourceTypeApplication resourceTypeApplication;
	
	private ResourceVO resourceVO;
	
	private ResourceTypeVO resourceTypeVO;

	@Before
	public void setUp() throws Exception {
		resourceVO = new ResourceVO();
		resourceVO.setName("testMenu");
		resourceVO.setDesc("testMenu");
		resourceVO.setIdentifier("test/menu.action");
		
		resourceTypeVO = new ResourceTypeVO();
		resourceTypeVO.setName("testResourceType");
		resourceTypeApplication.save(resourceTypeVO);
		assertNotNull(resourceTypeVO.getId());
		
		resourceVO.setMenuType(resourceTypeVO.getId());
		menuApplication.saveMenu(resourceVO);
		assertNotNull(resourceVO.getId());
	}
	
	@Test(expected = ApplicationException.class)
	public void testSave() {
		menuApplication.saveMenu(resourceVO);
	}

	@Test
	public void testGetMenu() {
		assertNotNull(menuApplication.getMenu(resourceVO.getId()));
	}

	@Test
	public void testUpdateMenu() {
		resourceVO.setName("updateMenu");
		menuApplication.updateMenu(resourceVO);
		assertEquals("updateMenu", menuApplication.getMenu(resourceVO.getId()).getName());
	}

	@Test
	public void testRemoveMenu() {
		menuApplication.removeMenu(resourceVO.getId());
	}

	@Test
	public void testFindAllMenu() {
		List<ResourceVO> results = menuApplication.findAllMenu();
		assertNotNull(results);
		assertTrue(results.size() > 0);
	}

	@Test
	public void testPageQueryMenu() {
		Page<ResourceVO> page = menuApplication.pageQueryMenu(1, 10);
		assertNotNull(page);
		assertTrue(page.getResult().size() > 0);
	}

	@Test
	public void testAssign() {
		ResourceVO child = new ResourceVO();
		child.setName("child");
		child.setIdentifier("test/ChildMenu.action");
		child.setMenuType(resourceTypeVO.getId());
		menuApplication.saveMenu(child);
		assertNotNull(child.getId());
		
		menuApplication.assign(resourceVO, child);
		
	}

}
