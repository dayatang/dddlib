package org.dayatang.domain.internal;

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.dayatang.domain.Entity;
import org.dayatang.domain.NamedParameters;

/**
 * 代表某个属性的值不包含在指定集合或数组中的查询条件
 *
 * @author yyang
 */
public class NotInCriterion extends BasicCriterion {

    private Collection<? extends Object> value = new ArrayList<Object>();

    /**
     * 创建查询条件
     * @param propName 属性名
     * @param value 集合值
     */
    public NotInCriterion(String propName, Collection<? extends Object> value) {
        super(propName);
        if (value != null) {
            this.value = value;
        }
    }

    /**
     * 创建查询条件
     * @param propName 属性名
     * @param value 数组值
     */
    public NotInCriterion(String propName, Object[] value) {
        super(propName);
        if (value != null && value.length > 0) {
            this.value = Arrays.asList(value);
        }
    }

    /**
     * 获得集合值
     * @return 集合值
     */
    public Collection<? extends Object> getValue() {
        return value;
    }

    @Override
    public String toQueryString() {
        if (value == null || value.isEmpty()) {
            return "";
        }return getPropNameWithAlias() + " not in " + getParamNameWithColon();
            //return getPropNameWithAlias() + " in (" + createInString(value) + ")";
    }

    @Override
    public NamedParameters getParameters() {
        NamedParameters result = NamedParameters.create();
        if (!value.isEmpty()) {
            result = result.add(getParamName(), value);
        }
        return result;
    }

    private String createInString(Collection<? extends Object> value) {
        Set<Object> elements = new HashSet<Object>();
        for (Object item : value) {
            Object element;
            if (item instanceof Entity) {
                element = ((Entity) item).getId();
            } else {
                element = item;
            }
            if (element instanceof String || element instanceof Date) {
                element = "'" + element + "'";
            }
            elements.add(element);
        }
        return StringUtils.join(elements, ", ");
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NotInCriterion)) {
            return false;
        }
        NotInCriterion that = (NotInCriterion) other;
        return new EqualsBuilder()
                .append(this.getPropName(), that.getPropName())
                .append(value, that.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getPropName()).append(value).toHashCode();
    }

    @Override
    public String toString() {
        return getPropName() + " not in collection [" + collectionToString(value) + "]";
    }

    private String collectionToString(Collection<? extends Object> value) {
        return StringUtils.join(value, ", ");
    }

}
