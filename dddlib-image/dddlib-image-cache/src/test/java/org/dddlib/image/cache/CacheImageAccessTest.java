package org.dddlib.image.cache;

import org.apache.commons.io.FileUtils;
import org.dayatang.cache.Cache;
import org.dayatang.cache.ehcache.EhCacheBasedCache;
import org.dayatang.cache.redis.RedisCacheBaseCahe;
import org.dddlib.image.api.ImageAccess;
import org.dddlib.image.api.pojo.ImageFile;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Created by lingen on 14-7-16.
 */
public class CacheImageAccessTest {

    private Cache cache = new EhCacheBasedCache("sampleCache");

    @Test
    public void testSaveImage() throws IOException {
        String file = "/Users/lingen/abc.jpg";
        ImageAccess imageAccess = new CacheImageAccess(cache);
        String uuid = imageAccess.saveImageFile(FileUtils.readFileToByteArray(new File(file)), "abc.jpg");
        System.out.println(uuid);
        assertTrue(uuid != null);
        ImageFile imageFile = imageAccess.getImageFile(uuid);
        assertTrue(imageFile.getContent() != null);
        assertTrue(imageFile.getFileName().equals("abc.jpg"));
    }

    @Test
    public void testRedisCache() throws IOException {
        String file = "/Users/lingen/abc.jpg";
        Cache redisCache = new RedisCacheBaseCahe("127.0.0.1", 6379);
        ImageAccess imageAccess = new CacheImageAccess(redisCache);
        String uuid = imageAccess.saveImageFile(FileUtils.readFileToByteArray(new File(file)), "abc.jpg");
        System.out.println(uuid);
        assertTrue(uuid != null);
        ImageFile imageFile = imageAccess.getImageFile(uuid);
        assertTrue(imageFile.getContent() != null);
        assertTrue(imageFile.getFileName().equals("abc.jpg"));
    }
}
