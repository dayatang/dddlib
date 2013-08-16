package org.openkoala.gqc.core.domain.utils;

import org.openkoala.gqc.core.domain.DataSource;

/**
 * 查询所有记录查询器
 * @author xmfang
 *
 */
public class QueryAllQuerier extends Querier {

	public QueryAllQuerier(String querySql, DataSource dataSource) {
		super(querySql, dataSource);
	}

	@Override
	public String generateQuerySql() {
		return getQuerySql();
	}

}
