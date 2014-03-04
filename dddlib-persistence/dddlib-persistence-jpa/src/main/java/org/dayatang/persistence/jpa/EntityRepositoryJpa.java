package org.dayatang.persistence.jpa;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.IocInstanceNotFoundException;

/**
 * 通用仓储接口的JPA实现。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
@Named("dddlib_entity_repository_jpa")
public class EntityRepositoryJpa extends AbstractEntityRepositoryJpa {

    @Inject
    @PersistenceContext
    private EntityManager entityManager;

    private EntityManagerFactory entityManagerFactory;

    public EntityRepositoryJpa() {
    }

    public EntityRepositoryJpa(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    protected EntityManager getEntityManager() {
        if (entityManager != null) {
            return entityManager;
        }
        try {
            return InstanceFactory.getInstance(EntityManager.class);
        } catch (IocInstanceNotFoundException e) {
            if (entityManagerFactory == null) {
                entityManagerFactory = InstanceFactory
                        .getInstance(EntityManagerFactory.class);
            }
            return entityManagerFactory.createEntityManager();
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
