package org.openkoala.gqc.application.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.openkoala.gqc.core.domain.FieldDetail;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.PreQueryCondition;
import org.openkoala.gqc.core.domain.QueryOperation;
import org.openkoala.gqc.infra.util.DatabaseUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;

@PrepareForTest({ AbstractEntity.class})
public class GqcApplicationImplTest {
	
	private GqcApplicationImpl instance = new GqcApplicationImpl();
	
	@Mock
	private QueryChannelService queryChannel;

	@Mock
	private EntityRepository repository;
	
	@Mock
	private EntityManager entityManager;
	
	private GeneralQuery generalQuery;
	
	private int currentPage = 1;
	private int pageSize = 10;
	
	private Long idExist = 1L;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		GeneralQuery.setRepository(repository);
		
		//测试静态方法
		PowerMockito.mockStatic(AbstractEntity.class);
		generalQuery = new GeneralQuery();
	}

	@After
	public void tearDown() throws Exception {
		DataSource.setRepository(null);
		generalQuery = null;
	}

	@Test
	public void testGetEntity() {
//		this.createAndInitGeneralQuery();
//		PowerMockito.when(AbstractEntity.get(GeneralQuery.class,idExist)).thenReturn(generalQuery);
//		assertEquals(idExist, instance.getEntity(GeneralQuery.class, idExist).getId());
	}
	
	/*@Test
	public void testSaveEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testPagingQueryGeneralQueries() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetById() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveEntities() {
		fail("Not yet implemented");
	}

	@Test
	public void testPagingQueryGeneralQueriesByQueryName() {
		fail("Not yet implemented");
	}*/
	
	/**
	 * 创建GeneralQuery实例
	 * @return
	 */
	private void createAndInitGeneralQuery(){
		generalQuery.setDataSource(this.createAndInitDataSource());
		generalQuery.setQueryName("test");
		generalQuery.setTableName("GENERAL_QUERYS");
		generalQuery.setDescription("test 描述");
		generalQuery.setCreateDate(new Date());
		generalQuery.setPreQueryConditions(this.createPreQueryConditions());
		generalQuery.setFieldDetails(this.createFieldDetails());
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
