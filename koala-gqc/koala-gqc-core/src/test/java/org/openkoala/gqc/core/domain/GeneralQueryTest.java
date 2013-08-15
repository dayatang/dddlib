package org.openkoala.gqc.core.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.domain.EntityRepository;
import com.dayatang.querychannel.support.Page;

@TransactionConfiguration(transactionManager = "transactionManager_gqc",defaultRollback = true)
public class GeneralQueryTest extends KoalaBaseSpringTestCase{

//	@Mock
//	private static EntityRepository repository;
//	
	private static GeneralQuery generalQuery;
	
	@Before
	public void setUp() throws Exception {
		generalQuery = createAndInitGeneralQuery();
//		MockitoAnnotations.initMocks(this);
//		GeneralQuery.setRepository(repository);
	}

	@After
	public void tearDown() throws Exception {
//		GeneralQuery.setRepository(null);
		generalQuery = null;
	}
	
	@Test
	public void testQuery() {
		List<Map<String, Object>> list = generalQuery.query();
		assertNull("查询结果总记录应该为空！", list);
		
//		generalQuery.getDataSource().save();
//		
//		generalQuery.save();
//		list = generalQuery.query();
//		assertEquals("查询结果总记录应该为1！", 1, list.size());
//		
//		generalQuery.remove();
//		list = generalQuery.query();
//		assertNull("查询结果总记录应该为空！", list);
//		
//		generalQuery.getDataSource().remove();
	}

	@Test
	public void testPagingQuery() {
		List<Map<String, Object>> list = generalQuery.pagingQuery(1, 10);
		assertNull("查询结果总记录应该为空！", list);
	}

	@Test
	public void testPagingQueryPage() {
		Page<Map<String, Object>> page = generalQuery.pagingQueryPage(1, 10);
		assertNull("查询结果总记录应该为空！", page.getResult());
	}

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

	@Test
	public void testGetQuerySql() {
		String jpql = generalQuery.getQuerySql();
		assertNotNull("sql不应该为空！", jpql);
	}

	@Test
	public void testGetVisiblePreQueryConditions() {
		List<PreQueryCondition> results = generalQuery.getVisiblePreQueryConditions();
		assertEquals("静态查询条件结果集应该为0！", 0, results.size());
	}

	@Test
	public void testGetDynamicQueryConditionByFieldName() {
		DynamicQueryCondition dynamicQueryCondition = generalQuery.getDynamicQueryConditionByFieldName("abc");
		assertNull("动态查询条件应该为空！", dynamicQueryCondition);
	}

	@Test
	public void testIsNew() {
		Boolean isNew = generalQuery.isNew();
		assertTrue("应该是未入库的！",isNew);
	}

	@Test
	public void testExisted() {
		Boolean existed = generalQuery.existed();
		assertTrue("应该是未入库的！",!existed);
	}

	@Test
	public void testNotExisted() {
		Boolean notExisted = generalQuery.notExisted();
		assertTrue("应该是未入库的！",notExisted);
	}

	@Test
	public void testSave() {
		this.save();
	}
	
	@Test
	public void testRemove() {
		this.save();
		this.remove();
	}

	@Test
	public void testGet() {
		this.save();
		GeneralQuery generalQueryBean = GeneralQuery.get(GeneralQuery.class, generalQuery.getId());
		assertEquals(generalQuery.getId(), generalQueryBean.getId());
	}

	@Test
	public void testGetUnmodified() {
		this.save();
		
		GeneralQuery generalQueryBean = GeneralQuery.getUnmodified(GeneralQuery.class, generalQuery);
		assertNotNull(generalQueryBean);
	}

	@Test
	public void testLoad() {
		this.save();
		
		GeneralQuery generalQueryBean = GeneralQuery.load(GeneralQuery.class, generalQuery.getId());

		assertEquals(generalQuery.getId(), generalQueryBean.getId());
	}

	@Test
	public void testFindAll() {
		this.save();
		
		List<GeneralQuery> list = GeneralQuery.findAll(GeneralQuery.class);
		
		assertEquals(1,list.size());
	}
	
	private void save(){
		generalQuery.getDataSource().save();
		generalQuery.save();
		
//		Page<Map<String, Object>> page = generalQuery.pagingQueryPage(1, 10);
//		assertEquals("查询结果总记录应该1！", 1, page.getResult().size());
		GeneralQuery generalQueryBean = GeneralQuery.findByQueryName("test");
		assertNotNull(generalQueryBean);
		
	}
	
	private void remove(){
		generalQuery.remove();
		
//		Page<Map<String, Object>> page = generalQuery.pagingQueryPage(1, 10);
//		assertNull("查询结果总记录应该为空！", page.getResult());
		
		GeneralQuery generalQueryBean = GeneralQuery.findByQueryName("test");
		assertNull(generalQueryBean);
		
//		generalQuery.getDataSource().remove();
	}
	
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
	
	private DataSource createAndInitDataSource(){
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSource.setDataSourceId("dataSource_gqc");
		return dataSource;
	}
	
	private List<PreQueryCondition> createPreQueryConditions(){
		List<PreQueryCondition> preQueryConditions = new ArrayList<PreQueryCondition>();
		
		PreQueryCondition preQueryCondition = new PreQueryCondition();
		preQueryCondition.setFieldName("dataSource");
		preQueryCondition.setQueryOperation(QueryOperation.EQ);
		preQueryCondition.setValue("_none");
		
		preQueryConditions.add(preQueryCondition);
		
		return preQueryConditions;
	}
	
	private List<FieldDetail> createFieldDetails(){
		List<FieldDetail> fieldDetails = new ArrayList<FieldDetail>();
		
		FieldDetail fieldDetail = new FieldDetail();
		fieldDetail.setFieldName("dataSource");
		fieldDetail.setLabel("数据源");
		
		fieldDetails.add(fieldDetail);
		
		return fieldDetails;
	}

}
