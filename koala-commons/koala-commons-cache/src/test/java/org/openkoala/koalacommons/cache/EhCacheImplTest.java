package org.openkoala.koalacommons.cache;

import org.junit.Test;
import org.openkoala.framework.cache.EhCacheImpl;

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
	public void testCache(){
		Cache cache = new EhCacheImpl("sampleCache");
		cache.put("KEY", "VALUE");
		String value = (String) cache.get("KEY");
		Assert.isTrue(value.equals("VALUE"));
		
		boolean exists = cache.isKeyInCache("KEY");
		Assert.isTrue(exists);
		
		cache.remove("KEY");
		exists = cache.isKeyInCache("KEY");
		Assert.isTrue(!exists);
	}
	
	/**
	 * 使用参数手动构建一个cache
	 */
	@Test
	public void testCache2(){
		Cache cache = new EhCacheImpl("mycache",100000,true,true,1200000,200000);
		cache.put("KEY", "VALUE");
		String value = (String) cache.get("KEY");
		Assert.isTrue(value.equals("VALUE"));
		
		boolean exists = cache.isKeyInCache("KEY");
		Assert.isTrue(exists);
		
		cache.remove("KEY");
		exists = cache.isKeyInCache("KEY");
		Assert.isTrue(!exists);
		
	}
	
	
}
