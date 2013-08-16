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
public class H2PagingQueryDialectTest {

	/**
	 * 声明实例
	 */
	private H2PagingQueryDialect h2PagingQueryDialect;

	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		h2PagingQueryDialect = this.createAndInitH2PagingQueryDialect();
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		h2PagingQueryDialect = null;
	}

	/**
	 * 测试
	 */
	@Test
	public void testGeneratePagingQueryStatement() {
		String sqlExpected = "select * from DATA_SOURCES limit 10 offset 0";
		String sql = h2PagingQueryDialect.generatePagingQueryStatement();
		assertEquals(sqlExpected, sql);
	}

	/**
	 * 创建H2PagingQueryDialect实例
	 * @param firstRow
	 * @return
	 */
	private H2PagingQueryDialect createAndInitH2PagingQueryDialect(){
		H2PagingQueryDialect h2PagingQueryDialect = new H2PagingQueryDialect();
		h2PagingQueryDialect.setFirstRow(0);
		h2PagingQueryDialect.setPagesize(10);
		h2PagingQueryDialect.setQuerySql("select * from DATA_SOURCES");
		return h2PagingQueryDialect;
	}

}
