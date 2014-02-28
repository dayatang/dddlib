package org.dayatang.persistence.jpa;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 通用仓储接口的JPA实现。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
@SuppressWarnings({"unchecked", "deprecation"})
@Named("dddlib_entity_repository_jpa")
public class EntityRepositoryJpa extends AbstractEntityRepositoryJpa {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRepositoryJpa.class);

    @Inject
    @PersistenceContext
    private EntityManager entityManager;

    private EntityManagerFactory entityManagerFactory;

    public EntityRepositoryJpa() {
    }

    public EntityRepositoryJpa(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

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
