package com.dayatang.querychannel.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.dayatang.IocInstanceNotFoundException;
import com.dayatang.domain.Entity;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QuerySettings;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@SuppressWarnings("unchecked")
@Named("dddlib_querychannel_service_jpa")
public class QueryChannelServiceJpa implements QueryChannelService {

	private static final long serialVersionUID = -2520631490347218114L;

	@Inject
	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		if (entityManager != null) {
			return entityManager;
		}

		try {
			return InstanceFactory.getInstance(EntityManager.class);
		} catch (IocInstanceNotFoundException e) {
			EntityManagerFactory entityManagerFactory = InstanceFactory
					.getInstance(EntityManagerFactory.class);
			return entityManagerFactory.createEntityManager();
		}
	}

	public QueryChannelServiceJpa() {
	}

	public QueryChannelServiceJpa(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Query createQuery(String hql, Object[] params) {
		Query query = getEntityManager().createQuery(hql);
		return setParameters(query, params);
	}

	@SuppressWarnings("unused")
	private Query createNamedQuery(String queryName, Object[] params) {
		Query query = getEntityManager().createNamedQuery(queryName);
		return setParameters(query, params);
	}

	private Query setParameters(Query query, Object[] params) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		return query;
	}

	/**
	 * 获取任一页第一条数据在数据集的位置.
	 * 
	 * @param currentPage
	 *            从1开始的当前页号
	 * @param pageSize
	 *            每页记录条数
	 * @return 该页第一条数据号
	 */
	private static int getFirstRow(int currentPage, int pageSize) {
		return Page.getStartOfPage(currentPage, pageSize);
	}

	/**
	 * 构造一个查询数据条数的语句,不能用于union
	 * 
	 * @param queryStr
	 *            源语句
	 * @return 查询数据条数的语句
	 */
	private static String buildCountQueryStr(String queryStr) {
		String removedOrdersQueryStr = removeOrders(queryStr);
		int index = removedOrdersQueryStr.toLowerCase().indexOf(" from ");

		StringBuilder builder = assemCountString(removedOrdersQueryStr, index);

		if (index != -1) {
			builder.append(removedOrdersQueryStr.substring(index));
		} else {
			builder.append(removedOrdersQueryStr);
		}
		return builder.toString();
	}

	private static StringBuilder assemCountString(String hql, int fromIndex) {

		String strInCount = "*";

		int distinctIndex = -1;

		int tempdistinctIndex = hql.indexOf("distinct(");
		if (tempdistinctIndex != -1 && tempdistinctIndex < fromIndex) {
			distinctIndex = tempdistinctIndex;
		}
		tempdistinctIndex = hql.indexOf("distinct ");
		if (tempdistinctIndex != -1 && tempdistinctIndex < fromIndex) {
			distinctIndex = tempdistinctIndex;
		}

		if (distinctIndex != -1) {
			String distinctToFrom = hql.substring(distinctIndex, fromIndex);

			// 除去“,”之后的语句
			int commaIndex = distinctToFrom.indexOf(",");
			String strMayBeWithAs = "";
			if (commaIndex != -1) {
				strMayBeWithAs = distinctToFrom.substring(0, commaIndex);
			} else {
				strMayBeWithAs = distinctToFrom;
			}

			// 除去as语句
			int asIndex = strMayBeWithAs.indexOf(" as ");
			if (asIndex != -1) {
				strInCount = strMayBeWithAs.substring(0, asIndex);
			} else {
				strInCount = strMayBeWithAs;
			}

			// 除去()，因为hql不支持 select count(distinct (...))，但支持select count(distinct ...)
			strInCount = strInCount.replace("(", " ");
			strInCount = strInCount.replace(")", " ");

		}

		StringBuilder builder = new StringBuilder("select count(" + strInCount
				+ ") ");

		return builder;
	}

	/**
	 * 去除查询语句的orderby 子句
	 * 
	 */
	private static String removeOrders(String queryStr) {
		Matcher m = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE).matcher(queryStr);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 获取每页数, 如果pageSize为0, 则使用Page对象中默认的值
	 * 
	 * @param pageSize
	 * @return
	 */
	private int getPageSize(int pageSize) {
		return (pageSize == 0) ? Page.DEFAULT_PAGE_SIZE : pageSize;
	}

	private long countSizeInSession(final String queryStr, final Object[] params) {

		long totalCount = 0;

		String countQueryString = "";
		boolean containGroup = false;

		int groupIndex = queryStr.toLowerCase().indexOf(" group by ");
		if (groupIndex != -1) {// 用了group by 则计算group by记录的数量
			containGroup = true;
			countQueryString = removeOrders(queryStr);
		} else {
			countQueryString = buildCountQueryStr(queryStr);
		}

		Query query = createQuery(countQueryString, params);

		if (containGroup) {
			totalCount = query.getResultList().size();
		} else {
			List<Long> count = query.getResultList();
			if (!count.isEmpty()) {
				totalCount = count.get(0);
			}
		}
		return totalCount;
	}

	@Override
	public <T> T querySingleResult(final String queryStr, final Object[] params) {
		Query query = createQuery(queryStr, params);
		List<T> list = query.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public <T> List<T> queryResult(final String queryStr, final Object[] params) {
		Query query = createQuery(queryStr, params);
		return query.getResultList();
	}

	@Override
	public <T> List<T> queryResult(final String queryStr,
			final Object[] params, final long firstRow, final int pageSize) {
		Query query = createQuery(queryStr, params);
		query.setFirstResult(Long.valueOf(firstRow).intValue()).setMaxResults(
				pageSize);
		return query.getResultList();
	}

	@Override
	public long queryResultSize(final String queryStr, final Object[] params) {
		return countSizeInSession(queryStr, params);
	}

	@Override
	public List<Map<String, Object>> queryMapResult(final String queryStr,
			final Object[] params) {

		throw new UnsupportedOperationException("not implemented yet!");

	}

	@Override
	public <T> Page<T> queryPagedResult(final String queryStr,
			final Object[] params, final long firstRow, final int pageSize) {
		long totalCount = countSizeInSession(queryStr, params);

		List<T> data = createQuery(queryStr, params)
				.setFirstResult(Long.valueOf(firstRow).intValue())
				.setMaxResults(getPageSize(pageSize)).getResultList();

		return new Page<T>(firstRow, totalCount, getPageSize(pageSize), data);
	}

	@Override
	public <T> Page<T> queryPagedResultByPageNo(String queryStr,
			Object[] params, int currentPage, int pageSize) {

		final int firstRow = getFirstRow(currentPage, pageSize);

		return queryPagedResult(queryStr, params, firstRow, pageSize);
	}

	@Override
	public <T> Page<T> queryPagedResultByNamedQuery(final String queryName,
			final Object[] params, final long firstRow, final int pageSize) {

		throw new UnsupportedOperationException("not implemented yet!");
	}

	@Override
	public <T> Page<T> queryPagedResultByPageNoAndNamedQuery(String queryName,
			Object[] params, int currentPage, int pageSize) {

		throw new UnsupportedOperationException("not implemented yet!");
	}

	@Override
	public Page<Map<String, Object>> queryPagedMapResult(final String queryStr,
			final Object[] params, int currentPage, final int pageSize) {

		throw new UnsupportedOperationException("not implemented yet!");
	}

	@Override
	public Page<Map<String, Object>> queryPagedMapResultByNamedQuery(
			final String queryName, final Object[] params, int currentPage,
			final int pageSize) {

		throw new UnsupportedOperationException("not implemented yet!");
	}

	@Override
	public <T extends Entity> Page<T> queryPagedByQuerySettings(
			QuerySettings<T> settings, int currentPage, int pageSize) {
		throw new UnsupportedOperationException("not implemented yet!");
	}
}
