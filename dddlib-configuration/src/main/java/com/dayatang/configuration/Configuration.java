package com.dayatang.configuration;

import java.util.Date;
import java.util.Properties;

/**
 * 用于读取全局性配置信息的接口。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface Configuration {

	/**
	 * 获取指定的键对应的字符串型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	String getString(String key, String defaultValue);

	/**
	 * 获取指定的键对应的字符串型键值。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。如果键值不存在则返回空字符串。
	 */
	String getString(String key);

	/**
	 * 获取指定的键对应的整数型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	int getInt(String key, int defaultValue);

	/**
	 * 获取指定的键对应的整数型键值。如果键值不存在则返回0。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。
	 */
	int getInt(String key);

	/**
	 * 获取指定的键对应的长整型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	long getLong(String key, long defaultValue);

	/**
	 * 获取指定的键对应的长整型键值。如果键值不存在则返回0。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。
	 */
	long getLong(String key);

	/**
	 * 获取指定的键对应的双精度型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	double getDouble(String key, double defaultValue);

	/**
	 * 获取指定的键对应的双精度型键值。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。如果键值不存在则返回0。
	 */
	double getDouble(String key);

	/**
	 * 获取指定的键对应的布尔型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	boolean getBoolean(String key, boolean defaultValue);

	/**
	 * 获取指定的键对应的布尔型键值。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。如果键值不存在则返回false。
	 */
	boolean getBoolean(String key);

	/**
	 * 获取指定的键对应的日期型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	Date getDate(String key, Date defaultValue);

	/**
	 * 获取指定的键对应的日期型键值。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。如果键值不存在则返回null。
	 */
	Date getDate(String key);

	/**
	 * 设置指定配置项的字符串型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setString(String key, String value);

	/**
	 * 设置指定配置项的整数型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setInt(String key, int value);

	/**
	 * 设置指定配置项的长整型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setLong(String key, long value);

	/**
	 * 设置指定配置项的双精度型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setDouble(String key, double value);

	/**
	 * 设置指定配置项的布尔型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setBoolean(String key, boolean value);

	/**
	 * 设置指定配置项的日期型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setDate(String key, Date value);
	
	/**
	 * 获得所有属性
	 * @return
	 */
	Properties getProperties();
	
	/**
	 * 从持久化源中获取最新配置，更新当前设置
	 */
	void load();
}