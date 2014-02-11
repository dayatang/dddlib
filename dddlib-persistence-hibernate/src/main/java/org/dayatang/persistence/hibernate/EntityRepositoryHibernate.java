package org.dayatang.persistence.hibernate;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.IocException;
import org.dayatang.domain.*;
import org.dayatang.persistence.hibernate.internal.QueryTranslator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 通用仓储接口的Hibernate实现。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
@SuppressWarnings({"unchecked"})
public class EntityRepositoryHibernate implements EntityRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRepositoryHibernate.class);

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#save(org.dayatang.domain.Entity)
     */
    @Override
    public <T extends Entity> T save(T entity) {
        if (entity.notExisted()) {
            getSession().save(entity);
            LOGGER.info("create a entity: " + entity.getClass() + "/" + entity.getId() + ".");
            return entity;
        }
        getSession().update(entity);
        LOGGER.info("update a entity: " + entity.getClass() + "/" + entity.getId() + ".");
        return entity;
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#remove(org.dayatang.domain.Entity)
     */
    @Override
    public void remove(Entity entity) {
        getSession().delete(entity);
        LOGGER.info("remove a entity: " + entity.getClass() + "/" + entity.getId() + ".");
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#exists(java.lang.Class, java.io.Serializable)
     */
    @Override
    public <T extends Entity> boolean exists(final Class<T> clazz, final Serializable id) {
        return get(clazz, id) != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dayatang.domain.EntityRepository#get(java.io.Serializable)
     */
    @Override
    public <T extends Entity> T get(final Class<T> clazz, final Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#load(java.lang.Class, java.io.Serializable)
     */
    @Override
    public <T extends Entity> T load(final Class<T> clazz, final Serializable id) {
        return (T) getSession().load(clazz, id);
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#getUnmodified(java.lang.Class, org.dayatang.domain.Entity)
     */
    @Override
    public <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
        getSession().evict(entity);
        return get(clazz, entity.getId());
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#findAll(java.lang.Class)
     */
    @Override
    public <T extends Entity> List<T> findAll(Class<T> clazz) {
        return getSession().createCriteria(clazz).list();
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#createCriteriaQuery(java.lang.Class)
     */
    @Override
    public <T extends Entity> CriteriaQuery createCriteriaQuery(Class<T> entityClass) {
        return new CriteriaQuery(this, entityClass);
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#find(org.dayatang.domain.CriteriaQuery)
     */
    @Override
    public <T> List<T> find(CriteriaQuery dddQuery) {
        QueryTranslator translator = new QueryTranslator(dddQuery);
        String queryString = translator.getQueryString();
        LOGGER.info("QueryString: '" + queryString + "'");
        List<Object> params = translator.getParams();
        LOGGER.info("params: " + StringUtils.join(params, ", "));
        Query query = getSession().createQuery(queryString);
        for (int i = 0; i < params.size(); i++) {
            System.out.println("==================" + i + ": " + params.get(i));
            query.setParameter(i, params.get(i));
        }
        query.setFirstResult(dddQuery.getFirstResult());
        if (dddQuery.getMaxResults() > 0) {
            query.setMaxResults(dddQuery.getMaxResults());
        }
        return query.list();
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#getSingleResult(org.dayatang.domain.CriteriaQuery)
     */
    @Override
    public <T> T getSingleResult(CriteriaQuery dddQuery) {
        List<T> results = find(dddQuery);
        return results == null || results.isEmpty() ? null : results.get(0);
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#createJpqlQuery(java.lang.String)
     */
    @Override
    public JpqlQuery createJpqlQuery(String jpql) {
        return new JpqlQuery(this, jpql);
    }

    private Session getSession() {
        try {
            return InstanceFactory.getInstance(Session.class);
        } catch (IocException e) {
            SessionFactory sessionFactory = InstanceFactory.getInstance(SessionFactory.class);
            return sessionFactory.getCurrentSession();
        }
    }

    private void fillParameters(Query query, QueryParameters params) {
        if (params == null) {
            return;
        }
        if (params instanceof ArrayParameters) {
            Object[] paramArray = ((ArrayParameters) params).getParams();
            for (int i = 0; i < paramArray.length; i++) {
                query = query.setParameter(i, paramArray[i]);
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

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#find(org.dayatang.domain.JpqlQuery)
     */
    @Override
    public <T> List<T> find(JpqlQuery jpqlQuery) {
        Query query = getSession().createQuery(jpqlQuery.getJpql());
        fillParameters(query, jpqlQuery.getParameters());
        query.setFirstResult(jpqlQuery.getFirstResult());
        if (jpqlQuery.getMaxResults() > 0) {
            query.setMaxResults(jpqlQuery.getMaxResults());
        }
        return query.list();
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#getSingleResult(org.dayatang.domain.JpqlQuery)
     */
    @Override
    public <T> T getSingleResult(JpqlQuery jpqlQuery) {
        List<T> results = find(jpqlQuery);
        return results == null || results.isEmpty() ? null : results.get(0);
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#executeUpdate(org.dayatang.domain.JpqlQuery)
     */
    @Override
    public int executeUpdate(JpqlQuery jpqlQuery) {
        Query query = getSession().createQuery(jpqlQuery.getJpql());
        fillParameters(query, jpqlQuery.getParameters());
        return query.executeUpdate();
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#createNamedQuery(java.lang.String)
     */
    @Override
    public NamedQuery createNamedQuery(String queryName) {
        return new NamedQuery(this, queryName);
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#find(org.dayatang.domain.NamedQuery)
     */
    @Override
    public <T> List<T> find(NamedQuery namedQuery) {
        Query query = getSession().getNamedQuery(namedQuery.getQueryName());
        fillParameters(query, namedQuery.getParameters());
        query.setFirstResult(namedQuery.getFirstResult());
        if (namedQuery.getMaxResults() > 0) {
            query.setMaxResults(namedQuery.getMaxResults());
        }
        return query.list();
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#getSingleResult(org.dayatang.domain.NamedQuery)
     */
    @Override
    public <T> T getSingleResult(NamedQuery namedQuery) {
        List<T> results = find(namedQuery);
        return results == null || results.isEmpty() ? null : results.get(0);
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#executeUpdate(org.dayatang.domain.NamedQuery)
     */
    @Override
    public int executeUpdate(NamedQuery namedQuery) {
        Query query = getSession().getNamedQuery(namedQuery.getQueryName());
        fillParameters(query, namedQuery.getParameters());
        return query.executeUpdate();
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#findByExample(org.dayatang.domain.Entity, org.dayatang.domain.ExampleSettings)
     */
    @Override
    public <T extends Entity, E extends T> List<T> findByExample(final E example, final ExampleSettings<T> settings) {
        Example theExample = Example.create(example);
        if (settings.isLikeEnabled()) {
            theExample.enableLike(MatchMode.ANYWHERE);
        }
        if (settings.isIgnoreCaseEnabled()) {
            theExample.ignoreCase();
        }
        if (settings.isExcludeNone()) {
            theExample.excludeNone();
        }
        if (settings.isExcludeZeroes()) {
            theExample.excludeZeroes();
        }
        for (String propName : settings.getExcludedProperties()) {
            theExample.excludeProperty(propName);
        }
        return getSession().createCriteria(settings.getEntityClass()).add(theExample).list();
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#findByProperty(java.lang.Class, java.lang.String, java.lang.Object)
     */
    @Override
    public <T extends Entity> List<T> findByProperty(Class<T> clazz, String propertyName, Object propertyValue) {
        return find(new CriteriaQuery(this, clazz).eq(propertyName, propertyValue));
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#findByProperties(java.lang.Class, java.util.Map)
     */
    @Override
    public <T extends Entity> List<T> findByProperties(Class<T> clazz, Map<String, Object> properties) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(this, clazz);
        for (Map.Entry<String, Object> each : properties.entrySet()) {
            criteriaQuery = criteriaQuery.eq(each.getKey(), each.getValue());
        }
        return find(criteriaQuery);
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#flush()
     */
    @Override
    public void flush() {
        getSession().flush();
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#refresh(org.dayatang.domain.Entity)
     */
    @Override
    public void refresh(Entity entity) {
        getSession().refresh(entity);
    }

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.EntityRepository#clear()
     */
    @Override
    public void clear() {
        getSession().clear();
    }
}
