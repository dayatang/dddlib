package com.dayatang.utils;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 整数范围类。包括左右边界值。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class IntRange implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8864945948923971061L;

	private int min;
	
	private int max;

	/**
	 * @param min
	 * @param max
	 */
	public IntRange(int min, int max) {
		super();
		this.min = min;
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
	
	public boolean contains(int number) {
		return number >= min && number <= max;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(min).append(max).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof IntRange)) {
			return false;
		}
		IntRange that = (IntRange) other; 
		return new EqualsBuilder().append(this.getMin(), that.getMin())
			.append(this.getMax(), that.getMax())
			.isEquals();
	}

	@Override
	public String toString() {
		return "[" + min + " - " + max + "]";
	}
	
	
}
