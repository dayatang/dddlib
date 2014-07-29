package org.dayatang.cache.redis.pool;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by lingen on 14-7-16.
 */
public class JedisPoolUtil {

    /**
     * maxActive：控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
     * 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态就成exhausted了
     */
    private static int MAX_ACTIVE = -1;
    /**
     * maxIdle：控制一个pool最多有多少个状态为idle的jedis实例；
     */
    private static int MAX_IDLE = -1;
    /**
     * maxWait：表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；(in milliseconds单位:毫秒)
     */
    private static long MAX_WAIT = 1000l;
    /**
     * testOnBorrow：在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
     */
    private static boolean TEST_ON_BORROW = true;
    /**
     * testOnReturn：在return给pool时，是否提前进行validate操作；
     */
    private static boolean TEST_ON_RETURN = false;

    private JedisPoolUtil() {
    }

    public static JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_ACTIVE);
        config.setMaxIdle(MAX_IDLE);
        config.setMaxWaitMillis(MAX_WAIT);
        config.setTestOnBorrow(TEST_ON_BORROW);
        config.setTestOnReturn(TEST_ON_RETURN);
        return config;
    }

}
