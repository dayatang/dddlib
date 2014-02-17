package org.dayatang.querychannel.service.impl;

import org.dayatang.domain.IocInstanceNotFoundException;
import org.dayatang.domain.CriteriaQuery;
import org.dayatang.domain.Entity;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.service.QueryChannelService;
import org.dayatang.querychannel.service.Page;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dayatang.querychannel.service.QueryChannelService1;

@SuppressWarnings("unchecked")
public class QueryChannelServiceHibernate implements QueryChannelService1 {

	private static final long serialVersionUID = -2520631490347218114L;
        
        private SessionFactory sessionFactory;

	public QueryChannelServiceHibernate() {
	}

    public QueryChannelServiceHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	public Session getSession() {
        try {
            return InstanceFactory.getInstance(Session.class);
        } catch (IocInstanceNotFoundException e) {
            if (sessionFactory == null) {
                sessionFactory = InstanceFactory.getInstance(SessionFactory.class);
            }
            return sessionFactory.getCurrentSession();
        }
	}

	private Query createQuery(String queryStr, Object[] params) {
		Query query = getSession().createQuery(queryStr);
		return setParameters(query, params);
	}

	private Query createNamedQuery(String queryName, Object[] params) {
		Query query = getSession().getNamedQuery(queryName);
		return setParameters(query, params);
	}

	private Query setParameters(Query query, Object[] params) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
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

	private static StringBuilder assemCountString(String queryStr, int fromIndex) {

		String strInCount = "*";

		int distinctIndex = -1;

		int tempdistinctIndex = queryStr.indexOf("distinct(");
		if (tempdistinctIndex != -1 && tempdistinctIndex < fromIndex) {
			distinctIndex = tempdistinctIndex;
		}
		tempdistinctIndex = queryStr.indexOf("distinct ");
		if (tempdistinctIndex != -1 && tempdistinctIndex < fromIndex) {
			distinctIndex = tempdistinctIndex;
		}

		if (distinctIndex != -1) {
			String distinctToFrom = queryStr.substring(distinctIndex, fromIndex);

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

			// 除去()，因为HQL不支持 select count(distinct (...))，但支持select count(distinct ...)
			strInCount = strInCount.replace("(", " ");
			strInCount = strInCount.replace(")", " ");

		}

		StringBuilder builder = new StringBuilder("select count(" + strInCount + ") ");

		return builder;
	}

	/**
	 * 去除查询语句的orderby 子句
	 * 
	 */
	private static String removeOrders(String queryStr) {
		Matcher m = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE).matcher(queryStr);
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
		if (containGroupBy(queryStr)) {
			List<Long> rows = createQuery(removeOrders(queryStr), params).list();
			return rows == null ? 0 : rows.size();
		} else {
			List<Long> rows = createQuery(buildCountQueryStr(queryStr), params).list();
			return rows == null || rows.isEmpty() ? 0 : rows.get(0);
		}
	}

	private boolean containGroupBy(String hql) {
		return hql.toLowerCase().indexOf(" group by ") > -1;
	}
	
	
	@Override
	public <T> T querySingleResult(final String queryStr, final Object[] params) {
		Query query = createQuery(queryStr, params);
		List<T> list = query.list();
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public <T> List<T> queryResult(final String queryStr, final Object[] params) {
		Query query = createQuery(queryStr, params);
		return query.list();
	}

	@Override
	public <T> List<T> queryResult(final String queryStr, final Object[] params, final long firstRow, final int pageSize) {
		Query query = createQuery(queryStr, params);
		query.setFirstResult(Long.valueOf(firstRow).intValue()).setMaxResults(pageSize);
		return query.list();
	}

	@Override
	public long queryResultSize(final String queryStr, final Object[] params) {
		return countSizeInSession(queryStr, params);
	}

	@Override
	public List<Map<String, Object>> queryMapResult(final String queryStr, final Object[] params) {
		Query query = createQuery(queryStr, params);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public <T> Page<T> queryPagedResult(final String queryStr, final Object[] params, final long firstRow,
			final int pageSize) {
		long totalCount = countSizeInSession(queryStr, params);
		List<T> data = createQuery(queryStr, params)
				.setFirstResult(Long.valueOf(firstRow).intValue())
				.setMaxResults(getPageSize(pageSize))
				.list();
		return new Page<T>(firstRow, totalCount, getPageSize(pageSize), data);
	}

	@Override
	public <T> Page<T> queryPagedResultByPageNo(String queryStr, Object[] params, int currentPage, int pageSize) {
		final int firstRow = getFirstRow(currentPage, pageSize);
		return queryPagedResult(queryStr, params, firstRow, pageSize);
	}

	@Override
	public <T> Page<T> queryPagedResultByNamedQuery(final String queryName, final Object[] params, final long firstRow,
			final int pageSize) {
		String queryStr = getSession().getNamedQuery(queryName).getQueryString();
		long totalCount = countSizeInSession(queryStr, params);
		List<T> data = createNamedQuery(queryName, params)
				.setFirstResult(Long.valueOf(firstRow).intValue()).setMaxResults(getPageSize(pageSize)).list();
		return new Page<T>(firstRow, totalCount, getPageSize(pageSize), data);
	}

	@Override
	public <T> Page<T> queryPagedResultByPageNoAndNamedQuery(String queryName, Object[] params, int currentPage,
			int pageSize) {
		final int firstRow = getFirstRow(currentPage, pageSize);
		return queryPagedResultByNamedQuery(queryName, params, firstRow, pageSize);
	}

	@Override
	public Page<Map<String, Object>> queryPagedMapResult(final String queryStr, final Object[] params, int currentPage,
			final int pageSize) {
		final int firstRow = getFirstRow(currentPage, pageSize);
		long totalCount = countSizeInSession(queryStr, params);
		List<Map<String, Object>> data = createQuery(queryStr, params)
				.setFirstResult(Long.valueOf(firstRow).intValue()).setMaxResults(getPageSize(pageSize))
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		return new Page<Map<String, Object>>(firstRow, totalCount, getPageSize(pageSize), data);
	}

	@Override
	public Page<Map<String, Object>> queryPagedMapResultByNamedQuery(final String queryName, final Object[] params,
			int currentPage, final int pageSize) {

		final int firstRow = getFirstRow(currentPage, pageSize);
		String queryStr = getSession().getNamedQuery(queryName).getQueryString();

		long totalCount = countSizeInSession(queryStr, params);

		List<Map<String, Object>> data = createNamedQuery(queryName, params)
				.setFirstResult(Long.valueOf(firstRow).intValue()).setMaxResults(getPageSize(pageSize))
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

		return new Page<Map<String, Object>>(firstRow, totalCount, getPageSize(pageSize), data);
	}

	@Override
	public <T extends Entity> Page<T> queryPagedByCriteriaQuery(CriteriaQuery criteriaQuery, int currentPage, int pageSize) {
		throw new UnsupportedOperationException("还没有实现。");
	}
}
