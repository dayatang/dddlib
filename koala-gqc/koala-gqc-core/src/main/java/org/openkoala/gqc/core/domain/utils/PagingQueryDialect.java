package org.openkoala.gqc.core.domain.utils;

/**
 * 分页查询方言
 * @author xmfang
 *
 */
public abstract class PagingQueryDialect {
	
	/**
	 * sql
	 */
	private SqlStatmentMode querySql;
	
	/**
	 * 当前页第一行索引
	 */
	private int firstRow;
	
	/**
	 * 每页大小
	 */
	private int pagesize;
	
	public SqlStatmentMode getQuerySql() {
		return querySql;
	}

	public void setQuerySql(SqlStatmentMode querySql) {
		this.querySql = querySql;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public PagingQueryDialect() {}
	
	public PagingQueryDialect(SqlStatmentMode querySql, int firstRow, int pagesize) {
		this.querySql = querySql;
		this.firstRow = firstRow;
		this.pagesize = pagesize;
	}
	
	/**
	 * 获得一个分页查询方言的实例
	 * @return
	 */
	public abstract SqlStatmentMode generatePagingQueryStatement();
	
}
