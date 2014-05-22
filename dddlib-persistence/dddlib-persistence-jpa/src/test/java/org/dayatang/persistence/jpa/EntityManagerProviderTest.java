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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.dayatang.domain.InstanceFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author yyang
 */
public class EntityManagerProviderTest {

    private EntityManagerFactory entityManagerFactory;
    
    private EntityManager entityManager;
    
    @Before
    public void setUp() {
        entityManagerFactory = mock(EntityManagerFactory.class);
        entityManager = mock(EntityManager.class);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    }

    @Test
    public void testDefaultConstructor() {
        InstanceFactory.bind(EntityManagerFactory.class, entityManagerFactory);
        EntityManagerProvider instance = new EntityManagerProvider();
        EntityManager entityManager2 = instance.getEntityManager();
        assertSame(entityManager2, entityManager);
        EntityManager entityManager3 = instance.getEntityManager();
        assertSame(entityManager3, entityManager);
    }

    @Test
    public void testConstructorEntityManagerFactory() {
        EntityManagerProvider instance = new EntityManagerProvider(entityManagerFactory);
        EntityManager entityManager2 = instance.getEntityManager();
        assertSame(entityManager2, entityManager);
        EntityManager entityManager3 = instance.getEntityManager();
        assertSame(entityManager3, entityManager);
    }
}
