package com.dayatang.cache.memcached;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.dayatang.cache.Cache;
import com.dayatang.utils.Assert;

/**
 * 基于Memcached的缓存实现
 * 
 * @author chencao
 * 
 */
public class MemCachedBasedCache implements Cache {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MemCachedBasedCache.class);

	private MemCachedClient mcc;

	private String[] servers;

	private int initConn = 100;

	private int minConn = 100;

	private int maxConn = 250;

	private int connectTimeout = 3000;

	private String poolName;

	private boolean initialized = false;

	public Object get(String key) {
		init();
		Object obj = mcc.get(key);
		debug("命中缓存：{}，key：{}", new Object[] { (obj != null), key });
		return obj;
	}

	@Override
	public Map<String, Object> get(String... keys) {
		init();
		Map<String, Object> map = mcc.getMulti(keys);
		return map;
	}

	public boolean isKeyInCache(String key) {
		init();
		return mcc.keyExists(key);
	}

	public void put(String key, Object value) {
		init();
		mcc.set(key, value);
		debug("缓存数据，key：{}", key);
	}

	public void put(String key, Object value, Date expiry) {
		init();
		mcc.set(key, value, expiry);
		debug("缓存数据，key：{}，过期日期：{}", key, expiry);
	}

	public void put(String key, Object value, long living) {
		init();
		Date now = new Date();
		Date expiry = new Date(now.getTime() + living);
		put(key, value, expiry);
	}

	public boolean remove(String key) {
		init();
		boolean result = mcc.delete(key);
		debug("删除缓存，key：{}", key);
		return result;
	}

	@SuppressWarnings("rawtypes")
	protected void prepareClient() {

		// grab an instance of our connection pool
		SockIOPool pool = null;
		if (StringUtils.isBlank(getPoolName())) {
			pool = SockIOPool.getInstance();
			mcc = new MemCachedClient();
		} else {
			pool = SockIOPool.getInstance(getPoolName());
			mcc = new MemCachedClient(getPoolName());
		}

		// Integer[] weights = { 5, 1 };

		// set the servers and the weights
		pool.setServers(servers);
		// pool.setWeights(weights);

		// set some basic pool settings
		// 5 initial, 5 min, and 250 max conns
		// and set the max idle time for a conn
		// to 6 hours
		pool.setInitConn(getInitConn());
		pool.setMinConn(getMinConn());
		pool.setMaxConn(getMaxConn());
		pool.setMaxIdle(1000 * 60 * 60 * 6);

		// set the sleep for the maint thread
		// it will wake up every x seconds and
		// maintain the pool size
		pool.setMaintSleep(30);
//		pool.setBufferSize(1024);

		// set some TCP settings
		// disable nagle
		// set the read timeout to 3 secs
		// and don't set a connect timeout
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setSocketConnectTO(getConnectTimeout());

		// initialize the connection pool
		pool.initialize();

		// lets set some compression on for the client
		// compress anything larger than 64k
		// mcc.setCompressEnable(true);
		// mcc.setCompressThreshold(64 * 1024);

		Map map = mcc.stats(servers);
		Set keys = map.keySet();
		if (keys.size() == 0) {
			error("客户端创建遇到错误，无法创建。");
		}
		for (Object key : keys) {
			// logger.info("客户端创建完成。key = 【{}】， status = 【{}】", key, map
			// .get(key));
			info("客户端创建完成。key = 【{}】", key);
		}

	}

	public String[] getServers() {
		return servers;
	}

	public void setServers(String[] servers) {
		this.servers = Arrays.copyOf(servers, servers.length);
	}

	/**
	 * 获取初始化连接数
	 * 
	 * @return 初始化连接数
	 */
	public int getInitConn() {
		return initConn;
	}

	/**
	 * 设置初始化连接数
	 * 
	 * @param initConn
	 *            初始化连接数
	 */
	public void setInitConn(int initConn) {
		this.initConn = initConn;
	}

	/**
	 * 获取最小连接数
	 * 
	 * @return 最小连接数
	 */
	public int getMinConn() {
		return minConn;
	}

	/**
	 * 设置最小连接数
	 * 
	 * @param minConn
	 *            最小连接数
	 */
	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}

	/**
	 * 获取最大连接数
	 * 
	 * @return 最大连接数
	 */
	public int getMaxConn() {
		return maxConn;
	}

	/**
	 * 设置最大连接数
	 * 
	 * @param maxConn
	 *            最大连接数
	 */
	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	/**
	 * 获取连接超时时间（毫秒）
	 * 
	 * @return 连接超时时间（毫秒）
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * 设置连接超时时间（毫秒）
	 * 
	 * @param connectTimeout
	 *            连接超时时间（毫秒）
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 获取socket pool名称
	 * 
	 * @return socket pool名称
	 */
	public String getPoolName() {
		return poolName;
	}

	/**
	 * 设置socket pool名称
	 * 
	 * @param poolName
	 *            socket pool名称
	 */
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public void init() {

		if (initialized) {
			return;
		}
		Assert.notEmpty(servers);

		for (String server : servers) {
			info("准备为Memcached服务器{}创建客户端...", server);
			info("最小连接数为：{}", minConn);
			info("最大接数为：{}", maxConn);
			info("初始化连接数为：{}", initConn);
			info("连接超时时间为：{}毫秒", connectTimeout);
		}
		prepareClient();
		// if (logger.isDebugEnabled()) {
		// logger.debug("客户端创建完成。...");
		// }
		initialized = true;

	}

	private void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}

	private void info(String message, Object... params) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(message, params);
		}
	}

	private void error(String message, Object... params) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, params);
		}
	}

}
