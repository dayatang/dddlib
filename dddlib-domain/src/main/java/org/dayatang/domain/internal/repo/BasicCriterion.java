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
package org.dayatang.domain.internal.repo;

import org.dayatang.utils.Assert;

/**
 * 基本查询条件，指除AND/OR/NOT查询条件以外的大多数查询条件，基本上都是判断某个属性值是否符合 某种条件
 *
 * @author yyang
 */
public abstract class BasicCriterion extends AbstractCriterion {

    private final String propName;

    public BasicCriterion(String propName) {
        Assert.notBlank(propName, "Property name is null or blank!");
        this.propName = propName;
    }

    /**
     * 获取属性名
     *
     * @return 属性名
     */
    public String getPropName() {
        return propName;
    }

    /**
     * 获取带别名前缀的属性名
     *
     * @return 带别名前缀的属性名
     */
    protected String getPropNameWithAlias() {
        return ROOT_ALIAS + "." + propName;
    }

    /**
     * 获得参数名
     *
     * @return 参数名
     */
    protected String getParamName() {
        String result = ROOT_ALIAS + "_" + propName + hashCode();
        result = result.replace(".", "_");
        result = result.replace("-", "_");
        return result;
    }

    /**
     * 获得带冒号前缀的参数名
     *
     * @return 带冒号前缀的参数名
     */
    protected String getParamNameWithColon() {
        return ":" + getParamName();
    }
}
