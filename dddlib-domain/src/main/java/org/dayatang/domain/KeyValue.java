package org.dayatang.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.utils.Assert;

/**
 * 键值对数据类型
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public class KeyValue<K, V> {
	
	private K key;
	
	private V value;

	/**
	 * 生成键值对
	 * @param key 键
	 * @param value 值
	 */
	public KeyValue(K key, V value) {
		Assert.notNull(key, "Key must not be null!");
		this.key = key;
		this.value = value;
	}

	/**
	 * 获得键
	 * @return 键
	 */
	public K getKey() {
		return key;
	}

	/**
	 * 获得值
	 * @return 值
	 */
	public V getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(key).append(value).toHashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KeyValue)) {
			return false;
		}
		KeyValue<K, V> that = (KeyValue<K, V>) other;
		return new EqualsBuilder().append(this.key, that.key)
				.append(this.value, that.value).isEquals();
	}

	@Override
	public String toString() {
		return "KeyValue [key=" + key + ", value=" + value + "]";
	}

}
