package org.openkoala.gqc.core.domain.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author lambo
 *
 */
public class SqlServerPagingQueryDialectTest {

	/**
	 * 声明实例
	 */
	private SqlServerPagingQueryDialect sqlServerPagingQueryDialect;
	
	/**
	 * 初始化
	 */
	private String sql = "select * from DATA_SOURCES";
	/**
	 * 初始化
	 */
	private String sql2 = "select distinct * from DATA_SOURCES";

	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		sqlServerPagingQueryDialect = this.createAndInitSqlServerPagingQueryDialect(sql);
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		sqlServerPagingQueryDialect = null;
	}

	/**
	 * 测试
	 */
	@Test
	public void testGeneratePagingQueryStatement() {
		String sqlExpected = "select top 10 * from DATA_SOURCES";
		String sql = sqlServerPagingQueryDialect.generatePagingQueryStatement();
		assertEquals(sqlExpected, sql);
		
		sqlServerPagingQueryDialect = this.createAndInitSqlServerPagingQueryDialect(sql2);
		String sqlExpected2 = "select distinct top 10 * from DATA_SOURCES";
		String sql2 = sqlServerPagingQueryDialect.generatePagingQueryStatement();
		assertEquals(sqlExpected2, sql2);
	}

	/**
	 * 创建实例
	 * @param firstRow
	 * @return
	 */
	private SqlServerPagingQueryDialect createAndInitSqlServerPagingQueryDialect(String sql){
		SqlServerPagingQueryDialect sqlServerPagingQueryDialect = new SqlServerPagingQueryDialect();
		sqlServerPagingQueryDialect.setFirstRow(0);
		sqlServerPagingQueryDialect.setPagesize(10);
		sqlServerPagingQueryDialect.setQuerySql(sql);
		return sqlServerPagingQueryDialect;
	}

}
