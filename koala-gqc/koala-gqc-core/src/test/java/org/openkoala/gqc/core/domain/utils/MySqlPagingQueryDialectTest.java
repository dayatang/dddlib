package org.openkoala.gqc.core.domain.utils;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author lambo
 *
 */
public class MySqlPagingQueryDialectTest {

	/**
	 * 声明实例
	 */
	private MySqlPagingQueryDialect mySqlPagingQueryDialect;

	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		mySqlPagingQueryDialect = this.createAndInitMySqlPagingQueryDialect();
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		mySqlPagingQueryDialect = null;
	}

	/**
	 * 测试
	 */
	@Test
	public void testGeneratePagingQueryStatement() {
		String sqlExpected = "select * from DATA_SOURCES LIMIT 0,10";
		String sql = mySqlPagingQueryDialect.generatePagingQueryStatement().getStatment();
		assertEquals(sqlExpected, sql);
	}

	/**
	 * 创建MySqlPagingQueryDialect实例
	 * @param firstRow
	 * @return
	 */
	private MySqlPagingQueryDialect createAndInitMySqlPagingQueryDialect(){
		SqlStatmentMode sqlStatmentMode = new SqlStatmentMode();
		sqlStatmentMode.setStatment("select * from DATA_SOURCES");
		MySqlPagingQueryDialect mySqlPagingQueryDialect = new MySqlPagingQueryDialect();
		mySqlPagingQueryDialect.setFirstRow(0);
		mySqlPagingQueryDialect.setPagesize(10);
		mySqlPagingQueryDialect.setQuerySql(sqlStatmentMode);
		return mySqlPagingQueryDialect;
	}

}
