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

import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.IocInstanceNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * JPA 实体管理器提供者。如果当前线程中尚未存在entityManager线程变量，则从IoC容器中获取一个并存入当前线程，
 * 如果当前线程已经存在entityManager线程变量，直接返回。
 * <p>
 * 本类的存在，主要是为了在当前线程中，每次请求都返回相同的entityManager对象。避免事务和“会话已关闭”问题。
 *
 * @author yyang
 */
public class EntityManagerProvider {

    private EntityManagerFactory entityManagerFactory;

    private final ThreadLocal<EntityManager> entityManagerHolder = new ThreadLocal<EntityManager>();

    public EntityManagerProvider() {
        entityManagerFactory = InstanceFactory.getInstance(EntityManagerFactory.class);
    }

    public EntityManagerProvider(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManagerProvider(EntityManager entityManager) {
        entityManagerHolder.set(entityManager);
    }

    public EntityManager getEntityManager() {
        EntityManager result = entityManagerHolder.get();
        if (result != null && result.isOpen()) {
            return result;
        }
        result = getEntityManagerFromIoC();
        entityManagerHolder.set(result);
        return result;
    }

    public EntityManager getEntityManagerFromIoC() {
        try {
            return InstanceFactory.getInstance(EntityManager.class);
        } catch (IocInstanceNotFoundException e) {
            if (entityManagerFactory == null) {
                entityManagerFactory = InstanceFactory.getInstance(EntityManagerFactory.class);
            }
            return entityManagerFactory.createEntityManager();
        }
    }
}
