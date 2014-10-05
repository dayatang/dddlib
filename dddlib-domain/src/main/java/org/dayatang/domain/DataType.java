package org.dayatang.domain;

import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据类型枚举。
 * @author yyang
 */
public enum DataType {

    STRING {
                @Override
                public String getValue(String value) {
                    return StringUtils.isBlank(value) ? "" : value;
                }
            },
    INT {
                @Override
                public Integer getValue(String value) {
                    return StringUtils.isBlank(value) ? 0 : Integer.valueOf(value);
                }
            },
    LONG {
                @Override
                public Long getValue(String value) {
                    return StringUtils.isBlank(value) ? 0 : Long.valueOf(value);
                }
            },
    DOUBLE {
                @Override
                public Double getValue(String value) {
                    return StringUtils.isBlank(value) ? 0 : Double.valueOf(value);
                }
            },
    BIG_DECIMAL {
                @Override
                public BigDecimal getValue(String value) {
                    return StringUtils.isBlank(value) ? BigDecimal.ZERO : new BigDecimal(value);
                }
            },
    BOOLEAN {
                @Override
                public Boolean getValue(String value) {
                    return StringUtils.isBlank(value) ? false : Boolean.valueOf(value);
                }
            },
    DATE {
                @Override
                public Date getValue(String value) {
                    try {
                        return StringUtils.isBlank(value) ? null : DateUtils.parseDate(value, DATE_FORMAT);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("'" + value + "' cannot be converted to Date" + e);
                    }
                }
            },
    TIME {
                @Override
                public Date getValue(String value) {
                    try {
                        return StringUtils.isBlank(value) ? null : DateUtils.parseDate(value, TIME_FORMAT);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("'" + value + "' cannot be converted to Time" + e);
                    }
                }
            },
    DATE_TIME {
                @Override
                public Date getValue(String value) {
                    try {
                        return StringUtils.isBlank(value) ? null : DateUtils.parseDate(value, DATE_TIME_FORMAT);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("'" + value + "' cannot be converted to DateTime" + e);
                    }
                }
            };

    /**
     * 转换字符串值为相应数据类型形式的值。
     * @param value 字符串格式的值
     * @return 对象值
     */
    public abstract Object getValue(String value);

    /**
     * 日期格式
     */
    protected static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 时间格式
     */
    protected static final String TIME_FORMAT = "hh:mm:ss";

    /**
     * 时间戳格式
     */
    protected static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
}
