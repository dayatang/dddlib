package org.dayatang.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.IocException;
import org.dayatang.domain.*;
import org.dayatang.hibernate.internal.QueryTranslator;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用仓储接口的Hibernate实现。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
@SuppressWarnings({"unchecked", "deprecation"})
public class EntityRepositoryHibernate implements EntityRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRepositoryHibernate.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dayatang.domain.EntityRepository#save(org.dayatang.domain.Entity)
     */
    @Override
    public <T extends Entity> T save(T entity) {
        if (entity.isNew()) {
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
     * 
     * @see
     * org.dayatang.domain.EntityRepository#remove(org.dayatang.domain.Entity)
     */
    @Override
    public void remove(Entity entity) {
        getSession().delete(entity);
        LOGGER.info("remove a entity: " + entity.getClass() + "/" + entity.getId() + ".");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dayatang.domain.EntityRepository#exists(java.io.Serializable)
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
     * 
     * @see org.dayatang.domain.EntityRepository#load(java.io.Serializable)
     */
    @Override
    public <T extends Entity> T load(final Class<T> clazz, final Serializable id) {
        return (T) getSession().load(clazz, id);
    }

    @Override
    public <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
        getSession().evict(entity);
        return get(clazz, entity.getId());
    }

    @Override
    public <T extends Entity> List<T> findAll(Class<T> clazz) {
        return getSession().createCriteria(clazz).list();
    }

    @Override
    public <T extends Entity> List<T> find(final QuerySettings<T> settings) {
        QueryTranslator translator = new QueryTranslator(settings);
        String queryString = translator.getQueryString();
        LOGGER.info("QueryString: '" + queryString + "'");
        List<Object> params = translator.getParams();
        LOGGER.info("params: " + StringUtils.join(params, ", "));
        Query query = getSession().createQuery(queryString);
        fillParameters(query, ArrayParameters.create(params));
        query.setFirstResult(settings.getFirstResult());
        if (settings.getMaxResults() > 0) {
            query.setMaxResults(settings.getMaxResults());
        }
        return query.list();
    }

    @Override
    public <T> List<T> find(final String queryString, final QueryParameters params) {
        Query query = getSession().createQuery(queryString);
        fillParameters(query, params);
        return query.list();
    }

    @Override
    public <T> List<T> findByNamedQuery(final String queryName, final QueryParameters params) {
        Query query = getSession().getNamedQuery(queryName);
        fillParameters(query, params);
        return query.list();
    }

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

    @Override
    public <T extends Entity> List<T> findByProperty(Class<T> clazz, String propertyName, Object propertyValue) {
        QuerySettings<T> querySettings = QuerySettings.create(clazz).eq(propertyName, propertyValue);
        return find(querySettings);
    }

    @Override
    public <T extends Entity> List<T> findByProperties(Class<T> clazz, Map<String, Object> properties) {
        QuerySettings<T> querySettings = QuerySettings.create(clazz);
        for (Map.Entry<String, Object> each : properties.entrySet()) {
            querySettings = querySettings.eq(each.getKey(), each.getValue());
        }
        return find(querySettings);
    }

    @Override
    public <T extends Entity> T getSingleResult(QuerySettings<T> settings) {
        List<T> list = find(settings);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public <T> T getSingleResult(final String queryString, final QueryParameters params) {
        Query query = getSession().createQuery(queryString);
        fillParameters(query, params);
        return (T) query.uniqueResult();
    }

    @Override
    public void executeUpdate(final String queryString, final QueryParameters params) {
        Query query = getSession().createQuery(queryString);
        fillParameters(query, params);
        query.executeUpdate();
    }

    @Override
    public void flush() {
        getSession().flush();
    }

    @Override
    public void refresh(Entity entity) {
        getSession().refresh(entity);
    }

    @Override
    public void clear() {
        getSession().clear();
    }

    @Override
    public <T extends Entity> CriteriaQuery createCriteriaQuery(Class<T> entityClass) {
        return new CriteriaQueryImpl(this, entityClass);
    }

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

    @Override
    public <T> T getSingleResult(CriteriaQuery dddQuery) {
        List<T> results = find(dddQuery);
        return results == null || results.isEmpty() ? null : results.get(0);
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

    @Override
    public <T> List<T> find(JpqlQuery jpqlQuery) {
        Query query = getSession().createQuery(jpqlQuery.getJpql());
        fillParameters(query, jpqlQuery.getParams());
        query.setFirstResult(jpqlQuery.getFirstResult());
        if (jpqlQuery.getMaxResults() > 0) {
            query.setMaxResults(jpqlQuery.getMaxResults());
        }
        return query.list();
    }

    @Override
    public <T> T getSingleResult(JpqlQuery jpqlQuery) {
        List<T> results = find(jpqlQuery);
        return results == null || results.isEmpty() ? null : results.get(0);
    }

    @Override
    public <T> List<T> find(NamedQuery namedQuery) {
        Query query = getSession().getNamedQuery(namedQuery.getQueryName());
        fillParameters(query, namedQuery.getParams());
        query.setFirstResult(namedQuery.getFirstResult());
        if (namedQuery.getMaxResults() > 0) {
            query.setMaxResults(namedQuery.getMaxResults());
        }
        return query.list();
    }

    @Override
    public <T> T getSingleResult(NamedQuery namedQuery) {
        List<T> results = find(namedQuery);
        return results == null || results.isEmpty() ? null : results.get(0);
    }
}
