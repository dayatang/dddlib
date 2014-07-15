package org.dayatang.cache.redis;

import org.apache.commons.lang3.SerializationUtils;
import org.dayatang.cache.Cache;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lingen on 14-7-15.
 * cache的redis实现
 */
public class RedisCacheBaseCahe implements Cache {

    private Jedis jedis;

    public RedisCacheBaseCahe(String host, int port) {
        this.jedis = new Jedis(host, port);
    }

    public RedisCacheBaseCahe(String host, int port, String password) {
        this.jedis = new Jedis(host, port);
        if (password != null) {
            jedis.auth(password);
        }
    }

    @Override
    public Object get(String key) {
        byte[] keyBytes = SerializationUtils.serialize(key);
        if (jedis.exists(keyBytes)) {
            byte[] valueBytes = jedis.get(keyBytes);
            return SerializationUtils.deserialize(valueBytes);
        }
        return null;
    }

    @Override
    public Map<String, Object> get(String... keys) {
        Map<String, Object> values = new HashMap<String, Object>();
        for (String key : keys) {
            values.put(key, get(key));
        }
        return values;
    }

    @Override
    public void put(String key, Object value) {
        byte[] keyBytes = SerializationUtils.serialize(key);
        byte[] valueBytes = SerializationUtils.serialize((Serializable) value);
        jedis.set(keyBytes, valueBytes);
    }


    @Override
    public void put(String key, Object value, Date expiry) {
        Date now = new Date();
        long living = expiry.getTime() - now.getTime();
        put(key, value, living);
    }

    @Override
    public void put(String key, Object value, long living) {
        byte[] keyBytes = SerializationUtils.serialize(key);
        byte[] valueBytes = SerializationUtils.serialize((Serializable) value);
        jedis.setex(keyBytes, (int) living, valueBytes);

    }

    @Override
    public boolean remove(String key) {
        jedis.del(SerializationUtils.serialize(key));
        return false;
    }

    @Override
    public boolean containsKey(String key) {
        return jedis.exists(SerializationUtils.serialize(key));
    }
}
