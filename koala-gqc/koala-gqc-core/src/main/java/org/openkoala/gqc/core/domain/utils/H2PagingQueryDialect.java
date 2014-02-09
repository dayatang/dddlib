package org.openkoala.gqc.core.domain.utils;

/**
 * H2分页查询方言
 * @author xmfang
 *
 */
public class H2PagingQueryDialect extends PagingQueryDialect {
	
	/**
	 * 初始大小
	 */
	private final int INIT_FITTED_VALUE = 20;

	@Override
	public SqlStatmentMode generatePagingQueryStatement() {
		SqlStatmentMode result = getQuerySql();
		result.setStatment(new StringBuilder( result.getStatment().length() + INIT_FITTED_VALUE )
			.append( result.getStatment() )
			.append( " limit " + getPagesize() + " offset " + getFirstRow() )
			.toString());
		
		return result;
	}

}
