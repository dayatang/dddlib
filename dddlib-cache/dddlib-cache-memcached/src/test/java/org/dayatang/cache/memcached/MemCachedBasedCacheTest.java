package org.dayatang.cache.memcached;

import org.dayatang.cache.Cache;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class MemcachedBasedCacheTest {

    private Cache cache = createCache();

    private Cache createCache() {
        MemcachedBasedCache result = new MemcachedBasedCache();
        result.setServers("localhost:11211");
        result.setInitConn(3);
        result.setMinConn(3);
        result.setMaxConn(5);
        return result;
    }

    @Test
    public void notnull() {
        assertNotNull(cache);
    }

    @Test
    public void get() {
        Object obj = cache.get("hehe");
        assertNull(obj);
    }

    @Test
    public void put() throws InterruptedException {
        try {
            Thread.sleep(1000);
            Date now = new Date();
            cache.put("time", now);
            Date obj = (Date) cache.get("time");
            assertNotNull(obj);
            assertEquals(now, obj);
        } catch (RuntimeException ex) {
            System.err.println("出错了..." + ex);
            throw ex;
        }
    }

    @Test
    public void remove() {

        assertFalse(cache.remove("no-exist"));

        Date now = new Date();
        cache.put("time", now);
        boolean delete = cache.remove("time");
        assertTrue(delete);

    }

    @Test
    public void isKeyInCache() {

        assertFalse(cache.containsKey("no-exist"));

        Date now = new Date();
        cache.put("time", now);
        assertTrue(cache.containsKey("time"));

    }
}
