package org.openkoala.gqc.core.domain.utils;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.openkoala.gqc.core.domain.DynamicQueryCondition;
import org.openkoala.gqc.core.domain.FieldDetail;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.PreQueryCondition;
import org.openkoala.gqc.core.domain.QueryOperation;
import org.openkoala.gqc.core.exception.SystemDataSourceNotExistException;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 
 * @author lambo
 *
 */
@TransactionConfiguration(transactionManager = "transactionManager_gqc",defaultRollback = true) 
public class PagingQuerierTest extends KoalaBaseSpringTestCase{

	/**
	 * 声明实例
	 */
	private PagingQuerier pagingQuerier;

	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		pagingQuerier = this.createAndInitPagingQuerier(this.initSystemDataSourceAndDataSourceIdExist());
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		pagingQuerier = null;
	}

	/**
	 * 测试
	 */
	@Test
	public void testGenerateQuerySql() {
		String sql = pagingQuerier.generateQuerySql();
		assertEquals("select * from DATA_SOURCES limit 10 offset 0", sql);
		
		GeneralQuery generalQuery = new GeneralQuery("person");
		
		List<FieldDetail> fieldDetails = generalQuery.getFieldDetails();
		FieldDetail nameField = new FieldDetail("name");
		FieldDetail ageField = new FieldDetail("age");
		FieldDetail telField = new FieldDetail("tel");
		FieldDetail heightField = new FieldDetail("height");
		FieldDetail weightField = new FieldDetail("weight");
		
		fieldDetails.add(nameField);
		fieldDetails.add(ageField);
		fieldDetails.add(telField);
		fieldDetails.add(heightField);
		fieldDetails.add(weightField);
//		generalQuery.setFieldDetails(fieldDetails);
		
		List<PreQueryCondition> preQueryConditions = generalQuery.getPreQueryConditions();
		PreQueryCondition nameCondition = new PreQueryCondition("name");
		nameCondition.setValue("steven");
		nameCondition.setQueryOperation(QueryOperation.EQ);
		PreQueryCondition ageCondition = new PreQueryCondition("age");
		ageCondition.setValue("11");
		ageCondition.setQueryOperation(QueryOperation.GE);
		
		preQueryConditions.add(nameCondition);
		preQueryConditions.add(ageCondition);
//		generalQuery.setPreQueryConditions(preQueryConditions);
		
		List<DynamicQueryCondition> dynamicQueryConditions = generalQuery.getDynamicQueryConditions();
		DynamicQueryCondition telCondition = new DynamicQueryCondition("tel");
		telCondition.setQueryOperation(QueryOperation.LT);
		telCondition.setValue("1234567890");
		DynamicQueryCondition heightCondition = new DynamicQueryCondition("weight");
		heightCondition.setQueryOperation(QueryOperation.LE);
		heightCondition.setValue("120");
		
		dynamicQueryConditions.add(telCondition);
		dynamicQueryConditions.add(heightCondition);
//		generalQuery.setDynamicQueryConditions(dynamicQueryConditions);
		
//		DataSource dataSource = new DataSource();
//		dataSource.setConnectUrl("jdbc:mysql://localhost:3306/test");
//		dataSource.setJdbcDriver("com.mysql.jdbc.Driver");
//		dataSource.setUsername("root");
//		dataSource.setPassword("xmfang");
//		generalQuery.setDataSource(dataSource);
//
//		PagingQuerier pagingQuerier = new PagingQuerier(generalQuery.getQuerySql(), dataSource);
//		System.out.println(pagingQuerier.getTotalCount());
	}

	/**
	 * 测试
	 */
	@Test(expected = SystemDataSourceNotExistException.class)
	public void testSystemDataSourceNotExist(){
		pagingQuerier = this.createAndInitPagingQuerier(this.initSystemDataSourceAndDataSourceIdNotExist());
		pagingQuerier.generateQuerySql();
	}

	/**
	 * 测试
	 */
	@Test
	public void testGetTotalCount() {
		long total = pagingQuerier.getTotalCount();
		assertEquals(0, total);
	}

	/**
	 * 测试
	 */
	@Test
	public void testQuery() {
		List<Map<String, Object>> results = pagingQuerier.query();
		assertTrue(results.isEmpty());
	}

	/**
	 * 创建实例
	 * @param firstRow
	 * @return
	 */
	private PagingQuerier createAndInitPagingQuerier(DataSource dataSource){
		String querySql = "select * from DATA_SOURCES";
		PagingQuerier pagingQuerier = new PagingQuerier(querySql, dataSource);
		return pagingQuerier;
	}
	
	/**
	 * 初始化系统数据源，数据源id不存在
	 * @return
	 */
	private DataSource initSystemDataSourceAndDataSourceIdNotExist(){
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSource.setDataSourceId("aaabbb");
		
		return dataSource;
	}
	
	/**
	 * 初始化系统数据源，数据源id存在
	 * @return
	 */
	private DataSource initSystemDataSourceAndDataSourceIdExist(){
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSource.setDataSourceId("dataSource_gqc");
		
		return dataSource;
	}

}
=======
package org.openkoala.gqc.core.domain.utils;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.openkoala.gqc.core.domain.DynamicQueryCondition;
import org.openkoala.gqc.core.domain.FieldDetail;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.PreQueryCondition;
import org.openkoala.gqc.core.domain.QueryOperation;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 
 * @author lambo
 *
 */
@TransactionConfiguration(transactionManager = "transactionManager_gqc",defaultRollback = true) 
public class PagingQuerierTest extends KoalaBaseSpringTestCase{

	/**
	 * 声明实例
	 */
	private PagingQuerier pagingQuerier;

	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		pagingQuerier = this.createAndInitPagingQuerier(this.initSystemDataSourceAndDataSourceIdExist());
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		pagingQuerier = null;
	}

	/**
	 * 测试
	 */
	@Test
	public void testGenerateQuerySql() {
		String sql = pagingQuerier.generateQuerySql();
		assertEquals("select * from DATA_SOURCES limit 10 offset 0", sql);
		
		GeneralQuery generalQuery = new GeneralQuery("person");
		
		List<FieldDetail> fieldDetails = generalQuery.getFieldDetails();
		FieldDetail nameField = new FieldDetail("name");
		FieldDetail ageField = new FieldDetail("age");
		FieldDetail telField = new FieldDetail("tel");
		FieldDetail heightField = new FieldDetail("height");
		FieldDetail weightField = new FieldDetail("weight");
		
		fieldDetails.add(nameField);
		fieldDetails.add(ageField);
		fieldDetails.add(telField);
		fieldDetails.add(heightField);
		fieldDetails.add(weightField);
//		generalQuery.setFieldDetails(fieldDetails);
		
		List<PreQueryCondition> preQueryConditions = generalQuery.getPreQueryConditions();
		PreQueryCondition nameCondition = new PreQueryCondition("name");
		nameCondition.setValue("steven");
		nameCondition.setQueryOperation(QueryOperation.EQ);
		PreQueryCondition ageCondition = new PreQueryCondition("age");
		ageCondition.setValue("11");
		ageCondition.setQueryOperation(QueryOperation.GE);
		
		preQueryConditions.add(nameCondition);
		preQueryConditions.add(ageCondition);
//		generalQuery.setPreQueryConditions(preQueryConditions);
		
		List<DynamicQueryCondition> dynamicQueryConditions = generalQuery.getDynamicQueryConditions();
		DynamicQueryCondition telCondition = new DynamicQueryCondition("tel");
		telCondition.setQueryOperation(QueryOperation.LT);
		telCondition.setValue("1234567890");
		DynamicQueryCondition heightCondition = new DynamicQueryCondition("weight");
		heightCondition.setQueryOperation(QueryOperation.LE);
		heightCondition.setValue("120");
		
		dynamicQueryConditions.add(telCondition);
		dynamicQueryConditions.add(heightCondition);
//		generalQuery.setDynamicQueryConditions(dynamicQueryConditions);
		
//		DataSource dataSource = new DataSource();
//		dataSource.setConnectUrl("jdbc:mysql://localhost:3306/test");
//		dataSource.setJdbcDriver("com.mysql.jdbc.Driver");
//		dataSource.setUsername("root");
//		dataSource.setPassword("xmfang");
//		generalQuery.setDataSource(dataSource);
//
//		PagingQuerier pagingQuerier = new PagingQuerier(generalQuery.getQuerySql(), dataSource);
//		System.out.println(pagingQuerier.getTotalCount());
	}

	/**
	 * 测试
	 */
	@Test(expected = SystemDataSourceNotExistException.class)
	public void testSystemDataSourceNotExist(){
		pagingQuerier = this.createAndInitPagingQuerier(this.initSystemDataSourceAndDataSourceIdNotExist());
		pagingQuerier.generateQuerySql();
	}

	/**
	 * 测试
	 */
	@Test
	public void testGetTotalCount() {
		long total = pagingQuerier.getTotalCount();
		assertEquals(0, total);
	}

	/**
	 * 测试
	 */
	@Test
	public void testQuery() {
		List<Map<String, Object>> results = pagingQuerier.query();
		assertTrue(results.isEmpty());
	}

	/**
	 * 创建实例
	 * @param firstRow
	 * @return
	 */
	private PagingQuerier createAndInitPagingQuerier(DataSource dataSource){
		String querySql = "select * from DATA_SOURCES";
		PagingQuerier pagingQuerier = new PagingQuerier(querySql, dataSource);
		return pagingQuerier;
	}
	
	/**
	 * 初始化系统数据源，数据源id不存在
	 * @return
	 */
	private DataSource initSystemDataSourceAndDataSourceIdNotExist(){
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSource.setDataSourceId("aaabbb");
		
		return dataSource;
	}
	
	/**
	 * 初始化系统数据源，数据源id存在
	 * @return
	 */
	private DataSource initSystemDataSourceAndDataSourceIdExist(){
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSource.setDataSourceId("dataSource_gqc");
		
		return dataSource;
	}

}
