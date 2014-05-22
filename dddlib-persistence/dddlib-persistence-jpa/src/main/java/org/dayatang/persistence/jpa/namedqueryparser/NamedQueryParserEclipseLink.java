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

package org.dayatang.persistence.jpa.namedqueryparser;

import org.dayatang.persistence.jpa.EntityManagerProvider;
import org.dayatang.persistence.jpa.NamedQueryParser;
import org.eclipse.persistence.jpa.JpaEntityManager;


/**
 * NamedQueryParser接口的EclipseLink实现
 * @author yyang
 */
public class NamedQueryParserEclipseLink extends NamedQueryParser {

    public NamedQueryParserEclipseLink() {
    }

    public NamedQueryParserEclipseLink(EntityManagerProvider entityManagerProvider) {
        super(entityManagerProvider);
    }
    
    @Override
    public String getQueryStringOfNamedQuery(String queryName) {
        JpaEntityManager jpaEntityManager = (JpaEntityManager) getEntityManager().getDelegate();
        return jpaEntityManager.getActiveSession().getQuery(queryName).getJPQLString();
    }
    
}
