package org.openkoala.gqc.core.domain.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author lambo
 *
 */
public class PagingQueryDialectResolverTest {
	
	/**
	 * 声明
	 */
	private PagingQueryDialect bean;
	
	/**
	 * 声明
	 */
	private List<String> databaseList;

	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		databaseList = this.createAndInitDatabaseList();
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		databaseList = null;
	}

	/**
	 * 测试
	 */
	@Test
	public void testGetPagingQuerierInstance() {
		bean = PagingQueryDialectResolver.getPagingQuerierInstance("aa");
		assertNull(bean);
		
		for(String database : databaseList){
			bean = PagingQueryDialectResolver.getPagingQuerierInstance(database);
			assertNotNull("数据库"+database+"方言实例不应该为空！",bean);
		}
	}

	/**
	 * 创建实例
	 * @param firstRow
	 * @return
	 */
	private List<String> createAndInitDatabaseList(){
		List<String> list = new ArrayList<String>();
		list.add("H2");
		list.add("MySQL");
		list.add("Oracle");
		list.add("Microsoft SQL Server");
		list.add("DB2/");
		return list;
	}

}
