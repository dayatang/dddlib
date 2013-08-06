package org.openkoala.gqc.core.domain.utils;

/**
 * Mysql分页查询方言
 * @author xmfang
 *
 */
public class MySqlPagingQueryDialect extends PagingQueryDialect {

	@Override
	public String generatePagingQueryStatement() {
		StringBuilder result = new StringBuilder();
		result.append(getQuerySql());
		result.append(" LIMIT " + getFirstRow() + "," + getPagesize());
		
		return result.toString();
	}

}
