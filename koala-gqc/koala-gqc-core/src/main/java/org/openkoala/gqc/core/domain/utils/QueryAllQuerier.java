package org.openkoala.gqc.core.domain.utils;

import org.openkoala.gqc.core.domain.DataSource;

/**
 * 查询所有记录查询器
 * @author xmfang
 *
 */
public class QueryAllQuerier extends Querier {

	public QueryAllQuerier(SqlStatmentMode querySql, DataSource dataSource) {
		super(querySql, dataSource);
	}

	@Override
	public SqlStatmentMode generateQuerySql() {
		return getQuerySql();
	}

}
