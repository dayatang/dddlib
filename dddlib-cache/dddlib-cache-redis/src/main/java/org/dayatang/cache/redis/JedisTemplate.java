package org.dayatang.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by yyang on 15/9/8.
 */
public class JedisTemplate {

    private JedisPool jedisPool;

    public JedisTemplate(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public <T> T execute(JedisFunction<T> jedisOperation) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedisOperation.doInRedis(jedis);
        } finally {
            jedis.close();
        }
    }

    public void execute(JedisAction jedisOperation) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedisOperation.doInRedis(jedis);
        } finally {
            jedis.close();
        }
    }
}
