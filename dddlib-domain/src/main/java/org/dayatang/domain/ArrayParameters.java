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
import org.apache.commons.lang3.ArrayUtils;
import org.dayatang.utils.Assert;

/**
 * 定位参数形式的查询参数集合
 * @author yyang
 */
public class ArrayParameters implements QueryParameters {
    
    private Object[] params = new Object[]{};
    
    public static ArrayParameters create() {
        return new ArrayParameters();
    }
    
    public static ArrayParameters create(Object... params) {
        return new ArrayParameters(params);
    }
    
    public static ArrayParameters create(List<Object> params) {
        return new ArrayParameters(params.toArray());
    }

    private ArrayParameters() {
    }

    private ArrayParameters(Object[] params) {
        Assert.notNull(params);
        this.params = Arrays.copyOf(params, params.length);
    }

    /**
     * 添加一或多个参数
     * @param params
     * @return 
     */
    public ArrayParameters add(Object... params) {
        this.params = ArrayUtils.addAll(this.params, params);
        return this;
    }

    /**
     * 获得参数数组
     * @return 参数数组
     */
    public Object[] getParams() {
        return Arrays.copyOf(params, params.length);
    }
    
}
