package com.dayatang.domain;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.time.DateUtils;

/** 
 * 值
 * 
 * @author chencao
 *
 */
@Embeddable
public class Value implements ValueObject {

	private static final long serialVersionUID = 4254026874177282302L;
	
	private static final String[] DATE_TIME_FORMAT = {
		"yyyy-MM-dd",
		"hh:mm",
		"hh:mm:ss",
		"yyyy-MM-dd hh:mm",
		"yyyy-MM-dd hh:mm:ss",
	};

	@Enumerated(EnumType.STRING)
	@Column(name = "data_type")
	private DataType dataType;

	@Column(name = "obj_value")
	private String value;

	public Value() {
		super();
	}

	public Value(DataType dataType, String value) {
		super();
		this.dataType = dataType;
		this.value = value;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getString() {
		return value;
	}
	
	public int getInt() {
		return value == null ? 0 : Integer.parseInt(value);
	}
	
	public double getDouble() {
		return value == null ? 0.0 : Double.parseDouble(value);
	}
	
	public BigDecimal getBigDecimal() {
		return value == null ? null : new BigDecimal(value);
	}
	
	public boolean getBoolean() {
		return Boolean.parseBoolean(value);
	}
	
	public Date getDate() {
		if (value == null) {
			return null;
		}
		try {
			return DateUtils.parseDateStrictly(value, DATE_TIME_FORMAT);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Object getDefaultValue() {
		return dataType.getDefaultValue();
	}
	
	public Object getRealValue() {
		return dataType.getRealValue(value);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(dataType).append(value).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Value)) {
			return false;
		}
		Value that = (Value) other;
		return new EqualsBuilder()
			.append(this.getDataType(), that.getDataType())
			.append(this.getValue(), that.getValue())
			.isEquals();
	}

	@Override
	public String toString() {
		return value;
	}

}
