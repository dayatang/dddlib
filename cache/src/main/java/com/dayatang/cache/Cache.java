package com.dayatang.cache;

import java.util.Date;
import java.util.Map;

/**
 * 缓存接口
 * 
 * @author chencao
 * 
 */
/**
 * @author chencao
 * 
 */
public interface Cache {
	/**
	 * 根据指定key，从缓存中获取对象
	 * 
	 * @param key
	 *            对象的key
	 * @return 缓存中的对象
	 */
	 Object get(String key);

	/**
	 * 根据指定key集合，从缓存中获取对象Map
	 * 
	 * @param keys
	 *            对象的key集合
	 * @return 缓存中的对象Map
	 */
	 Map<String, Object> get(String... keys);

	/**
	 * 把对象以key的形式放入缓存（同名key覆盖）
	 * 
	 * @param key
	 *            指定对象的key
	 * @param value
	 *            放入缓存的对象
	 */
	 void put(String key, Object value);

	/**
	 * 把对象以key的形式放入缓存（同名key覆盖）
	 * 
	 * @param key
	 *            指定对象的key
	 * @param value
	 *            放入缓存的对象
	 * @param expiry
	 *            缓存过期日期
	 */
	 void put(String key, Object value, Date expiry);

	/**
	 * 把对象以key的形式放入缓存（同名key覆盖）
	 * 
	 * @param key
	 *            指定对象的key
	 * @param value
	 *            放入缓存的对象
	 * @param living
	 *            缓存存活时间（毫秒）
	 */
	 void put(String key, Object value, long living);

	/**
	 * 删除key所对应的缓存，key不存在不报错
	 * 
	 * @param key
	 *            需要删除缓存对象的key
	 * @return true=成功，false=失败
	 */
	 boolean remove(String key);

	/**
	 * 判断key是否已经已存在
	 * 
	 * @param key
	 *            缓存对象的key
	 * @return true=存在，false=不存在
	 */
	 boolean isKeyInCache(String key);
}
