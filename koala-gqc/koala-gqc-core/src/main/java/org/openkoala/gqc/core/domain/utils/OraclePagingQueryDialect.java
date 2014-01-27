package org.openkoala.gqc.core.domain.utils;

/**
 * Oracle分页查询方言
 * @author xmfang
 *
 */
public class OraclePagingQueryDialect extends PagingQueryDialect {
	
	/**
	 * 初始大小
	 */
	private final int INIT_FITTED_VALUE = 100;

	@Override
	public SqlStatmentMode generatePagingQueryStatement() {
		SqlStatmentMode result = getQuerySql();
		String sql = result.getStatment();
		sql = sql.trim();

		StringBuilder pagingSelect = new StringBuilder( sql.length() + INIT_FITTED_VALUE );
		pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ ) where rownum_ <= " + (getFirstRow() + getPagesize()) + " and rownum_ > " + getFirstRow());

		result.setStatment(pagingSelect.toString());
		return result;
	}


	/*@Override
	public String generateQuerySql() {
		StringBuilder result = new StringBuilder();
		CommonSqlUtils commonSqlUtils = new CommonSqlUtils(getGeneralQuery());
		
		result.append("SELECT * FROM (SELECT ROWNUM r,");
		
		for (FieldDetail fieldDetail : getGeneralQuery().getFieldDetails()) {
			result.append("t1." + fieldDetail.getFieldName() + ",");
		}
		result.deleteCharAt(result.length() - 1);
		
		result.append(" FROM " + getGeneralQuery().getTableName() + " t1 where rownum<" + (getFirstRow() + getPagesize()) + ") t2");
		result.append(" where t2.r>=" + getFirstRow());
		result.append(commonSqlUtils.generatePreQueryConditionStatement());
		result.append(commonSqlUtils.generateDynamicQueryConditionStatement());
		
		return result.toString();
	}*/

}
