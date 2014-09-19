package org.dayatang.cache.ehcache;

import org.dayatang.cache.Cache;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by yyang on 14-2-28.
 */
public class EhCacheBasedCacheTest {

    /**
     * 在ehcache.xml中配置指定cache名称进行cache
     */
    @Test
    public void testCreatedCacheWithName() {
        Cache cache = new EhCacheBasedCache("sampleCache");
        assertTrue(cache != null);
    }

    /**
     * 测试使用一个不存在的名称创建参数，则会抛出
     */
    @Test(expected = UnExistsedCacheNameException.class)
    public void testCreatedCacheWithName2() {
        new EhCacheBasedCache("notExistsCacheName");
    }

    /**
     * 使用参数手动构建一个cache
     */
    @Test
    public void testCreatedCacheWithParams() {
        Cache cache = new EhCacheBasedCache("mycache", 100000, true, true, 1200000, 200000);
        assertTrue(cache != null);
    }

    /**
     * 使用参数手动构建一个cache
     */
    @Test
    public void testCreatedCacheWithConfiguration() {
        EhCacheConfiguration configuration = EhCacheConfiguration.builder().name("mycache")
                .maxElementsInMemory(100000).overflowToDisk(true).eternal(true)
                .timeToLiveSeconds(1200000).timeToIdleSeconds(200000).build();
        Cache cache = new EhCacheBasedCache(configuration);
        assertTrue(cache != null);
    }

    /**
     * 测试从缓存中取一个KEY值的CACHE
     */
    @Test
    public void testGet() {
        Cache cache = new EhCacheBasedCache("sampleCache");
        cache.put("KEY1", "Koala Project");
        assertTrue("Koala Project".equals(cache.get("KEY1")));
        assertNull(cache.get("KEY2"));
        cache.remove("KEY1");
    }

    /**
     * 传入一系列的KEY值
     */
    @Test
    public void testGetArray() {
        Cache cache = new EhCacheBasedCache("sampleCache");
        cache.put("KEY1", "ABC");
        cache.put("KEY2", "VALUE2");
        cache.put("KEY4", "CHINA");
        Map<String, Object> result = cache.get("KEY1", "KEY2", "KEY3", "KEY4");
        assertTrue(result.size() == 4);
        assertTrue(result.get("KEY1").equals("ABC"));
        assertNull(result.get("KEY3"));
        cache.remove("KEY1");
        cache.remove("KEY2");
        cache.remove("KEY4");
    }


    /**
     * 测试某个KEY值在缓存中是否存在
     */
    @Test
    public void testIsKeyInCache() {
        Cache cache = new EhCacheBasedCache("sampleCache");
        cache.put("KEY1", "ABC");
        assertTrue(cache.containsKey("KEY1"));
        cache.remove("KEY1");
        assertTrue(!cache.containsKey("KEY1"));
    }

    @Test
    public void testExpiredCache() throws InterruptedException {
        Cache cache = new EhCacheBasedCache("sampleCache");
        assertTrue(!cache.containsKey("KEY1"));
        cache.put("KEY1", "ABC",3);
        assertTrue(cache.containsKey("KEY1"));
        Thread.sleep(4*1000);
        assertTrue(!cache.containsKey("KEY1"));
    }
}
