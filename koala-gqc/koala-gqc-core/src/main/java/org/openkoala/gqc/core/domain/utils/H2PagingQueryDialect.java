package org.openkoala.gqc.core.domain.utils;

/**
 * H2分页查询方言
 * @author xmfang
 *
 */
public class H2PagingQueryDialect extends PagingQueryDialect {

	@Override
	public String generatePagingQueryStatement() {
		return new StringBuilder( getQuerySql().length() + 20 )
		.append( getQuerySql() )
		.append( " limit " + getPagesize() + " offset " + getFirstRow() )
		.toString();
	}

}
