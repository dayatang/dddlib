package org.openkoala.gqc.core.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.querychannel.support.Page;

/**
 * 
 * @author lambo
 *
 */
@TransactionConfiguration(transactionManager = "transactionManager_gqc",defaultRollback = true)
public class GeneralQueryTest extends KoalaBaseSpringTestCase{

	/**
	 * 实例
	 */
	private GeneralQuery generalQuery;
	
	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		generalQuery = createAndInitGeneralQuery();
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		generalQuery = null;
	}
	
	/**
	 * 测试
	 */
	@Test
	public void testQuery() {
		List<Map<String, Object>> list = generalQuery.query();
		assertTrue("查询结果总记录应该为空！", list.isEmpty());
		
//		generalQuery.getDataSource().save();
//		generalQuery.save();
		
//		list = generalQuery.query();
//		assertEquals("查询结果总记录应该为1！", 0, list.size());
//		
//		generalQuery.remove();
//		list = generalQuery.query();
//		assertNull("查询结果总记录应该为空！", list);
//		
//		generalQuery.getDataSource().remove();
	}

	/**
	 * 测试
	 */
	@Test
	public void testPagingQuery() {
		List<Map<String, Object>> list = generalQuery.pagingQuery(1, 10);
		assertTrue("查询结果总记录应该为空！", list.isEmpty());
	}

	/**
	 * 测试
	 */
	@Test
	public void testPagingQueryPage() {
		Page<Map<String, Object>> page = generalQuery.pagingQueryPage(1, 10);
		assertTrue(page.getResult().isEmpty());
	}

	/**
	 * 测试
	 */
	@Test
	public void testFindByQueryName() {
		GeneralQuery generalQueryBean = GeneralQuery.findByQueryName("test");
		assertNull("查询结果应该为空！", generalQueryBean);
		
		generalQuery.getDataSource().save();
		
		generalQuery.save();
		GeneralQuery generalQueryBean2 = GeneralQuery.findByQueryName("test");
		assertEquals("查询器名称应该为'test'！", "test", generalQueryBean2.getQueryName());
		
		generalQuery.remove();
		GeneralQuery generalQueryBean3 = GeneralQuery.findByQueryName("test");
		assertNull("查询结果应该为空！", generalQueryBean3);
		
		generalQuery.getDataSource().remove();
	}

	/**
	 * 测试
	 */
	@Test
	public void testGetQuerySql() {
		String jpql = generalQuery.getQuerySql().getStatment();
		assertNotNull("sql不应该为空！", jpql);
	}

	/**
	 * 测试
	 */
	@Test
	public void testGetVisiblePreQueryConditions() {
		List<PreQueryCondition> results = generalQuery.getVisiblePreQueryConditions();
		assertEquals("静态查询条件结果集应该为0！", 0, results.size());
	}

	/**
	 * 测试
	 */
	@Test
	public void testGetDynamicQueryConditionByFieldName() {
		DynamicQueryCondition dynamicQueryCondition = generalQuery.getDynamicQueryConditionByFieldName("abc");
		assertNull("动态查询条件应该为空！", dynamicQueryCondition);
	}

	/**
	 * 测试
	 */
	public void testIsNew() {
		Boolean isNew = generalQuery.isNew();
		assertTrue("应该是未入库的！",isNew);
	}

	/**
	 * 测试
	 */
	@Test
	public void testExisted() {
		Boolean existed = generalQuery.existed();
		assertTrue("应该是未入库的！",!existed);
	}

	/**
	 * 测试
	 */
	@Test
	public void testNotExisted() {
		Boolean notExisted = generalQuery.notExisted();
		assertTrue("应该是未入库的！",notExisted);
	}

	/**
	 * 测试
	 */
	@Test
	public void testSave() {
		this.save();
	}
	
	/**
	 * 测试
	 */
	@Test
	public void testRemove() {
		this.save();
		this.remove();
	}

	/**
	 * 测试
	 */
	@Test
	public void testGet() {
		this.save();
		GeneralQuery generalQueryBean = GeneralQuery.get(GeneralQuery.class, generalQuery.getId());
		assertEquals(generalQuery.getId(), generalQueryBean.getId());
	}

	/**
	 * 测试
	 */
	@Test
	public void testGetUnmodified() {
		this.save();
		
		GeneralQuery generalQueryBean = GeneralQuery.getUnmodified(GeneralQuery.class, generalQuery);
		assertNotNull(generalQueryBean);
	}

	/**
	 * 测试
	 */
	@Test
	public void testLoad() {
		this.save();
		
		GeneralQuery generalQueryBean = GeneralQuery.load(GeneralQuery.class, generalQuery.getId());

		assertEquals(generalQuery.getId(), generalQueryBean.getId());
	}

	/**
	 * 测试
	 */
	@Test
	public void testFindAll() {
		this.save();
		
		List<GeneralQuery> list = GeneralQuery.findAll(GeneralQuery.class);
		
		assertEquals(1,list.size());
	}
	
	/**
	 * 保存
	 */
	private void save(){
		generalQuery.getDataSource().save();
		generalQuery.save();
		
//		Page<Map<String, Object>> page = generalQuery.pagingQueryPage(1, 10);
//		assertEquals("查询结果总记录应该1！", 1, page.getResult().size());
		GeneralQuery generalQueryBean = GeneralQuery.findByQueryName("test");
		assertNotNull(generalQueryBean);
		
	}
	
	/**
	 * 删除
	 */
	private void remove(){
		generalQuery.remove();
		
//		Page<Map<String, Object>> page = generalQuery.pagingQueryPage(1, 10);
//		assertNull("查询结果总记录应该为空！", page.getResult());
		
		GeneralQuery generalQueryBean = GeneralQuery.findByQueryName("test");
		assertNull(generalQueryBean);
		
//		generalQuery.getDataSource().remove();
	}
	
	/**
	 * 创建GeneralQuery实例
	 * @return
	 */
	private GeneralQuery createAndInitGeneralQuery(){
		GeneralQuery generalQuery = new GeneralQuery();
		
		generalQuery.setDataSource(this.createAndInitDataSource());
		generalQuery.setQueryName("test");
		generalQuery.setTableName("GENERAL_QUERYS");
		generalQuery.setDescription("test 描述");
		generalQuery.setCreateDate(new Date());
		generalQuery.setPreQueryConditions(this.createPreQueryConditions());
		generalQuery.setFieldDetails(this.createFieldDetails());
		
		return generalQuery;
	}
	
	/**
	 * 创建DataSource实例
	 * @return
	 */
	private DataSource createAndInitDataSource(){
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSource.setDataSourceId("dataSource_gqc");
		return dataSource;
	}
	
	/**
	 * 创建PreQueryCondition集合
	 * @return
	 */
	private List<PreQueryCondition> createPreQueryConditions(){
		List<PreQueryCondition> preQueryConditions = new ArrayList<PreQueryCondition>();
		
		PreQueryCondition preQueryCondition = new PreQueryCondition();
		preQueryCondition.setFieldName("QUERY_NAME");
		preQueryCondition.setQueryOperation(QueryOperation.EQ);
		preQueryCondition.setValue("test");
		
		preQueryConditions.add(preQueryCondition);
		
		return preQueryConditions;
	}
	
	/**
	 * 创建FieldDetail集合
	 * @return
	 */
	private List<FieldDetail> createFieldDetails(){
		List<FieldDetail> fieldDetails = new ArrayList<FieldDetail>();
		
		FieldDetail fieldDetail = new FieldDetail();
		fieldDetail.setFieldName("DATA_SOURCE_ID");
		fieldDetail.setLabel("数据源");
		
		fieldDetails.add(fieldDetail);
		
		return fieldDetails;
	}

}
