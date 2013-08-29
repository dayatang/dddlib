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
public class DB2PagingQueryDialectTest {
	
	/**
	 * 声明实例
	 */
	private DB2PagingQueryDialect db2PagingQueryDialect;

	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		db2PagingQueryDialect = this.createAndInitDB2PagingQueryDialect(0);
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		db2PagingQueryDialect = null;
	}

	/**
	 * 测试
	 */
	@Test
	public void testGeneratePagingQueryStatement() {
		String sqlExpected = "select * from DATA_SOURCES fetch first 10 rows only";
		String sql = db2PagingQueryDialect.generatePagingQueryStatement().getStatment();
		assertEquals(sqlExpected, sql);
		
		db2PagingQueryDialect = this.createAndInitDB2PagingQueryDialect(20);
		String sqlExpected2 = "select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( "
				+ "select * from DATA_SOURCES"
				+ " fetch first "
				+ 10
				+ " rows only ) as inner2_ ) as inner1_ where rownumber_ > "
				+ 20
				+ " order by rownumber_";
		String sql2 = db2PagingQueryDialect.generatePagingQueryStatement().getStatment();
		assertEquals(sqlExpected2, sql2);
	}
	
	/**
	 * 创建DB2PagingQueryDialect实例
	 * @param firstRow
	 * @return
	 */
	private DB2PagingQueryDialect createAndInitDB2PagingQueryDialect(int firstRow){
		SqlStatmentMode sqlStatmentMode = new SqlStatmentMode();
		sqlStatmentMode.setStatment("select * from DATA_SOURCES");
		DB2PagingQueryDialect db2PagingQueryDialect = new DB2PagingQueryDialect();
		db2PagingQueryDialect.setFirstRow(firstRow);
		db2PagingQueryDialect.setPagesize(10);
		db2PagingQueryDialect.setQuerySql(sqlStatmentMode);
		return db2PagingQueryDialect;
	}

}
