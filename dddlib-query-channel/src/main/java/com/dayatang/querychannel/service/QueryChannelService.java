package com.dayatang.querychannel.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.dayatang.domain.Entity;
import com.dayatang.domain.QuerySettings;
import com.dayatang.querychannel.support.Page;

public interface QueryChannelService extends Serializable {

	/**
	 * 根据查询语句和查询参数，返回查询结果的数量
	 * 
	 * @param queryStr
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @return 查询结果数量
	 */
	long queryResultSize(String queryStr, Object[] params);

	<T> List<T> queryResult(String queryStr, Object[] params, long firstRow, int pageSize);

	/**
	 * 若结果有多个，返回第一个；没有结果返回NULL
	 * 
	 * @param <T>
	 * @param queryStr
	 * @param params
	 * @return
	 */
	<T> T querySingleResult(String queryStr, Object[] params);

	<T> List<T> queryResult(String queryStr, Object[] params);

	List<Map<String, Object>> queryMapResult(String queryStr, Object[] params);

	<T> Page<T> queryPagedResult(String queryStr, Object[] params, long firstRow, int pageSize);

	<T> Page<T> queryPagedResultByPageNo(String queryStr, Object[] params, int currentPage, int pageSize);

	<T> Page<T> queryPagedResultByNamedQuery(String queryName, Object[] params, long firstRow, int pageSize);

	<T> Page<T> queryPagedResultByPageNoAndNamedQuery(String queryName, Object[] params, int currentPage, int pageSize);

	Page<Map<String, Object>> queryPagedMapResult(String queryStr, Object[] params, int currentPage, int pageSize);

	Page<Map<String, Object>> queryPagedMapResultByNamedQuery(String queryName, Object[] params, int currentPage,
			int pageSize);

	<T extends Entity> Page<T> queryPagedByQuerySettings(QuerySettings<T> settings, int currentPage, int pageSize);
}
