package org.dayatang.cache.memcached;

import java.util.Date;

import static org.junit.Assert.*;

import org.dayatang.cache.Cache;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.springtest.PureSpringTestCase;
import org.junit.Before;
import org.junit.Test;

public class MemCachedBasedCacheTest extends PureSpringTestCase {

    private Cache cache;

    @Before
    @Override
    public void setup() {
        super.setup();
        cache = InstanceFactory.getInstance(Cache.class, "memcached");
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

        assertFalse(cache.isKeyInCache("no-exist"));

        Date now = new Date();
        cache.put("time", now);
        assertTrue(cache.isKeyInCache("time"));

    }
}
