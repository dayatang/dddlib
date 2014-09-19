package org.dayatang.cache.ehcache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.dayatang.cache.Cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yyang on 14-2-28.
 */
public class EhCacheBasedCache implements Cache {

    /**
     * cache属性
     */
    private net.sf.ehcache.Cache cache;

    /**
     * 使用指定的名字构建一个缓存，name对应ehcache.xml中的配置
     *
     * @param name
     */
    public EhCacheBasedCache(String name) {
        cache = CacheManager.getInstance().getCache(name);
        if (cache == null) {
            throw new UnExistsedCacheNameException();
        }
    }


    /**
     * 指定配置，构建一个缓存对象
     *
     * @param configuration EhCache配置信息
     */
    public EhCacheBasedCache(EhCacheConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalStateException("Configuration is null!");
        }
        if (!CacheManager.getInstance().cacheExists(configuration.name())) {
            cache = new net.sf.ehcache.Cache(configuration.name(), configuration.maxElementsInMemory(),
                    configuration.overflowToDisk(), configuration.eternal(),
                    configuration.timeToLiveSeconds(), configuration.timeToIdleSeconds());
            CacheManager.getInstance().addCache(cache);
        }
    }


    /**
     * 指定详细的参数，构建一个缓存对象
     *
     * @param name
     * @param maxElementsInMemory
     * @param overflowToDisk
     * @param eternal
     * @param timeToLiveSeconds
     * @param timeToIdleSeconds
     */
    public EhCacheBasedCache(String name, int maxElementsInMemory, boolean overflowToDisk, boolean eternal, long timeToLiveSeconds, long timeToIdleSeconds) {
        if (!CacheManager.getInstance().cacheExists(name)) {
            cache = new net.sf.ehcache.Cache(name, maxElementsInMemory, overflowToDisk, eternal,
                    timeToLiveSeconds, timeToIdleSeconds);
            CacheManager.getInstance().addCache(cache);
        }
    }

    /**
     * 从缓存中获取一个KEY值
     */
    public Object get(String key) {
        Element el = (Element) cache.get(key);
        if (el != null) {
            return el.getObjectValue();
        }
        return null;
    }

    /**
     * 传入一系列的KEY值，返回一个MAP
     */
    public Map<String, Object> get(String... keys) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (String key : keys) {
            Object val = get(key);
            result.put(key, val);
        }
        return result;
    }


    /**
     * 判定一个KEY值是否在缓存中存在
     */
    public boolean containsKey(String key) {
        return cache.isKeyInCache(key) && cache.get(key)!=null;
    }

    /**
     * 向缓存中存入一个键值对
     */
    public void put(String key, Object obj) {
        cache.put(new Element(key, obj));
    }


    /**
     * 向缓存中存入一个键值对，到指定的时间过期
     *
     * @param key         指定对象的key
     * @param obj
     * @param expiredDate
     */
    public void put(String key, Object obj, Date expiredDate) {
        Date now = new Date();
        long timeToLiveSeconds = (expiredDate.getTime() - now.getTime()) / 1000;
        put(key, obj, timeToLiveSeconds);

    }

    /**
     * 向缓存中存入一个键值对，指定其生存的秒数
     *
     * @param key               指定对象的key
     * @param obj
     * @param timeToLiveSeconds
     */
    public void put(String key, Object obj, long timeToLiveSeconds) {
        cache.put(new Element(key, obj, false, (int) timeToLiveSeconds, (int) timeToLiveSeconds));
    }

    /**
     * 从缓存中将某一个KEY值移除出去
     */
    public boolean remove(String key) {

        return cache.remove(key);
    }
}
