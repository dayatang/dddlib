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

import org.dayatang.utils.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Map形式的查询参数集，用来表示查询语言或命名查询的命名参数。JPA、Hibernate和SQL等都支持定位
 * 参数(如"... where e.name = ?")和命名参数(如"... where name = :name")两种形式。<br>
 * 尽可能采用命名参数的形式，定位参数是落后的形式。
 * @author yyang
 */
public class MapParameters implements QueryParameters {
    private Map<String, Object> params;
    
    /**
     * 创建一个空查询参数集
     * @return 一个基于Map的查询参数集
     */
    public static MapParameters create() {
        return new MapParameters(new HashMap<String, Object>());
    }
    
    /**
     * 创建一个查询参数集
     * @param params 要设置的查询参数的map，Key为参数名，Value为参数值
     * @return 一个基于Map的查询参数集
     */
    public static MapParameters create(Map<String, Object> params) {
        return new MapParameters(params);
    }

    private MapParameters(Map<String, Object> params) {
        if (params == null) {
            this.params = new HashMap<String, Object>();
        } else {
            this.params = new HashMap<String, Object>(params);
        }
    }
    
    /**
     * 添加一个命名参数
     * @param key 参数名称
     * @param value 参数值
     * @return 当前对象本身
     */
    public MapParameters add(String key, Object value) {
        Assert.notBlank(key);
        Assert.notNull(value);
        params.put(key, value);
        return this;
    }

    /**
     * 获得参数Map
     * @return 参数Map
     */
    public Map<String, Object> getParams() {
        return Collections.unmodifiableMap(params);
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
     * 判断参数集对象的等价性。当且仅当两个MapParameters包含的参数Map相同时，两个对象才是等价的。
     * @param other 另一个对象
     * @return 如果当前对象等价于other则返回true，否则返回false。
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MapParameters)) {
            return false;
        }
        MapParameters that = (MapParameters) other;
        return new EqualsBuilder().append(this.getParams(), that.getParams()).isEquals();
    }

    /**
     * 获得参数集的字符串表示形式
     * @return 当前对象的字符串表示形式
     */
    @Override
    public String toString() {
        return params.toString();
    }
    
}
