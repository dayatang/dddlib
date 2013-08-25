package com.dayatang.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 日期范围类。包括左右边界值。忽略时间部分。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class DateRange implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2800351591055572549L;

	private Date from;
	
	private Date to;

	/**
	 * @param from
	 * @param to
	 */
	public DateRange(Date from, Date to) {
		super();
		this.from = from;
		this.to = to;
	}

	public Date getFrom() {
		return from;
	}

	public Date getTo() {
		return to;
	}
	
	public boolean contains(Date date) {
		return (date.after(from) || DateUtils.isSameDay(date, from))
				&& (date.before(to) || DateUtils.isSameDay(date, to));
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DateRange)) {
			return false;
		}
		DateRange castOther = (DateRange) other;
		return DateUtils.isSameDay(this.from, castOther.from) && DateUtils.isSameDay(this.to, castOther.to); 
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(from).append(to).toHashCode();
	}

	@Override
	public String toString() {
		return "[" + DateFormat.getDateInstance().format(from) 
			+ " - " 
			+ DateFormat.getDateInstance().format(to) + "]";
	}
}
