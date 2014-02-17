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

package org.dayatang.persistence.jpa;

/**
 * 用于获取命名查询的查询字符串的接口。由于JPA规范不直接支持这一功能，所以要由使用JPA实现的本地API
 * 实现它。
 * @author yyang
 */
public interface NamedQueryParser {
    
    /**
     * 获取命名查询的查询字符串
     * @param queryName 命名查询的名字
     * @return 命名查询的查询字符串
     */
    String getQueryStringOfNamedQuery(String queryName);
    
}
