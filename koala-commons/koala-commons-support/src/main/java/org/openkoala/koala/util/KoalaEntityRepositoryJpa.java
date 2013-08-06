/*******************************************************************************
 * Copyright (c) 2012-9-14 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/


package org.openkoala.koala.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.domain.DataPage;
import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.ExampleSettings;
import com.dayatang.domain.QuerySettings;
import com.dayatang.jpa.internal.JpaCriteriaQueryBuilder;

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2012-9-14
 */
@SuppressWarnings("unchecked")
public class KoalaEntityRepositoryJpa implements EntityRepository {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KoalaEntityRepositoryJpa.class);


	EntityManager entityManager;

	public KoalaEntityRepositoryJpa() {
	}

	public <T extends Entity> T save(T entity) {
		if (entity.getId() == null) {
			getEntityManager().persist(entity);
			if(LOGGER.isDebugEnabled())LOGGER.debug("save a entity: " + entity.getClass() + "/"
					+ entity.getId() + ".");
		} else {
			entity = update(entity);
		}
		return entity;
	}

	public <T extends Entity> T update(T entity) {
		T result = getEntityManager().merge(entity);
		if(LOGGER.isDebugEnabled())LOGGER.debug("update a entity: " + entity.getClass() + "/"
				+ entity.getId() + ".");
		return result;
	}

	
	public void remove(Entity entity) {
		getEntityManager().remove(get(entity.getClass(), entity.getId()));
		if(LOGGER.isDebugEnabled())LOGGER.debug("remove a entity: " + entity.getClass() + "/"
				+ entity.getId() + ".");
	}

	
	public <T extends Entity> boolean exists(final Class<T> clazz,
			final Serializable id) {
		T entity = getEntityManager().find(clazz, id);
		return entity != null;
	}

	
	public <T extends Entity> T get(final Class<T> clazz, final Serializable id) {
		return getEntityManager().find(clazz, id);
	}

	
	public <T extends Entity> T load(final Class<T> clazz, final Serializable id) {
		return getEntityManager().getReference(clazz, id);
	}


	public <T extends Entity> T getUnmodified(final Class<T> clazz,
			final T entity) {
		getEntityManager().detach(entity);
		return get(clazz, entity.getId());
	}

	
	public <T extends Entity> List<T> findAll(final Class<T> clazz) {
		String queryString = "select o from " + clazz.getName() + " as o";
		return getEntityManager().createQuery(queryString).getResultList();
	}

	
	public <T extends Entity> List<T> find(final QuerySettings<T> settings) {
		CriteriaQuery<T> criteriaQuery = JpaCriteriaQueryBuilder.getInstance()
				.createCriteriaQuery(settings, getEntityManager());
		Query query = getEntityManager().createQuery(criteriaQuery);
		query.setFirstResult(settings.getFirstResult());
		if (settings.getMaxResults() > 0) {
			query.setMaxResults(settings.getMaxResults());
		}
		return query.getResultList();
	}


	public <T> List<T> find(final String queryString, final Object[] params,
			final Class<T> resultClass) {
		Query query = getEntityManager().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}

	
	public <T> List<T> find(final String queryString,
			final Map<String, Object> params, final Class<T> resultClass) {
		Query query = getEntityManager().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return query.getResultList();
	}

	
	public <T> List<T> findByNamedQuery(final String queryName,
			final Object[] params, final Class<T> resultClass) {
		Query query = getEntityManager().createNamedQuery(queryName);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}

	
	public <T> List<T> findByNamedQuery(final String queryName,
			final Map<String, Object> params, final Class<T> resultClass) {
		Query query = getEntityManager().createNamedQuery(queryName);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return query.getResultList();
	}

	
	public <T extends Entity, E extends T> List<T> findByExample(
			final E example, final ExampleSettings<T> settings) {
		//		Map<String, Object> propValues = new EntityUtils(example).getPropValues();
		//		Map<String, Class<?>> propTypes = new EntityUtils(example).getPropTypes();
		//		
		//		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		//		CriteriaQuery<E> query = (CriteriaQuery<E>) criteriaBuilder.createQuery(example.getClass());
		//		Root<E> root = (Root<E>) query.from(example.getClass());
		//		query.distinct(true);
		//		query.select(root);
		//		List<Predicate> predicates = new ArrayList<Predicate>();
		//		for (String prop : propValues.keySet()) {
		//			if (settings.getExcludedProperties().contains(prop)) {
		//				continue;
		//			}
		//			Object value = propValues.get(prop);
		//			if (value != null) {
		//				predicates.add(criteriaBuilder.equal(root.get(prop), criteriaBuilder.parameter(propTypes.get(prop), value)));
		//			}
		//		}
		//		

		throw new RuntimeException("not implemented yet!");
	}

	
	public <T extends Entity> T getSingleResult(QuerySettings<T> settings) {
		List<T> results = find(settings);
		return results.isEmpty() ? null : results.get(0);
	}

	
	public <T> T getSingleResult(final String queryString,
			final Object[] params, Class<T> resultClass) {
		Query query = getEntityManager().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return (T) query.getSingleResult();
	}

	
	public <T> T getSingleResult(final String queryString,
			final Map<String, Object> params, Class<T> resultClass) {
		Query query = getEntityManager().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return (T) query.getSingleResult();
	}

	
	public void executeUpdate(final String queryString, final Object[] params) {
		Query query = getEntityManager().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		query.executeUpdate();
	}

	
	public void executeUpdate(final String queryString,
			final Map<String, Object> params) {
		Query query = getEntityManager().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		query.executeUpdate();
	}

	
	public void flush() {
		getEntityManager().flush();
	}

	
	public void refresh(Entity entity) {
		getEntityManager().refresh(entity);
	}

	
	public <T extends Entity> DataPage<T> findAll(Class<T> clazz,
			int pageIndex, int pageSize) {
		List<T> data = findAll(clazz);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount
				: to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	
	public <T extends Entity> DataPage<T> find(QuerySettings<T> settings,
			int pageIndex, int pageSize) {
		List<T> data = find(settings);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount
				: to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	
	public <T> DataPage<T> find(String queryString, Object[] params,
			int pageIndex, int pageSize, Class<T> resultClass) {
		List<T> data = find(queryString, params, resultClass);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount
				: to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	
	public <T> DataPage<T> find(String queryString, Map<String, Object> params,
			int pageIndex, int pageSize, Class<T> resultClass) {
		List<T> data = find(queryString, params, resultClass);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount
				: to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	
	public <T> DataPage<T> findByNamedQuery(String queryName, Object[] params,
			int pageIndex, int pageSize, Class<T> resultClass) {
		List<T> data = findByNamedQuery(queryName, params, resultClass);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount
				: to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	
	public <T> DataPage<T> findByNamedQuery(String queryName,
			Map<String, Object> params, int pageIndex, int pageSize,
			Class<T> resultClass) {
		List<T> data = findByNamedQuery(queryName, params, resultClass);
		int from = (pageIndex - 1) * pageSize;
		int to = from + pageSize;
		int resultCount = data.size();
		List<T> pageDate = data.subList(from, to > resultCount ? resultCount
				: to);
		return new DataPage<T>(pageDate, pageIndex, pageSize, resultCount);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void clear() {
		getEntityManager().clear();
	}
}
