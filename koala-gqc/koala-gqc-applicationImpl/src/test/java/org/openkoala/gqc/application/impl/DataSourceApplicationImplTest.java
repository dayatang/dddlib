package org.openkoala.gqc.application.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.openkoala.gqc.infra.util.DatabaseUtils;
import org.openkoala.gqc.vo.DataSourceVO;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dayatang.IocException;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QuerySettings;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ InstanceFactory.class, DatabaseUtils.class })
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
	
	private DataSourceVO dataSourceVO;
	
	private final String jpqlForDataSourceId = " select _dataSource from DataSource _dataSource  where _dataSource.dataSourceId = ? ";
	private final String jpqlForPage = " select _dataSource from DataSource _dataSource where 1=1 ";
	
	private int currentPage = 1;
	private int pageSize = 10;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		DataSource.setRepository(repository);
		instance.setQueryChannelService(queryChannel);
		//测试静态方法
		PowerMockito.mockStatic(InstanceFactory.class);
		PowerMockito.mockStatic(DatabaseUtils.class);
		dataSource = new DataSource();
	}

	@After
	public void tearDown() throws Exception {
		DataSource.setRepository(null);
		dataSource = null;
		dataSourceVO = null;
	}

	@Test
	public void testGetDataSourceById() {
		this.mockGetOneDbRecordOfSystemDataSourceExistById();
		this.assertResultEqualsById();
		
		this.mockGetOneDbRecordOfCustomDataSourceExistById();
		this.assertResultEqualsById();
		
		this.mockOneDbRecordOfSystemDataSourceNotExistById();
		this.assertResultNotEqualsById();
		
		this.mockOneDbRecordOfCustomDataSourceNotExistById();
		this.assertResultNotEqualsById();
	}
	
	@Test
	public void testGetDataSourceVoByDataSourceId() {
		this.mockOneDbRecordOfSystemDataSourceExistByDataSourceId();
		this.assertResultEqualsByDataSourceId();

		this.mockOneDbRecordOfCustomDataSourceExistByDataSourceId();
		this.assertResultEqualsByDataSourceId();

		this.mockOneDbRecordOfSystemDataSourceNotExistByDataSourceId();
		this.assertResultNotEqualsByDataSourceId();

		this.mockOneDbRecordOfCustomDataSourceNotExistByDataSourceId();
		this.assertResultNotEqualsByDataSourceId();
	}

	@Test
	public void testSaveDataSource() {
		this.mockOneDbRecordOfSystemDataSourceExistByDataSourceId();
		this.assertDataSourceExistInDb();
		
		this.mockOneDbRecordOfCustomDataSourceExistByDataSourceId();
		this.assertDataSourceExistInDb();
		
		this.mockOneDbRecordOfSystemDataSourceNotExistByDataSourceId();
		this.assertSystemDataSourceNotExistInSystem();
		
		this.initSystemDataSourceAndDataSourceIdNotInDbButInSystem();
		this.assertSaveSystemDataSourceSuccess();
		
		this.initCustomDataSourceCannotConnect();
		this.assertSaveCustomDataSourceSuccess();
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
		this.assertRemoveSystemDataSourceSuccess();
		this.assertRemoveCustomDataSourceSuccess();
	}
	
	@Test
	public void testRemoveDataSources() {
		Long[] ids = this.getIdsExistInDb();
		instance.removeDataSources(ids);
		assertTrue(true);
	}
	
	@Test
	public void testFindAllDataSource() {
		assertTrue(instance.findAllDataSource().isEmpty());
		
		int findCounts = 2;
		this.mockFindAllDataSources(findCounts);
		
		assertEquals(findCounts, instance.findAllDataSource().size());
	}
	
	@Test
	public void testFindAllTable() throws SQLException {
		this.mockGetOneDbRecordOfSystemDataSourceExistById();
		this.mockSqlDataSourceInstance();
		this.mockGetTables();
		this.closeSqlDataSourceConnection();
	}
	
	@Test
	public void testFindAllColumn() throws SQLException {
		this.mockGetOneDbRecordOfSystemDataSourceExistById();
		this.mockSqlDataSourceInstance();
		this.mockGetColumns();
		this.closeSqlDataSourceConnection();
	}
	
	@Test
	public void testPageQueryDataSource() {
		Page<Object> pages = new Page<Object>();
		when(queryChannel.queryPagedResultByPageNo(jpqlForPage, new ArrayList<Object>().toArray(), currentPage, pageSize)).thenReturn(pages);
		assertTrue(instance.pageQueryDataSource(new DataSourceVO(), 1, 10).getResult().isEmpty());
	}

	@Test
	public void testCheckDataSourceCanConnect() {
		this.mockGetOneDbRecordOfSystemDataSourceExistById();
		this.mockSqlDataSourceInstance();
		assertTrue(instance.checkDataSourceCanConnect(dataSource));
		this.closeSqlDataSourceConnection();
	}

	@Test
	public void testTestConnection() {
		this.mockGetOneDbRecordOfSystemDataSourceExistById();
		this.mockSqlDataSourceInstance();
		assertTrue(instance.testConnection(dataSource.getId()));
		this.closeSqlDataSourceConnection();
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
	private void mockGetOneDbRecordOfSystemDataSourceExistById(){
		this.initSystemDataSourceAndDataSourceIdExist();
		when(repository.get(DataSource.class, dataSource.getId())).thenReturn(dataSource);
	}
	
	/**
	 * 模拟查询出一条数据库记录--系统数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockLoadOneDbRecordOfSystemDataSourceExistById(){
		this.initSystemDataSourceAndDataSourceIdExist();
		when(repository.load(DataSource.class, dataSource.getId())).thenReturn(dataSource);
	}
	
	/**
	 * 模拟查询出一条数据库记录--自定义数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockGetOneDbRecordOfCustomDataSourceExistById(){
		this.initCustomDataSourceCanConnect();
		when(repository.get(DataSource.class, dataSource.getId())).thenReturn(dataSource);
	}
	
	/**
	 * 模拟查询出一条数据库记录--自定义数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockLoadOneDbRecordOfCustomDataSourceExistById(){
		this.initCustomDataSourceCanConnect();
		when(repository.load(DataSource.class, dataSource.getId())).thenReturn(dataSource);
	}
	
	/**
	 * 模拟未查询出一条数据库记录--系统数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockOneDbRecordOfSystemDataSourceNotExistById(){
		initSystemDataSourceAndDataSourceIdNotExist();
		when(repository.get(DataSource.class, dataSource.getId())).thenReturn(null);
	}
	
	/**
	 * 模拟未查询出一条数据库记录--自定义数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockOneDbRecordOfCustomDataSourceNotExistById(){
		initCustomDataSourceCannotConnect();
		when(repository.get(DataSource.class, dataSource.getId())).thenReturn(null);
	}
	
	
	/**
	 * 模拟查询出一条数据库记录--系统数据源（通过唯一标识dataSourceId）
	 * @param dataSource
	 */
	private void mockOneDbRecordOfSystemDataSourceExistByDataSourceId(){
		this.initSystemDataSourceAndDataSourceIdExist();
		when(queryChannel.querySingleResult(jpqlForDataSourceId, 
				new Object[] { dataSource.getDataSourceId() })).thenReturn(dataSource);
	}
	
	/**
	 * 模拟查询出一条数据库记录--自定义数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockOneDbRecordOfCustomDataSourceExistByDataSourceId(){
		this.initCustomDataSourceCanConnect();
		when(queryChannel.querySingleResult(jpqlForDataSourceId, 
				new Object[] { dataSource.getDataSourceId() })).thenReturn(dataSource);
	}
	
	/**
	 * 模拟未查询出一条数据库记录--系统数据源（通过唯一标识dataSourceId）
	 * @param dataSource
	 */
	private void mockOneDbRecordOfSystemDataSourceNotExistByDataSourceId(){
		initSystemDataSourceAndDataSourceIdNotExist();
		when(queryChannel.querySingleResult(jpqlForDataSourceId, 
				new Object[] { dataSource.getDataSourceId() })).thenReturn(null);
	}
	
	/**
	 * 模拟未查询出一条数据库记录--自定义数据源（通过主键id）
	 * @param dataSource
	 */
	private void mockOneDbRecordOfCustomDataSourceNotExistByDataSourceId(){
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
	
	private void assertDataSourceExistInDb(){
		dataSourceVO = turnToDataSourceVO();
		assertThat(instance.saveDataSource(dataSourceVO), is("该数据源ID已存在"));
	}
	
	private void assertSystemDataSourceNotExistInSystem(){
		dataSourceVO = turnToDataSourceVO();
		PowerMockito.when(InstanceFactory.getInstance(javax.sql.DataSource.class, dataSource.getDataSourceId())).thenThrow(new IocException());
		assertThat(instance.saveDataSource(dataSourceVO), StringContains.containsString("系统数据源不存在"));
	}
	
	private void assertSaveSystemDataSourceSuccess(){
		dataSourceVO = turnToDataSourceVO();
		this.mockSqlDataSourceInstance();
		assertNull(instance.saveDataSource(dataSourceVO));
		this.closeSqlDataSourceConnection();
	}
	
	private void mockSqlDataSourceInstance(){
		this.initProxoolDataSource();
		PowerMockito.when(InstanceFactory.getInstance(javax.sql.DataSource.class, 
				dataSource.getDataSourceId())).thenReturn(ds);
	}
	
	private void closeSqlDataSourceConnection(){
		try {
			ds.getConnection().close();
		} catch (SQLException e) {
			fail("连接关闭失败！");
		}
	}
	
	private void assertSaveCustomDataSourceSuccess(){
		dataSourceVO = turnToDataSourceVO();
		when(repository.save(dataSource)).thenReturn(dataSource);
		assertThat(instance.saveDataSource(dataSourceVO),IsNull.nullValue());
	}
	
	private Long[] getIdsExistInDb(){
		Long[] ids = new Long[2];
		for(int i=0; i<ids.length; i++){
			DataSource dataSource = new DataSource();
			dataSource.setId(i+1L);
			when(repository.load(DataSource.class, dataSource.getId())).thenReturn(dataSource);
			ids[i] = i+1L;
		}
		
		return ids;
	}
	
	private Long getSystemId(){
		this.mockLoadOneDbRecordOfSystemDataSourceExistById();
		return dataSource.getId();
	}
	
	private Long getCustomId(){
		this.mockLoadOneDbRecordOfCustomDataSourceExistById();
		return dataSource.getId();
	}
	
	private void assertRemoveSystemDataSourceSuccess(){
		Long SystemId = this.getSystemId();
		instance.removeDataSource(SystemId);
		assertTrue(true);
	}
	
	private void assertRemoveCustomDataSourceSuccess(){
		Long CustomId = this.getCustomId();
		instance.removeDataSource(CustomId);
		assertTrue(true);
	}
	
	private void mockFindAllDataSources(int saveCounts){
		List<DataSource> all = new ArrayList<DataSource>();
		for(int i=0; i<saveCounts; i++){
			DataSource dataSource = new DataSource();
			dataSource.setId(i + 1L);
			all.add(dataSource);
		}
		when(repository.find(QuerySettings.create(DataSource.class))).thenReturn(all);
	}
	
	private void mockGetTables() throws SQLException{
		List<String> tables = new ArrayList<String>();
		tables.add("table1");
		tables.add("table2");
		PowerMockito.when(DatabaseUtils.getTables(ds.getConnection())).thenReturn(tables);
	}
	
	private void mockGetColumns() throws SQLException{
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("column1", 1);
		map.put("column2", 2);
		String tableName = "table1";
		PowerMockito.when(DatabaseUtils.getColumns(ds.getConnection(), tableName)).thenReturn(map);
	}

}
