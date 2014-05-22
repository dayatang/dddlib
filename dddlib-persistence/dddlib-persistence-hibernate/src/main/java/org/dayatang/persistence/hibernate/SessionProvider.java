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
package org.dayatang.persistence.hibernate;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.IocInstanceNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Hibernate会话提供者。如果当前线程中尚未存在session线程变量，则从IoC容器中获取一个并存入当前线程，
 * 如果当前线程已经存在session线程变量，直接返回。
 * <p>
 * 本类的存在，主要是为了在当前线程中，每次请求都返回相同的session对象。避免事务和“会话已关闭”问题。
 *
 * @author yyang
 */
public class SessionProvider {

    private final ThreadLocal<Session> sessionHolder = new ThreadLocal<Session>();

    private SessionFactory sessionFactory;

    public SessionProvider() {
        sessionFactory = InstanceFactory.getInstance(SessionFactory.class);
    }

    public SessionProvider(Session session) {
        sessionHolder.set(session);
    }

    public SessionProvider(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        Session result = sessionHolder.get();
        if (result != null && result.isOpen()) {
            return result;
        }
        result = getSessionFromIoC();
        sessionHolder.set(result);
        return result;
    }
      
    private Session getSessionFromIoC() {
        try {
            return InstanceFactory.getInstance(Session.class);
        } catch (IocInstanceNotFoundException e) {
            if (sessionFactory == null) {
                sessionFactory = InstanceFactory.getInstance(SessionFactory.class);
            }
            return sessionFactory.getCurrentSession();
        }
    }
}
