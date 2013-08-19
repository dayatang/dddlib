package org.openkoala.gqc.core.domain.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.openkoala.gqc.core.domain.DataSource;

/**
 * 分页查询器
 * @author xmfang
 *
 */
public class PagingQuerier extends Querier {
	
	/**
	 * 初始默认大小
	 */
	private final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 分页查询开始值
	 */
	private int firstRow = 0;
	
	/**
	 * 分页查询每页查询数
	 */
	private int pagesize = DEFAULT_PAGE_SIZE;
	
	public int getFirstRow() {
		return firstRow;
	}

	public int getPagesize() {
		return pagesize;
	}
	
	public PagingQuerier(String querySql, DataSource dataSource) {
		super(querySql, dataSource);
	}
	
	public void changePagingParameter(int firstRow, int pagesize) {
		this.firstRow = firstRow;
		this.pagesize = pagesize;
	}
	
	@Override
	public String generateQuerySql() {
		String result = null;
		Connection connection = null;
        try {
        	connection = getConnection();
            result = generatePagingQuerySql(connection.getMetaData());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        	closeConnection(connection);
        }
		return result;
	}
	
	/**
	 * 获取记录总数
	 * @return
	 */
	public long getTotalCount() {
		Number result = null;
		Connection connection = null;

        try {
            connection = getConnection();
            QueryRunner queryRunner = new QueryRunner();
            result = queryRunner.query(connection, generateQueryTotalCountSql(), new ScalarHandler<Number>());
        } catch (SQLException e) {
        	throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
		
		if (result == null) {
			return 0;
		}
		return result.longValue();
	}
	
	private String generatePagingQuerySql(DatabaseMetaData databaseMetaData) throws SQLException {
		String databaseName = databaseMetaData.getDatabaseProductName();
		PagingQueryDialect pagingQueryDialect = PagingQueryDialectResolver.getPagingQuerierInstance(databaseName);
		
		if (pagingQueryDialect == null) {
			throw new RuntimeException("Paging query do not support " + databaseName + "Dababase");
		}
		
		pagingQueryDialect.setQuerySql(getQuerySql());
		pagingQueryDialect.setFirstRow(firstRow);
		pagingQueryDialect.setPagesize(pagesize);
		
		return pagingQueryDialect.generatePagingQueryStatement();
	}
	
	private String generateQueryTotalCountSql() {
		String result = getQuerySql().substring(getQuerySql().indexOf("from"), getQuerySql().length());
		result = "select count(*) " + result;
		return result;
	}
	
	/*private String generatePagingQuerySql(DatabaseMetaData databaseMetaData) {
		StandardDialectResolver standardDialectResolver = new StandardDialectResolver();
		Dialect dialect = standardDialectResolver.resolveDialect(databaseMetaData);
		
		RowSelection selection = new RowSelection();
		selection.setFetchSize(pagesize);
		selection.setFirstRow(firstRow);
		selection.setMaxRows(pagesize);
		
		LimitHandler limitHandler = dialect.buildLimitHandler(generateNormalQuerySql(), selection);
		return limitHandler.getProcessedSql();
	}*/
	
}
