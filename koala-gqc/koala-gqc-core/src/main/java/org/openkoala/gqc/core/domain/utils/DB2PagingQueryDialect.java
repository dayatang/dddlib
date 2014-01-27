package org.openkoala.gqc.core.domain.utils;

/**
 * DB2分页查询方言
 * @author xmfang
 *
 */
public class DB2PagingQueryDialect extends PagingQueryDialect {
	
	/**
	 * 初始大小
	 */
	private final int INIT_FITTED_VALUE = 200;

	@Override
	public SqlStatmentMode generatePagingQueryStatement() {
		int offset = getFirstRow();
		int limit = getPagesize();
		SqlStatmentMode result = getQuerySql();
		
		if ( offset == 0 ) {
			result.setStatment(result.getStatment() + " fetch first " + limit + " rows only");
			return result;
		}
		
		String sql = result.getStatment();
		StringBuilder pagingSelect = new StringBuilder( sql.length() + INIT_FITTED_VALUE )
				.append(
						"select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( "
				)
				.append( sql )  //nest the main query in an outer select
				.append( " fetch first " )
				.append( limit )
				.append( " rows only ) as inner2_ ) as inner1_ where rownumber_ > " )
				.append( offset )
				.append( " order by rownumber_" );
		result.setStatment(pagingSelect.toString());
		
		return result;
	}

}
