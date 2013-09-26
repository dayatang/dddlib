package org.openkoala.gqc.core.domain.utils;

/**
 * H2分页查询方言
 * @author xmfang
 *
 */
public class H2PagingQueryDialect extends PagingQueryDialect {

	@Override
	public SqlStatmentMode generatePagingQueryStatement() {
		SqlStatmentMode result = getQuerySql();
		result.setStatment(new StringBuilder( result.getStatment().length() + 20 )
			.append( result.getStatment() )
			.append( " limit " + getPagesize() + " offset " + getFirstRow() )
			.toString());
		
		return result;
	}

}
