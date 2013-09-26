package org.openkoala.cache.sample; 

import java.util.Date;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dayatang.cache.Cache;

/**
 * DDDLib的缓存测试，目前DDDLib只提供了MemCached的实现
 * @author Justin
 * @since 2012-10-25
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-cache.xml" })
public class MemCachedClientTest {
	
	@Autowired
	private Cache cache;
	
	/**
	 * 测试从缓存中获取数据
	 */
	@Test
	public void testGetFromCache() {
		User user = new User();
		user.setName("Justin");
		cache.put("user", user);
		
		User cachedUser = (User) cache.get("user");
		Assert.assertNotNull(user);
		Assert.assertEquals("Justin", cachedUser.getName());
	}
	
	/**
	 * 测试从缓存中获取数据（指定失效时间）
	 */
	@Test
	public void testGetFromCacheForExpire() throws Exception {
		User user = new User();
		user.setName("Justin");
		Date expireDate = new Date(new Date().getTime() + 1000);
		// 指定缓存1秒后失效
		cache.put("expireUser", user, expireDate);
		Thread.sleep(2000);
		
		User cachedUser = (User) cache.get("expireUser");
		Assert.assertNull(cachedUser);
	}
	
	/**
	 * 测试从缓存中获取数据（指定缓存对象的存活时间）
	 */
	@Test
	public void testGetFromCacheForLiving() throws Exception {
		User user = new User();
		user.setName("Justin");
		// 指定缓存1秒后失效
		cache.put("livingUser", user, 1000);
		Thread.sleep(2000);
		
		User cachedUser = (User) cache.get("livingUser");
		Assert.assertNull(cachedUser);
	}
	
	/**
	 * 测试从缓存中移除缓存对象
	 */
	@Test
	public void testRemoveFromCache() {
		User cachedUser = (User) cache.get("user");
		boolean result = cache.remove("user");
		Assert.assertEquals(true, result);
		Assert.assertNotNull(cachedUser);
	}
	
	/**
	 * 测试根据keys从缓存中获取Map对象 
	 */
	@Test
	public void testGetMapFromCacheByKeys() {
		cache.put("one", 1);
		cache.put("two", 2);
		cache.put("three", 3);
		Map<String, Object> map = cache.get("one", "two", "three");
		Assert.assertEquals(1, map.get("one"));
		Assert.assertEquals(2, map.get("two"));
		Assert.assertEquals(3, map.get("three"));
	}
	
	/**
	 * 测试key是否已经存在缓存中
	 */
	@Test
	public void testKeyIsExistInCache() {
		boolean result = cache.isKeyInCache("one");
		Assert.assertEquals(true, result);
	}
}
