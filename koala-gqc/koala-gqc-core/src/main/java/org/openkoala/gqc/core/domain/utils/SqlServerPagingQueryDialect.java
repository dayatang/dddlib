package org.openkoala.gqc.core.domain.utils;

/**
 * SQLServer分页查询方言
 * @author xmfang
 *
 */
public class SqlServerPagingQueryDialect extends PagingQueryDialect {
	
	/**
	 * 初始大小
	 */
	private final int INIT_SIZE = 8;
	
	/**
	 * select distinct索引位置
	 */
	private final int INDEX_WITH_DISTINCT = 15;
	
	/**
	 * select索引位置
	 */
	private final int INDEX_WITHOUT_DISTINCT = 6;

	@Override
	public String generatePagingQueryStatement() {
		return new StringBuilder( getQuerySql().length() + INIT_SIZE )
				.append( getQuerySql() )
				.insert( getAfterSelectInsertPoint( getQuerySql() ), " top " + getPagesize() )
				.toString();
	}

	private int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf( "select" );
		final int selectDistinctIndex = sql.toLowerCase().indexOf( "select distinct" );
		return selectIndex + ( selectDistinctIndex == selectIndex ? INDEX_WITH_DISTINCT : INDEX_WITHOUT_DISTINCT );
	}

}
