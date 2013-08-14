package com.dayatang.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.IocException;
import com.dayatang.domain.DataPage;
import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.ExampleSettings;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QuerySettings;
import com.dayatang.hibernate.internal.QueryTranslator;

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
	 * com.dayatang.domain.EntityRepository#save(com.dayatang.domain.Entity)
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
	 * com.dayatang.domain.EntityRepository#remove(com.dayatang.domain.Entity)
	 */
	@Override
	public void remove(Entity entity) {
		getSession().delete(entity);
		LOGGER.info("remove a entity: " + entity.getClass() + "/" + entity.getId() + ".");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dayatang.domain.EntityRepository#exists(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> boolean exists(final Class<T> clazz, final Serializable id) {
		return get(clazz, id) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dayatang.domain.EntityRepository#get(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> T get(final Class<T> clazz, final Serializable id) {
		return (T) getSession().get(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dayatang.domain.EntityRepository#load(java.io.Serializable)
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
		for (int i = 0; i < params.size(); i++) {
			System.out.println("==================" + i + ": " + params.get(i));
			query.setParameter(i, params.get(i));
		}
		query.setFirstResult(settings.getFirstResult());
		if (settings.getMaxResults() > 0) {
			query.setMaxResults(settings.getMaxResults());
		}
		return query.list();
	}

	@Override
	public <T> List<T> find(final String queryString, final Object[] params, final Class<T> resultClass) {
		Query query = getSession().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		return query.list();
	}

	@Override
	public <T> List<T> find(final String queryString, final Map<String, Object> params, final Class<T> resultClass) {
		Query query = getSession().createQuery(queryString);
		for (Map.Entry<String, Object> each : params.entrySet()) {
			query = query.setParameter(each.getKey(), each.getValue());
		}
		return query.list();
	}

	@Override
	public <T> List<T> findByNamedQuery(final String queryName, final Object[] params, final Class<T> resultClass) {
		Query query = getSession().getNamedQuery(queryName);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		return query.list();
	}

	@Override
	public <T> List<T> findByNamedQuery(final String queryName, final Map<String, Object> params,
			final Class<T> resultClass) {
		Query query = getSession().getNamedQuery(queryName);
		for (Map.Entry<String, Object> each : params.entrySet()) {
			query = query.setParameter(each.getKey(), each.getValue());
		}
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
	public <T> T getSingleResult(final String queryString, final Object[] params, Class<T> resultClass) {
		Query query = getSession().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		return (T) query.uniqueResult();
	}

	@Override
	public <T> T getSingleResult(final String queryString, final Map<String, Object> params, Class<T> resultClass) {
		Query query = getSession().createQuery(queryString);
		for (Map.Entry<String, Object> each : params.entrySet()) {
			query = query.setParameter(each.getKey(), each.getValue());
		}
		return (T) query.uniqueResult();
	}

	@Override
	public void executeUpdate(final String queryString, final Object[] params) {
		Query query = getSession().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		query.executeUpdate();
	}

	@Override
	public void executeUpdate(final String queryString, final Map<String, Object> params) {
		Query query = getSession().createQuery(queryString);
		for (Map.Entry<String, Object> each : params.entrySet()) {
			query = query.setParameter(each.getKey(), each.getValue());
		}
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
	public <T extends Entity> DataPage<T> findAll(Class<T> clazz, int pageIndex, int pageSize) {
		List<T> data = findAll(clazz);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount : to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	@Override
	public <T extends Entity> DataPage<T> find(QuerySettings<T> settings, int pageIndex, int pageSize) {
		List<T> data = find(settings);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount : to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	@Override
	public <T> DataPage<T> find(String queryString, Object[] params, int pageIndex, int pageSize, Class<T> resultClass) {
		List<T> data = find(queryString, params, resultClass);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount : to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	@Override
	public <T> DataPage<T> find(String queryString, Map<String, Object> params, int pageIndex, int pageSize,
			Class<T> resultClass) {
		List<T> data = find(queryString, params, resultClass);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount : to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	@Override
	public <T> DataPage<T> findByNamedQuery(String queryName, Object[] params, int pageIndex, int pageSize,
			Class<T> resultClass) {
		List<T> data = findByNamedQuery(queryName, params, resultClass);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount : to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	@Override
	public <T> DataPage<T> findByNamedQuery(String queryName, Map<String, Object> params, int pageIndex, int pageSize,
			Class<T> resultClass) {
		List<T> data = findByNamedQuery(queryName, params, resultClass);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount : to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	private Session getSession() {
		try {
			return InstanceFactory.getInstance(Session.class);
		} catch (IocException e) {
			SessionFactory sessionFactory = InstanceFactory.getInstance(SessionFactory.class);
			return sessionFactory.getCurrentSession();
		}
	}
}
