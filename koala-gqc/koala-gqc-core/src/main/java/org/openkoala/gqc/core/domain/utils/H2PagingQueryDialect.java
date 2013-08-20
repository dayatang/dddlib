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
	private final int INIT_SIZE = 20;

	@Override
	public String generatePagingQueryStatement() {
		return new StringBuilder( getQuerySql().length() + INIT_SIZE )
		.append( getQuerySql() )
		.append( " limit " + getPagesize() + " offset " + getFirstRow() )
		.toString();
	}

}
