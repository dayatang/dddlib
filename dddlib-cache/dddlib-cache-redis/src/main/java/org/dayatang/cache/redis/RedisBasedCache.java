package org.dayatang.cache.redis;

import org.apache.commons.lang3.SerializationUtils;
import org.dayatang.cache.Cache;
import org.dayatang.cache.redis.pool.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

    private JedisTemplate template;

    public RedisBasedCache(String host, int port) {
        jedisPool = new JedisPool(JedisPoolUtil.createJedisPoolConfig(), host, port);
        template = new JedisTemplate(jedisPool);
    }

    public RedisBasedCache(String host, int port, String password) {
        jedisPool = new JedisPool(JedisPoolUtil.createJedisPoolConfig(), host, port, 10000, password);
        template = new JedisTemplate(jedisPool);
    }

    @Override
    public Object get(final String key) {
        return template.execute(new JedisFunction<Object>() {
            @Override
            public Object doInRedis(Jedis jedis) {
                final byte[] keyBytes = SerializationUtils.serialize(key);
                return SerializationUtils.deserialize(jedis.get(keyBytes));
            }
        });
    }

    @Override
    public Map<String, Object> get(final String... keys) {
        return template.execute(new JedisFunction<Map<String, Object>>() {
            @Override
            public Map<String, Object> doInRedis(Jedis jedis) {
                final Map<String, Object> values = new HashMap<String, Object>();
                for (String key : keys) {
                    byte[] keyBytes = SerializationUtils.serialize(key);
                    values.put(key, SerializationUtils.deserialize(jedis.get(keyBytes)));
                }
                return values;
            }
        });
    }

    @Override
    public void put(final String key, final Object value) {
        template.execute(new JedisAction() {
            @Override
            public void doInRedis(Jedis jedis) {
                byte[] keyBytes = SerializationUtils.serialize(key);
                byte[] valueBytes = SerializationUtils.serialize((Serializable) value);
                jedis.set(keyBytes, valueBytes);
            }
        });
    }


    @Override
    public void put(String key, Object value, Date expiry) {
        long livingSeconds = (expiry.getTime() - new Date().getTime()) / 1000;
        put(key, value, livingSeconds);
    }

    @Override
    public void put(final String key, final Object value, final long livingSeconds) {
        template.execute(new JedisAction() {
            @Override
            public void doInRedis(Jedis jedis) {
                byte[] keyBytes = SerializationUtils.serialize(key);
                byte[] valueBytes = SerializationUtils.serialize((Serializable) value);
                jedis.setex(keyBytes, (int) livingSeconds, valueBytes);
            }
        });
    }

    @Override
    public boolean remove(final String key) {
        return template.execute(new JedisFunction<Boolean>() {
            @Override
            public Boolean doInRedis(Jedis jedis) {
                try {
                    jedis.del(SerializationUtils.serialize(key));
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    @Override
    public boolean containsKey(final String key) {
        return template.execute(new JedisFunction<Boolean>() {
            @Override
            public Boolean doInRedis(Jedis jedis) {
                return jedis.exists(SerializationUtils.serialize(key));
            }
        });
    }
}
