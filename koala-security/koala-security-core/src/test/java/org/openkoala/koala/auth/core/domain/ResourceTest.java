/**
 * 
 */
package org.openkoala.koala.auth.core.domain;

import static org.junit.Assert.*;

import java.util.Date;

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
