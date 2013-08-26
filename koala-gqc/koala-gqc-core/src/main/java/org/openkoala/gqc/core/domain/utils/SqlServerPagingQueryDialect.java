package org.openkoala.gqc.core.domain.utils;

/**
 * SQLServer分页查询方言
 * @author xmfang
 *
 */
public class SqlServerPagingQueryDialect extends PagingQueryDialect {

	@Override
	public SqlStatmentMode generatePagingQueryStatement() {
		SqlStatmentMode result = getQuerySql();
		String statment = result.getStatment();
		result.setStatment( new StringBuilder( statment.length() + 8 )
				.append( statment )
				.insert( getAfterSelectInsertPoint( statment ), " top " + getPagesize() )
				.toString() );
		return result;
	}

	private int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf( "select" );
		final int selectDistinctIndex = sql.toLowerCase().indexOf( "select distinct" );
		return selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
	}

}
