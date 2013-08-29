package org.openkoala.gqc.application.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.beanutils.BeanUtils;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;
import org.junit.runner.RunWith;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.openkoala.gqc.vo.DataSourceVO;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dayatang.IocException;
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
	
	@Mock
	private Connection conn;
	
	private ProxoolDataSource ds;
	
	/**
	 * 创建实例
	 */
	private DataSource dataSource;
	
//	private long idExist = 2L;
//	private long idNotExist = 100L;
//	
//	private String dataSourceIdNotExist = "notInDb";
	
	private final String jpqlForDataSourceId = " select _dataSource from DataSource _dataSource  where _dataSource.dataSourceId = ? ";
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		DataSource.setRepository(repository);
		instance.setQueryChannelService(queryChannel);
		//测试静态方法
		PowerMockito.mockStatic(InstanceFactory.class);
		dataSource = new DataSource();
	}

	@After
	public void tearDown() throws Exception {
		DataSource.setRepository(null);
		dataSource = null;
	}

	@Test
	public void testGetDataSourceById() {
		this.mockOneDbRecordExistOfSystemDataSourceById();
		this.assertResultEqualsById();
		
		this.mockOneDbRecordExistOfCustomDataSourceById();
		this.assertResultEqualsById();
		
		this.mockOneDbRecordNotExistOfSystemDataSourceById();
		this.assertResultNotEqualsById();
		
		this.mockOneDbRecordNotExistOfCustomDataSourceById();
		this.assertResultNotEqualsById();
	}
	
	@Test
	public void testGetDataSourceVoByDataSourceId() {
		this.mockOneDbRecordExistOfSystemDataSourceByDataSourceId();
		this.assertResultEqualsByDataSourceId();

		this.mockOneDbRecordExistOfCustomDataSourceByDataSourceId();
		this.assertResultEqualsByDataSourceId();

		this.mockOneDbRecordNotExistOfSystemDataSourceByDataSourceId();
		this.assertResultNotEqualsByDataSourceId();

		this.mockOneDbRecordNotExistOfCustomDataSourceByDataSourceId();
		this.assertResultNotEqualsByDataSourceId();
	}

	@Test
	public void testSaveDataSource() {
		this.mockOneDbRecordExistOfSystemDataSourceByDataSourceId();
		DataSourceVO dataSourceVO = turnToDataSourceVO();
		assertThat(instance.saveDataSource(dataSourceVO), is("该数据源ID已存在"));
		
		this.mockOneDbRecordExistOfCustomDataSourceByDataSourceId();
		dataSourceVO = turnToDataSourceVO();
		assertThat(instance.saveDataSource(dataSourceVO), is("该数据源ID已存在"));
		
		this.mockOneDbRecordNotExistOfSystemDataSourceByDataSourceId();
		dataSourceVO = turnToDataSourceVO();
		PowerMockito.when(InstanceFactory.getInstance(javax.sql.DataSource.class, dataSource.getDataSourceId())).thenThrow(new IocException());
		assertThat(instance.saveDataSource(dataSourceVO), StringContains.containsString("系统数据源不存在"));
		
		this.initSystemDataSourceAndDataSourceIdNotInDbButInSystem();
		dataSourceVO = turnToDataSourceVO();
		this.initProxoolDataSource();
		PowerMockito.when(InstanceFactory.getInstance(javax.sql.DataSource.class, 
				dataSource.getDataSourceId())).thenReturn(ds);
		assertNull(instance.saveDataSource(dataSourceVO));
		try {
			ds.getConnection().close();
		} catch (SQLException e) {
			fail("连接关闭失败！");
		}
		
		this.initCustomDataSourceCannotConnect();
		dataSourceVO = turnToDataSourceVO();
		when(repository.save(dataSource)).thenReturn(dataSource);
		assertThat(instance.saveDataSource(dataSourceVO),IsNull.nullValue());
	}
	
	@Test
	public void testUpdateDataSource() {
		DataSourceVO dataSourceVO = this.createDataSourceVO();
		
		this.initSystemDataSourceAndDataSourceIdExist();
		when(repository.get(DataSource.class,dataSourceVO.getId())).thenReturn(dataSource);
		
		instance.updateDataSource(dataSourceVO);
		assertTrue(true);
	}

	@Test
	public void testRemoveDataSource() {
		//fail("Not yet implemented");
	}

	@Test
	public void testRemoveDataSources() {
		//fail("Not yet implemented");
	}

	@Test
	public void testFindAllDataSource() {
		//fail("Not yet implemented");
	}

	@Test
	public void testFindAllTable() {
		//fail("Not yet implemented");
	}

	@Test
	public void testFindAllColumn() {
		//fail("Not yet implemented");
	}

	@Test
	public void testPageQueryDataSource() {
		//fail("Not yet implemented");
	}

	@Test
	public void testChechDataSourceCanConnect() {
		//fail("Not yet implemented");
	}

	@Test
	public void testTestConnection() {
		//fail("Not yet implemented");
	}
	
	/**
	 * 实体转换成vo
	 * @param
	 * @return
	 */
	private DataSourceVO turnToDataSourceVO(){
		try {
			DataSourceVO dataSourceVO = new DataSourceVO();
			BeanUtils.copyProperties(dataSourceVO, dataSource);
			dataSourceVO.setDataSourceTypeDesc(dataSourceVO.getDataSourceType().getDescription());
			return dataSourceVO;
		} catch (Exception e) {
			fail("实体转换成VO时失败！");
			return null;
		}
	}
	
	/**
	 * 创建VO对象
	 * @return
	 */
	private DataSourceVO createDataSourceVO(){
		DataSourceVO dataSourceVO = new DataSourceVO();
		dataSourceVO.setId(1L);
		dataSourceVO.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSourceVO.setJdbcDriver("a");
		dataSourceVO.setConnectUrl("a");
		dataSourceVO.setUsername("a");
		dataSourceVO.setPassword("a");
		return dataSourceVO;
	}
	
	/**
	 * 初始化系统数据源，数据源id不存在于数据库
	 * @return
	 */
	private void initSystemDataSourceAndDataSourceIdNotExist() {
		dataSource.setId(1L);
		dataSource.setDataSourceId("aaa");
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSource.setJdbcDriver("aaa");
		dataSource.setConnectUrl("aaa");
		dataSource.setUsername("aaa");
		dataSource.setPassword("aaa");
	}
	
	/**
	 * 初始化系统数据源，数据源id存在于数据库
	 * @return
	 */
	private void initSystemDataSourceAndDataSourceIdNotInDbButInSystem() {
		dataSource.setDataSourceId("dataSource_gqc");
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
	}
	
	/**
	 * 初始化系统数据源，数据源id存在于数据库
	 * @return
	 */
	private void initSystemDataSourceAndDataSourceIdExist() {
		dataSource.setId(2L);
		dataSource.setDataSourceId("bbb");
		dataSource.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
		dataSource.setJdbcDriver("bbb");
		dataSource.setConnectUrl("bbb");
		dataSource.setUsername("bbb");
		dataSource.setPassword("bbb");
	}
	
	/**
	 * 初始化自定义数据源，不能连接
	 * @return
	 */
	private void initCustomDataSourceCannotConnect() {
		dataSource.setId(3L);
		dataSource.setDataSourceId("ccc");
		dataSource.setDataSourceType(DataSourceType.CUSTOM_DATA_SOURCE);
		dataSource.setJdbcDriver("ccc");
		dataSource.setConnectUrl("ccc");
		dataSource.setUsername("ccc");
		dataSource.setPassword("ccc");
	}
	
	/**
	 * 初始化自定义数据源，可以连接
	 * @return
	 */
	private void initCustomDataSourceCanConnect() {
		dataSource.setId(4L);
		dataSource.setDataSourceId("ddd");
		dataSource.setDataSourceType(DataSourceType.CUSTOM_DATA_SOURCE);
		dataSource.setJdbcDriver("ddd");
		dataSource.setConnectUrl("ddd");
		dataSource.setUsername("ddd");
		dataSource.setPassword("ddd");
	}
	
	/**
	 * 模拟查询出一条数据库记录--系统数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockOneDbRecordExistOfSystemDataSourceById(){
		this.initSystemDataSourceAndDataSourceIdExist();
		when(repository.get(DataSource.class, dataSource.getId())).thenReturn(dataSource);
	}
	
	/**
	 * 模拟查询出一条数据库记录--自定义数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockOneDbRecordExistOfCustomDataSourceById(){
		this.initCustomDataSourceCanConnect();
		when(repository.get(DataSource.class, dataSource.getId())).thenReturn(dataSource);
	}
	
	/**
	 * 模拟未查询出一条数据库记录--系统数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockOneDbRecordNotExistOfSystemDataSourceById(){
		initSystemDataSourceAndDataSourceIdNotExist();
		when(repository.get(DataSource.class, dataSource.getId())).thenReturn(null);
	}
	
	/**
	 * 模拟未查询出一条数据库记录--自定义数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockOneDbRecordNotExistOfCustomDataSourceById(){
		initCustomDataSourceCannotConnect();
		when(repository.get(DataSource.class, dataSource.getId())).thenReturn(null);
	}
	
	
	/**
	 * 模拟查询出一条数据库记录--系统数据源（通过唯一标识dataSourceId）
	 * @param dataSource
	 */
	private void mockOneDbRecordExistOfSystemDataSourceByDataSourceId(){
		this.initSystemDataSourceAndDataSourceIdExist();
		when(queryChannel.querySingleResult(jpqlForDataSourceId, 
				new Object[] { dataSource.getDataSourceId() })).thenReturn(dataSource);
	}
	
	/**
	 * 模拟查询出一条数据库记录--自定义数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockOneDbRecordExistOfCustomDataSourceByDataSourceId(){
		this.initCustomDataSourceCanConnect();
		when(queryChannel.querySingleResult(jpqlForDataSourceId, 
				new Object[] { dataSource.getDataSourceId() })).thenReturn(dataSource);
	}
	
	/**
	 * 模拟未查询出一条数据库记录--系统数据源（通过唯一标识dataSourceId）
	 * @param dataSource
	 */
	private void mockOneDbRecordNotExistOfSystemDataSourceByDataSourceId(){
		initSystemDataSourceAndDataSourceIdNotExist();
		when(queryChannel.querySingleResult(jpqlForDataSourceId, 
				new Object[] { dataSource.getDataSourceId() })).thenReturn(null);
	}
	
	/**
	 * 模拟未查询出一条数据库记录--自定义数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockOneDbRecordNotExistOfCustomDataSourceByDataSourceId(){
		initCustomDataSourceCannotConnect();
		when(queryChannel.querySingleResult(jpqlForDataSourceId, 
				new Object[] { dataSource.getDataSourceId() })).thenReturn(null);
	}
	
	/**
	 * 断言相等
	 * @param dsId
	 * @param dataSourceVO
	 */
	private void assertResultEqualsById(){
		DataSourceVO dataSourceVO = turnToDataSourceVO();
		assertThat(instance.getDataSourceVoById(dataSource.getId()), is(dataSourceVO));
	}
	
	/**
	 * 断言相等
	 * @param dsId
	 * @param dataSourceVO
	 */
	private void assertResultNotEqualsById(){
		DataSourceVO dataSourceVO = turnToDataSourceVO();
		assertThat(instance.getDataSourceVoById(dataSource.getId()), not(dataSourceVO));
	}
	
	/**
	 * 断言相等
	 * @param dsId
	 * @param dataSourceVO
	 */
	private void assertResultEqualsByDataSourceId(){
		DataSourceVO dataSourceVO = turnToDataSourceVO();
		assertThat(instance.getDataSourceVoByDataSourceId(dataSource.getDataSourceId()), is(dataSourceVO));
	}
	
	/**
	 * 断言相等
	 * @param dsId
	 * @param dataSourceVO
	 */
	private void assertResultNotEqualsByDataSourceId(){
		DataSourceVO dataSourceVO = turnToDataSourceVO();
		assertThat(instance.getDataSourceVoByDataSourceId(dataSource.getDataSourceId()), not(dataSourceVO));
	}
	
	private void initProxoolDataSource(){
		ds = new ProxoolDataSource();
		ds.setAlias("test");
		ds.setDriver("org.h2.Driver");
		ds.setDriverUrl("jdbc:h2:~/db/koala");
		ds.setUser("sa");
		ds.setPassword("");
	}

}
