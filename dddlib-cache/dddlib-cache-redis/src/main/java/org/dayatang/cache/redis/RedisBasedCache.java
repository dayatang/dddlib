package org.dayatang.cache.redis;

import org.apache.commons.lang3.SerializationUtils;
import org.dayatang.cache.Cache;
import org.dayatang.cache.redis.pool.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.awt.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lingen on 14-7-15.
 * cache的redis实现
 */
public class RedisBasedCache implements Cache {

    private JedisPool jedisPool = null;

    public RedisBasedCache(String host, int port) {
        jedisPool = new JedisPool(JedisPoolUtil.createJedisPoolConfig(),host,port);
    }

    public RedisBasedCache(String host, int port, String password) {
        jedisPool = new JedisPool(JedisPoolUtil.createJedisPoolConfig(),host,port, 10000,password);
    }

    @Override
    public Object get(String key) {
        byte[] keyBytes = SerializationUtils.serialize(key);
        Jedis jedis = jedisPool.getResource();
        try {
            if (jedis.exists(keyBytes)) {
                byte[] valueBytes = jedis.get(keyBytes);
                return SerializationUtils.deserialize(valueBytes);
            }
        } finally {
            jedis.close();
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
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] keyBytes = SerializationUtils.serialize(key);
            byte[] valueBytes = SerializationUtils.serialize((Serializable) value);
            jedis.set(keyBytes, valueBytes);
        } finally {
            jedis.close();
        }
    }


    @Override
    public void put(String key, Object value, Date expiry) {
        Date now = new Date();
        long living = (expiry.getTime() - now.getTime()) / 1000;
        put(key, value, living);
    }

    @Override
    public void put(String key, Object value, long living) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] keyBytes = SerializationUtils.serialize(key);
            byte[] valueBytes = SerializationUtils.serialize((Serializable) value);
            jedis.setex(keyBytes, (int) living, valueBytes);
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean remove(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(SerializationUtils.serialize(key));
        } finally {
            jedis.close();
        }
        return false;
    }

    @Override
    public boolean containsKey(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(SerializationUtils.serialize(key));
        } finally {
            jedis.close();
        }
    }
}
