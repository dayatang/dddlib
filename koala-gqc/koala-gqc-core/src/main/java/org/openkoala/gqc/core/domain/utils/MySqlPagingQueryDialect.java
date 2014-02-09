package org.openkoala.gqc.core.domain.utils;

/**
 * Mysql分页查询方言
 * @author xmfang
 *
 */
public class MySqlPagingQueryDialect extends PagingQueryDialect {

	@Override
	public SqlStatmentMode generatePagingQueryStatement() {
		SqlStatmentMode result = getQuerySql();
		
		result.setStatment(new StringBuilder()
			.append(getQuerySql().getStatment())
			.append(" LIMIT " + getFirstRow() + "," + getPagesize())
			.toString());
		
		return result;
	}

}
