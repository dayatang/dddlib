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
     * @param value 字符串形式的值
     * @return 一个字符串类型的值
     */
    public static Value stringValue(String value) {
        return new Value(DataType.STRING, value);
    }

    /**
     * 创建整数值
     *
     * @param value 字符串形式的值
     * @return 一个整数类型的值
     */
    public static Value intValue(String value) {
        return new Value(DataType.INT, value);
    }

    /**
     * 创建长整型值
     *
     * @param value 字符串形式的值
     * @return 一个长整数类型的值
     */
    public static Value longValue(String value) {
        return new Value(DataType.LONG, value);
    }

    /**
     * 创建浮点值
     *
     * @param value 字符串形式的值
     * @return 一个小数类型的值
     */
    public static Value doubleValue(String value) {
        return new Value(DataType.DOUBLE, value);
    }

    /**
     * 创建BigDecimal值
     *
     * @param value 字符串形式的值
     * @return 一个BigDecimal类型的值
     */
    public static Value bigDecimalValue(String value) {
        return new Value(DataType.BIG_DECIMAL, value);
    }

    /**
     * 创建布尔型值
     *
     * @param value 字符串形式的值
     * @return 一个布尔类型的值
     */
    public static Value booleanValue(String value) {
        return new Value(DataType.BOOLEAN, value);
    }

    /**
     * 创建日期型值（无时间部分）
     *
     * @param value 字符串形式的值
     * @return 一个日期类型的值
     */
    public static Value dateValue(String value) {
        return new Value(DataType.DATE, value);
    }

    /**
     * 创建时间型值（无日期部分）
     *
     * @param value 字符串形式的值
     * @return 一个时间类型的值
     */
    public static Value timeValue(String value) {
        return new Value(DataType.TIME, value);
    }

    /**
     * 创建时间戳型值（包含日期和时间部分）
     *
     * @param value 字符串形式的值
     * @return 一个时间戳类型的值
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

    /**
     * 获取数据类型
     * @return 值所属的数据类型
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * 获取原始字符串值
     * @return 原始字符串值
     */
    public String getStringValue() {
        return value;
    }

    /**
     * 获取字符串值
     * @return 字符串形式的值
     */
    public String getString() {
        return (String) DataType.STRING.getValue(value);
    }

    /**
     * 获取整数值
     * @return 整数形式的值 
     */
    public int getInt() {
        return (Integer) DataType.INT.getValue(value);
    }

    /**
     * 获取长整数值
     * @return 长整数形式的值
     */
    public long getLong() {
        return (Long) DataType.LONG.getValue(value);
    }
    
    /**
     * 获取浮点数值
     * @return 浮点数形式的值
     */
    public double getDouble() {
        return (Double) DataType.DOUBLE.getValue(value);
    }

    /**
     * 获取BigDecimal值
     * @return BigDecimal形式的值。
     */
    public BigDecimal getBigDecimal() {
        return (BigDecimal) DataType.BIG_DECIMAL.getValue(value);
    }

    /**
     * 获取布尔值
     * @return 布尔形式的值
     */
    public boolean getBoolean() {
        return (Boolean) DataType.BOOLEAN.getValue(value);
    }

    /**
     * 获取日期/时间值
     * @return 日期形式的值
     */
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

    /**
     * 获得转换为相应数据类型的值
     * @return 由原始字符串转换为相应数据类型的值。
     */
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
