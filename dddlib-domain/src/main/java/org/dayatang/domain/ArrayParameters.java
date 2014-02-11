/*
 * Copyright 2014 Dayatang Open Source..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dayatang.domain;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 数组形式的查询参数集，用来表示查询语言或命名查询的定位参数。JPA、Hibernate和SQL等都支持定位
 * 参数(如"... where e.name = ?")和命名参数(如"... where name = :name")两种形式。<br>
 * 尽可能采用命名参数的形式，定位参数是落后的形式。
 * @author yyang
 */
public class ArrayParameters implements QueryParameters {
    
    private Object[] params;
    
    /**
     * 创建一个空查询参数集
     * @return 一个基于数组的查询参数集
     */
    public static ArrayParameters create() {
        return new ArrayParameters(new Object[]{});
    }
    
    /**
     * 创建一个查询参数集，用数组填充参数值
     * @param params 参数值数组
     * @return 一个基于数组的参数集
     */
    public static ArrayParameters create(Object... params) {
        return new ArrayParameters(params);
    }
    
    /**
     * 创建一个查询参数集，用列表填充参数值
     * @param params 参数值列表
     * @return 一个基于数组的参数集
     */
    public static ArrayParameters create(List<Object> params) {
        return new ArrayParameters(params.toArray());
    }

    private ArrayParameters(Object[] params) {
        if (params == null) {
            this.params = new Object[]{};
        } else {
            this.params = Arrays.copyOf(params, params.length);
        }
    }

    /**
     * 获得参数值数组
     * @return 参数数组
     */
    public Object[] getParams() {
        return Arrays.copyOf(params, params.length);
    }

    /**
     * 获得对象的哈希值
     * @return 对象的哈希值
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 43).append(params).toHashCode();
    }

    /**
     * 判断参数集对象的等价性。当且仅当两个ArrayParameters包含的参数数组相同时，两个对象才是等价的。
     * @param other 另一个对象
     * @return 如果当前对象等价于other则返回true，否则返回false。
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ArrayParameters)) {
            return false;
        }
        ArrayParameters that = (ArrayParameters) other;
        return new EqualsBuilder().append(this.getParams(), that.getParams()).isEquals();
    }

    /**
     * 获得参数集的字符串表示形式
     * @return 当前对象的字符串表示形式
     */
    @Override
    public String toString() {
        return Arrays.toString(params);
    }
}    