package org.dayatang.utils;

import java.util.Objects;

/**
 * Bean的属性信息，包括属性名、属性类型和属性值。
 * Created by yyang on 15/7/26.
 */
public class PropertyInfo {
    private String propName;
    private Class<?> propType;
    private Object propValue;

    public PropertyInfo(String propName, Class<?> propType, Object propValue) {
        this.propName = propName;
        this.propType = propType;
        this.propValue = propValue;
    }

    public String getPropName() {
        return propName;
    }

    public Class<?> getPropType() {
        return propType;
    }

    public Object getPropValue() {
        return propValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyInfo)) {
            return false;
        }
        PropertyInfo that = (PropertyInfo) o;
        return Objects.equals(getPropName(), that.getPropName()) &&
                Objects.equals(getPropType(), that.getPropType()) &&
                Objects.equals(getPropValue(), that.getPropValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPropName(), getPropType(), getPropValue());
    }

    @Override
    public String toString() {
        return "PropertyInfo{" +
                "propName='" + propName + '\'' +
                ", propType=" + propType +
                ", propValue=" + propValue +
                '}';
    }
}
