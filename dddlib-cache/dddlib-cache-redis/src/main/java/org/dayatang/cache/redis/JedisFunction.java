package org.dayatang.cache.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by yyang on 15/9/8.
 */
public interface JedisFunction<T> {
    T doInRedis(Jedis jedis);
}
