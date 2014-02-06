package org.dayatang.cache.memcached;

import org.dayatang.cache.Cache;
import org.dayatang.springtest.AbstractSpringIntegrationTest;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.*;

public class MemCachedBasedCacheTest extends AbstractSpringIntegrationTest {

    @Inject
    private Cache cache;

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

        assertFalse(cache.isKeyInCache("no-exist"));

        Date now = new Date();
        cache.put("time", now);
        assertTrue(cache.isKeyInCache("time"));

    }
}
