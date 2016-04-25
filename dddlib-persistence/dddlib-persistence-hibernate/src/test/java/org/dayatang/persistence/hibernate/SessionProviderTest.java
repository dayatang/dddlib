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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author yyang
 */
public class SessionProviderTest {

    private SessionFactory sessionFactory;
    private Session session;
    
    @Before
    public void setUp() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testDefaultConstructor() {
        InstanceFactory.bind(SessionFactory.class, sessionFactory);
        SessionProvider instance = new SessionProvider();
        Session session2 = instance.getSession();
        assertSame(session2, session);
        Session session3 = instance.getSession();
        assertSame(session3, session);
    }

    @Test
    public void testConstructorSessionFactory() {
        SessionProvider instance = new SessionProvider(sessionFactory);
        Session session2 = instance.getSession();
        assertSame(session2, session);
        Session session3 = instance.getSession();
        assertSame(session3, session);
    }
    
}
