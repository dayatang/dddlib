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
public class OraclePagingQueryDialectTest {

	/**
	 * 声明实例
	 */
	private OraclePagingQueryDialect oraclePagingQueryDialect;

	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		oraclePagingQueryDialect = this.createAndInitOraclePagingQueryDialect();
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		oraclePagingQueryDialect = null;
	}

	/**
	 * 测试
	 */
	@Test
	public void testGeneratePagingQueryStatement() {
		String sqlExpected = "select * from ( select row_.*, rownum rownum_ from ( select * from DATA_SOURCES ) row_ ) where rownum_ <= 10 and rownum_ > 0";
		String sql = oraclePagingQueryDialect.generatePagingQueryStatement().getStatment();
		assertEquals(sqlExpected, sql);
	}

	/**
	 * 创建OraclePagingQueryDialect实例
	 * @param firstRow
	 * @return
	 */
	private OraclePagingQueryDialect createAndInitOraclePagingQueryDialect(){
		SqlStatmentMode sqlStatmentMode = new SqlStatmentMode();
		sqlStatmentMode.setStatment("select * from DATA_SOURCES");
		OraclePagingQueryDialect oraclePagingQueryDialect = new OraclePagingQueryDialect();
		oraclePagingQueryDialect.setFirstRow(0);
		oraclePagingQueryDialect.setPagesize(10);
		oraclePagingQueryDialect.setQuerySql(sqlStatmentMode);
		return oraclePagingQueryDialect;
	}

}
