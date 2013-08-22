package org.openkoala.gqc.application.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.openkoala.gqc.vo.DataSourceVO;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.AssertThrows;

import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ InstanceFactory.class })
public class DataSourceApplicationImplTest {
	/**
	 * 创建实例
	 */
	private DataSourceApplicationImpl instance = new DataSourceApplicationImpl();
	
	@Mock
	private QueryChannelService queryChannel;
	
	@Mock
	private EntityRepository repository;
	
	/**
	 * 创建实例
	 */
	private DataSource dataSource;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		DataSource.setRepository(repository);
		instance.setQueryChannelService(queryChannel);
		//测试静态方法
		PowerMockito.mockStatic(InstanceFactory.class);
	}

	@After
	public void tearDown() throws Exception {
		DataSource.setRepository(null);
	}

	@Test
	public void testGetDataSource() {
		dataSource = createDataSource();
		
//		when(queryChannel.querySingleResult("sql",new Object[]{dataSource.getId()})).thenReturn(dataSource);
		
		when(repository.get(DataSource.class, dataSource.getId())).thenReturn(dataSource);
		
		DataSourceVO dataSourceVO = null;
		try {
			dataSourceVO = turnToDataSourceVO(dataSource);
		} catch (Exception e) {
			fail("实体转换成VO时失败！");
		}
		
		assertThat(instance.getDataSourceVoById(2L), is(dataSourceVO));
	}

	@Test
	public void testGetDataSourceVoByDataSourceId() {
		String jpql = " select _dataSource from DataSource _dataSource  where _dataSource.dataSourceId = ? ";
		dataSource = createDataSource();
		//返回非空的分支
		when(queryChannel.querySingleResult(jpql, new Object[] { dataSource.getDataSourceId() })).thenReturn(dataSource);
		DataSourceVO dataSourceVO = null;
		try {
			dataSourceVO = turnToDataSourceVO(dataSource);
		} catch (Exception e) {
			fail("实体转换成VO时失败！");
		}
		assertThat(instance.getDataSourceVoByDataSourceId(dataSource.getDataSourceId()), is(dataSourceVO));

		//返回空的分支
		when(queryChannel.querySingleResult(jpql, new Object[] { "notInDb" })).thenReturn(null);
		assertThat(instance.getDataSourceVoByDataSourceId( "notInDb" ), not(dataSourceVO));
	}

	@Test
	public void testSaveDataSource() {
		String jpql = " select _dataSource from DataSource _dataSource  where _dataSource.dataSourceId = ? ";
		dataSource = createDataSource();
		/**返回数据源ID已存在的分支*/
		when(queryChannel.querySingleResult(jpql, new Object[] { dataSource.getDataSourceId() })).thenReturn(dataSource);
		DataSourceVO dataSourceVO = null;
		try {
			dataSourceVO = turnToDataSourceVO(dataSource);
		} catch (Exception e) {
			fail("实体转换成VO时失败！");
		}
		assertThat(instance.saveDataSource(dataSourceVO), is("该数据源ID已存在"));
		
		/**返回数据源ID不存在的分支*/
		//初始化系统数据源，数据源id不存在
		dataSource = initSystemDataSourceAndDataSourceIdNotExist();
		try {
			dataSourceVO = turnToDataSourceVO(dataSource);
		} catch (Exception e) {
			fail("实体转换成VO时失败！");
		}
		PowerMockito.when(InstanceFactory.getInstance(javax.sql.DataSource.class, dataSource.getDataSourceId())).thenThrow(new RuntimeException("该系统数据源不存在！"));
		when(queryChannel.querySingleResult(jpql, new Object[] { "dataSourceId1" })).thenReturn(null);
		assertThat(instance.saveDataSource(dataSourceVO), StringContains.containsString("系统数据源不存在"));
		
		//初始化系统数据源，数据源id存在
		dataSource = initSystemDataSourceAndDataSourceIdExist();
		try {
			dataSourceVO = turnToDataSourceVO(dataSource);
		} catch (Exception e) {
			fail("实体转换成VO时失败！");
		}
		javax.sql.DataSource ds = new BasicDataSource();
		PowerMockito.when(InstanceFactory.getInstance(javax.sql.DataSource.class, dataSource.getDataSourceId())).thenReturn(ds);
		when(queryChannel.querySingleResult(jpql, new Object[] { dataSource.getDataSourceId() })).thenReturn(null);
		assertThat(instance.saveDataSource(dataSourceVO), StringContains.containsString("获取系统数据源失败"));
		
		//初始化自定义数据源，不能连接
		dataSource = initCustomDataSourceCannotConnect();
		when(repository.save(dataSource)).thenReturn(dataSource);
		try {
			dataSourceVO = turnToDataSourceVO(dataSource);
		} catch (Exception e) {
			fail("实体转换成VO时失败！");
		}
		assertThat(instance.saveDataSource(dataSourceVO),IsNull.nullValue());
		
		//初始化自定义数据源，能连接
		dataSource = initCustomDataSourceCanConnect();
		when(repository.save(dataSource)).thenReturn(dataSource);
		try {
			dataSourceVO = turnToDataSourceVO(dataSource);
		} catch (Exception e) {
			fail("实体转换成VO时失败！");
		}
		assertThat(instance.saveDataSource(dataSourceVO),IsNull.nullValue());
	}

	@Test
	public void testUpdateDataSource() {
		dataSource = createDataSource();
		when(repository.get(DataSource.class,dataSource.getId())).thenReturn(dataSource);
		
		DataSourceVO dataSourceVO = null;
		try {
			dataSourceVO = turnToDataSourceVO(dataSource);
		} catch (Exception e) {
			fail("实体转换成VO时失败！");
		}
		dataSource = createDataSourceToUpdate();
		//when(instance.updateDataSource(dataSourceVO);)
		
	}

	@Test
	public void testRemoveDataSource() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveDataSources() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllDataSource() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllColumn() {
		fail("Not yet implemented");
	}

	@Test
	public void testPageQueryDataSource() {
		fail("Not yet implemented");
	}

	@Test
	public void testChechDataSourceCanConnect() {
		fail("Not yet implemented");
	}

	@Test
	public void testTestConnection() {
		fail("Not yet implemented");
	}
	
	/**
	 * 实体转换成vo
	 * @param dataSource
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private DataSourceVO turnToDataSourceVO(DataSource dataSource) throws Exception {
		DataSourceVO dataSourceVO = new DataSourceVO();
		BeanUtils.copyProperties(dataSourceVO, dataSource);
		dataSourceVO.setDataSourceTypeDesc(dataSourceVO.getDataSourceType().getDescription());
		return dataSourceVO;
	}
	
	/**
	 * 模拟一个实体
	 * @return
	 */
	private DataSource createDataSource(){
		DataSource dataSource = new DataSource();
		dataSource.setId(2L);
		dataSource.setDataSourceId("dataSourceId1");
		dataSource.setDataSourceType(DataSourceType.CUSTOM_DATA_SOURCE);
		dataSource.setJdbcDriver("driver1");
		dataSource.setConnectUrl("connectUrl1");
		dataSource.setUsername("username1");
		dataSource.setPassword("password1");
		return dataSource;
	}
	
	/**
	 * 模拟一个实体
	 * @return
	 */
	private DataSource createDataSourceToUpdate(){
		DataSource dataSource = new DataSource();
		dataSource.setId(2L);
		dataSource.setDataSourceId("dataSourceId2");
		dataSource.setDataSourceType(DataSourceType.CUSTOM_DATA_SOURCE);
		dataSource.setJdbcDriver("driver2");
		dataSource.setConnectUrl("connectUrl2");
		dataSource.setUsername("username2");
		dataSource.setPassword("password2");
		return dataSource;
	}
	
	/**
	 * 初始化系统数据源，数据源id不存在
	 * @return
	 */
	private DataSource initSystemDataSourceAndDataSourceIdNotExist() {
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSource.setDataSourceId("aaabbb");
		
		return dataSource;
	}
	
	/**
	 * 初始化系统数据源，数据源id存在
	 * @return
	 */
	private DataSource initSystemDataSourceAndDataSourceIdExist() {
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSource.setDataSourceId("dataSource_gqc");
		
		return dataSource;
	}
	
	/**
	 * 初始化自定义数据源，不能连接
	 * @return
	 */
	private DataSource initCustomDataSourceCannotConnect() {
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.CUSTOM_DATA_SOURCE);
		dataSource.setJdbcDriver("a");
		dataSource.setConnectUrl("a");
		dataSource.setUsername("a");
		dataSource.setPassword("a");

		return dataSource;
	}
	
	/**
	 * 初始化自定义数据源，可以连接
	 * @return
	 */
	private DataSource initCustomDataSourceCanConnect() {
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.CUSTOM_DATA_SOURCE);
		dataSource.setJdbcDriver("org.h2.Driver");
		dataSource.setConnectUrl("jdbc:h2:~/db/test");
		dataSource.setUsername("sa");
		dataSource.setPassword("");

		return dataSource;
	}

}
