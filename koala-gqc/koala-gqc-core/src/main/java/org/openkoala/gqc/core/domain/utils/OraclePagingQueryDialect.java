package org.openkoala.gqc.core.domain.utils;

/**
 * Oracle分页查询方言
 * @author xmfang
 *
 */
public class OraclePagingQueryDialect extends PagingQueryDialect {

	@Override
	public String generatePagingQueryStatement() {
		String sql = getQuerySql();
		sql = sql.trim();

		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 );
		pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ ) where rownum_ <= " + (getFirstRow() + getPagesize()) + " and rownum_ > " + getFirstRow());

		return pagingSelect.toString();
	}


//	@Override
//	public String generateQuerySql() {
//		StringBuilder result = new StringBuilder();
//		CommonSqlUtils commonSqlUtils = new CommonSqlUtils(getGeneralQuery());
//		
//		result.append("SELECT * FROM (SELECT ROWNUM r,");
//		
//		for (FieldDetail fieldDetail : getGeneralQuery().getFieldDetails()) {
//			result.append("t1." + fieldDetail.getFieldName() + ",");
//		}
//		result.deleteCharAt(result.length() - 1);
//		
//		result.append(" FROM " + getGeneralQuery().getTableName() + " t1 where rownum<" + (getFirstRow() + getPagesize()) + ") t2");
//		result.append(" where t2.r>=" + getFirstRow());
//		result.append(commonSqlUtils.generatePreQueryConditionStatement());
//		result.append(commonSqlUtils.generateDynamicQueryConditionStatement());
//		
//		return result.toString();
//	}

}
