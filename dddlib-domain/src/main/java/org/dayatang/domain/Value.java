package org.dayatang.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * 值。这个类主要用于实体类的自定义属性，例如动态地给Employee类添加一批属性， 每个属性都记录数据类型和字符串形式的值
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
        "yyyy-MM-dd hh:mm:ss",};

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private DataType dataType;

    @Column(name = "obj_value")
    private String value = "";

    /**
     * 创建字符串值
     *
     * @param value
     * @return
     */
    public static Value stringValue(String value) {
        return new Value(DataType.STRING, value);
    }

    /**
     * 创建整数值
     *
     * @param value
     * @return
     */
    public static Value intValue(String value) {
        return new Value(DataType.INT, value);
    }

    /**
     * 创建长整型值
     *
     * @param value
     * @return
     */
    public static Value longValue(String value) {
        return new Value(DataType.LONG, value);
    }

    /**
     * 创建浮点值
     *
     * @param value
     * @return
     */
    public static Value doubleValue(String value) {
        return new Value(DataType.DOUBLE, value);
    }

    /**
     * 创建BigDecimal值
     *
     * @param value
     * @return
     */
    public static Value bigDecimalValue(String value) {
        return new Value(DataType.BIG_DECIMAL, value);
    }

    /**
     * 创建布尔型值
     *
     * @param value
     * @return
     */
    public static Value booleanValue(String value) {
        return new Value(DataType.BOOLEAN, value);
    }

    /**
     * 创建日期型值（无时间部分）
     *
     * @param value
     * @return
     */
    public static Value dateValue(String value) {
        return new Value(DataType.DATE, value);
    }

    /**
     * 创建时间型值（无日期部分）
     *
     * @param value
     * @return
     */
    public static Value timeValue(String value) {
        return new Value(DataType.TIME, value);
    }

    /**
     * 创建时间戳型值（包含日期和时间部分）
     *
     * @param value
     * @return
     */
    public static Value dateTimeValue(String value) {
        return new Value(DataType.DATE_TIME, value);
    }

    protected Value() {
    }

    private Value(DataType dataType, String value) {
        this.dataType = dataType;
        if (value != null) {
            this.value = value;
        }

    }

    public DataType getDataType() {
        return dataType;
    }

    public String getStringValue() {
        return value;
    }

    public String getString() {
        return (String) DataType.STRING.getValue(value);
    }

    public int getInt() {
        return (Integer) DataType.INT.getValue(value);
    }

    public long getLong() {
        return (Long) DataType.LONG.getValue(value);
    }

    public double getDouble() {
        return (Double) DataType.DOUBLE.getValue(value);
    }

    public BigDecimal getBigDecimal() {
        return (BigDecimal) DataType.BIG_DECIMAL.getValue(value);
    }

    public boolean getBoolean() {
        return (Boolean) DataType.BOOLEAN.getValue(value);
    }

    public Date getDate() {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return DateUtils.parseDateStrictly(value, DATE_TIME_FORMAT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getValue() {
        return dataType.getValue(value);
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
                .append(this.getStringValue(), that.getStringValue())
                .isEquals();
    }

    @Override
    public String toString() {
        return value;
    }

}
