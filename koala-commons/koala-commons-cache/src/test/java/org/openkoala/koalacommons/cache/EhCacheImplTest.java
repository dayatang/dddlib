package org.openkoala.koalacommons.cache;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.framework.cache.EhCacheImpl;
import org.openkoala.framework.cache.exception.UnExistsedCacheNameException;

import com.dayatang.cache.Cache;
import com.dayatang.utils.Assert;

/**
 * 针对Koala的ehcache实现做测试
 * @author lingen lingen.liu@gmail.com
 *
 */
public class EhCacheImplTest {

	
	/**
	 * 在ehcache.xml中配置指定cache名称进行cache
	 */
	@Test
	public void testCreatedCacheWithName(){
		Cache cache = new EhCacheImpl("sampleCache");
		Assert.isTrue(cache!=null);

	}
	
	/**
	 * 测试使用一个不存在的名称创建参数，则会抛出
	 */
	@Test(expected=UnExistsedCacheNameException.class)
	public void testCreatedCacheWithName2(){
		new EhCacheImpl("notExistsCacheName");
	}
	
	/**
	 * 使用参数手动构建一个cache
	 */
	@Test
	public void testCreatedCacheWithParams(){
		Cache cache = new EhCacheImpl("mycache",100000,true,true,1200000,200000);
		Assert.isTrue(cache!=null);
	}
	/**
	 * 测试从缓存中取一个KEY值的CACHE
	 */
	@Test
	public void testGet(){
		Cache cache = new EhCacheImpl("sampleCache");
		cache.put("KEY1", "Koala Project");
		Assert.isTrue("Koala Project".equals(cache.get("KEY1")));
		Assert.isNull(cache.get("KEY2"));
	}
	
	/**
	 * 传入一系列的KEY值
	 */
	@Test
	public void testGetArray(){
		Cache cache = new EhCacheImpl("sampleCache");
		cache.put("KEY1", "ABC");
		cache.put("KEY2", "VALUE2");
		cache.put("KEY4", "CHINA");
		Map<String,Object> result = cache.get("KEY1","KEY2","KEY3","KEY4");
		Assert.isTrue(result.size()==4);
		Assert.isTrue(result.get("KEY1").equals("ABC"));
		Assert.isNull(result.get("KEY3"));
	}
	
	
	/**
	 * 测试某个KEY值在缓存中是否存在
	 */
	@Test
	public void testIsKeyInCache(){
		Cache cache = new EhCacheImpl("sampleCache");
		cache.put("KEY1", "ABC");
		Assert.isTrue(cache.isKeyInCache("KEY1"));
		cache.remove("KEY1");
		Assert.isTrue(!cache.isKeyInCache("KEY1"));
	}
}
