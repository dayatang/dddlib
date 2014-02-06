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

/**
 * 命名参数形式的查询参数集合
 * @author yyang
 */
public class MapParameters implements QueryParameters {
    private Map<String, Object> params = new HashMap<String, Object>();
    
    public static MapParameters create() {
        return new MapParameters();
    }
    
    public static MapParameters create(Map<String, Object> params) {
        return new MapParameters(params);
    }
    
    
    private MapParameters() {
    }

    private MapParameters(Map<String, Object> params) {
        Assert.notNull(params);
        this.params = new HashMap<String, Object>(params);
    }
    
    /**
     * 添加一个命名参数
     * @param key 参数名称
     * @param value 参数值
     * @return 
     */
    public MapParameters add(String key, Object value) {
        Assert.notBlank(key);
        Assert.notNull(value);
        params.put(key, value);
        return this;
    }

    /**
     * 获得参数Map
     * @return 
     */
    public Map<String, Object> getParams() {
        return Collections.unmodifiableMap(params);
    }
    
}
