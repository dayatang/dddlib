package org.dayatang.persistence.jpa;

import org.dayatang.domain.IocInstanceNotFoundException;
import org.dayatang.domain.*;
import org.dayatang.persistence.jpa.internal.JpaCriteriaQueryBuilder;
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
public class EntityRepositoryJpa implements EntityRepository {

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

    private EntityManager getEntityManager() {
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


    @Override
    public <T extends Entity> T save(T entity) {
        if (entity.isNew()) {
            getEntityManager().persist(entity);
            LOGGER.info("create a entity: " + entity.getClass() + "/"
                    + entity.getId() + ".");
            return entity;
        }
        T result = getEntityManager().merge(entity);
        LOGGER.info("update a entity: " + entity.getClass() + "/"
                + entity.getId() + ".");
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dayatang.domain.EntityRepository#remove(org.dayatang.domain.Entity)
     */
    @Override
    public void remove(Entity entity) {
        getEntityManager().remove(get(entity.getClass(), entity.getId()));
        LOGGER.info("remove a entity: " + entity.getClass() + "/"
                + entity.getId() + ".");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dayatang.domain.EntityRepository#exists(java.io.Serializable)
     */
    @Override
    public <T extends Entity> boolean exists(final Class<T> clazz,
                                             final Serializable id) {
        T entity = getEntityManager().find(clazz, id);
        return entity != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dayatang.domain.EntityRepository#get(java.io.Serializable)
     */
    @Override
    public <T extends Entity> T get(final Class<T> clazz, final Serializable id) {
        return getEntityManager().find(clazz, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dayatang.domain.EntityRepository#load(java.io.Serializable)
     */
    @Override
    public <T extends Entity> T load(final Class<T> clazz, final Serializable id) {
        return getEntityManager().getReference(clazz, id);
    }

    @Override
    public <T extends Entity> T getUnmodified(final Class<T> clazz,
                                              final T entity) {
        getEntityManager().detach(entity);
        return get(clazz, entity.getId());
    }

    @Override
    public <T extends Entity> List<T> findAll(final Class<T> clazz) {
        String queryString = "select o from " + clazz.getName() + " as o";
        return getEntityManager().createQuery(queryString).getResultList();
    }

    @Override
    public <T extends Entity> CriteriaQuery createCriteriaQuery(Class<T> entityClass) {
        return new CriteriaQuery(this, entityClass);
    }

    @Override
    public <T> List<T> find(CriteriaQuery dddQuery) {
        javax.persistence.criteria.CriteriaQuery criteriaQuery = JpaCriteriaQueryBuilder.getInstance()
                .createCriteriaQuery(dddQuery, getEntityManager());
        Query query = getEntityManager().createQuery(criteriaQuery);
        query.setFirstResult(dddQuery.getFirstResult());
        if (dddQuery.getMaxResults() > 0) {
            query.setMaxResults(dddQuery.getMaxResults());
        }
        return query.getResultList();
    }

    @Override
    public <T> T getSingleResult(org.dayatang.domain.CriteriaQuery dddQuery) {
        List<T> results = find(dddQuery);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public JpqlQuery createJpqlQuery(String jpql) {
        return new JpqlQuery(this, jpql);
    }

    @Override
    public <T> List<T> find(JpqlQuery jpqlQuery) {
        Query query = getEntityManager().createQuery(jpqlQuery.getJpql());
        fillParameters(query, jpqlQuery.getParameters());
        query.setFirstResult(jpqlQuery.getFirstResult());
        if (jpqlQuery.getMaxResults() > 0) {
            query.setMaxResults(jpqlQuery.getMaxResults());
        }
        return query.getResultList();
    }

    @Override
    public <T> T getSingleResult(JpqlQuery jpqlQuery) {
        List<T> results = find(jpqlQuery);
        return results == null || results.isEmpty() ? null : results.get(0);
    }

    @Override
    public int executeUpdate(JpqlQuery jpqlQuery) {
        Query query = getEntityManager().createQuery(jpqlQuery.getJpql());
        fillParameters(query, jpqlQuery.getParameters());
        return query.executeUpdate();

    }

    @Override
    public NamedQuery createNamedQuery(String queryName) {
        return new NamedQuery(this, queryName);
    }

    @Override
    public <T> List<T> find(NamedQuery namedQuery) {
        Query query = getEntityManager().createNamedQuery(namedQuery.getQueryName());
        fillParameters(query, namedQuery.getParameters());
        query.setFirstResult(namedQuery.getFirstResult());
        if (namedQuery.getMaxResults() > 0) {
            query.setMaxResults(namedQuery.getMaxResults());
        }
        return query.getResultList();
    }

    @Override
    public <T> T getSingleResult(NamedQuery namedQuery) {
        List<T> results = find(namedQuery);
        return results == null || results.isEmpty() ? null : results.get(0);
    }

    @Override
    public int executeUpdate(NamedQuery namedQuery) {
        Query query = getEntityManager().createNamedQuery(namedQuery.getQueryName());
        fillParameters(query, namedQuery.getParameters());
        return query.executeUpdate();
    }

    @Override
    public <T extends Entity, E extends T> List<T> findByExample(
            final E example, final ExampleSettings<T> settings) {
        throw new RuntimeException("not implemented yet!");
    }

    @Override
    public <T extends Entity> List<T> findByProperty(Class<T> clazz, String propertyName, Object propertyValue) {
        return find(new CriteriaQuery(this, clazz).eq(propertyName, propertyValue));
    }

    @Override
    public <T extends Entity> List<T> findByProperties(Class<T> clazz, Map<String, Object> properties) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(this, clazz);
        for (Map.Entry<String, Object> each : properties.entrySet()) {
            criteriaQuery = criteriaQuery.eq(each.getKey(), each.getValue());
        }
        return find(criteriaQuery);
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }

    @Override
    public void refresh(Entity entity) {
        getEntityManager().refresh(entity);
    }

    @Override
    public void clear() {
        getEntityManager().clear();
    }

    private void fillParameters(Query query, QueryParameters params) {
        if (params instanceof ArrayParameters) {
            Object[] paramArray = ((ArrayParameters) params).getParams();
            for (int i = 0; i < paramArray.length; i++) {
                query = query.setParameter(i + 1, paramArray[i]);
            }
        } else if (params instanceof MapParameters) {
            Map<String, Object> paramMap = ((MapParameters) params).getParams();
            for (Map.Entry<String, Object> each : paramMap.entrySet()) {
                query = query.setParameter(each.getKey(), each.getValue());
            }
        } else {
            throw new UnsupportedOperationException("不支持的参数形式");

        }
    }

}
