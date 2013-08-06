package org.openkoala.gqc.core.domain.utils;

/**
 * SQLServer分页查询方言
 * @author xmfang
 *
 */
public class SqlServerPagingQueryDialect extends PagingQueryDialect {

	@Override
	public String generatePagingQueryStatement() {
		return new StringBuilder( getQuerySql().length() + 8 )
				.append( getQuerySql() )
				.insert( getAfterSelectInsertPoint( getQuerySql() ), " top " + getPagesize() )
				.toString();
	}

	private int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf( "select" );
		final int selectDistinctIndex = sql.toLowerCase().indexOf( "select distinct" );
		return selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
	}

}
