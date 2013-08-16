package org.openkoala.gqc.core.domain;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(transactionManager = "transactionManager_gqc",defaultRollback = true) 
public class DataSourceTest extends KoalaBaseSpringTestCase{
	
	/**
	 * 数据源实例
	 */
	private DataSource dataSource;

	/**
	 * 初始化数据源实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		dataSource = initSystemDataSourceAndDataSourceIdNotExist();
	}

	/**
	 * 销毁数据源实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		dataSource = null;
	}

	@Test
	public void testIsNew() {
		boolean isNew = dataSource.isNew();
		assertTrue("应该是未入库的！",isNew);
	}

	@Test
	public void testNotExisted() {
		boolean notExisted = dataSource.notExisted();
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
	public void testExists() {
		Boolean existed = dataSource.existed();
		assertTrue("应该是未入库的！",!existed);
	}

	@Test
	public void testGet() {
		Boolean notExisted = dataSource.notExisted();
		assertTrue("应该是未入库的！",notExisted);
	}

	@Test
	public void testGetUnmodified() {
		this.save();
		DataSource dataSourceBean = DataSource.getUnmodified(DataSource.class, dataSource);
		assertNotNull(dataSourceBean);
	}

	@Test
	public void testLoad() {
		this.save();
		
		DataSource dataSourceBean = DataSource.load(DataSource.class, dataSource.getId());

		assertEquals(dataSource.getId(), dataSourceBean.getId());
	}

	@Test
	public void testFindAll() {
		this.save();
		
		List<DataSource> list = DataSource.findAll(DataSource.class);
		
		assertEquals(1,list.size());
	}
	
	@Test
	public void testGetSystemDataSource() {
		//不存在的系统数据源
		String systemDataSourceIdNotExist = "aaaaabbb";
		//存在的系统数据源
		String systemDataSourceIdExist = "dataSource_gqc";
		try {
			DataSource dataSourceNotExist = null;
			try {
				//当使用不存在的系统数据源时，这里会抛出异常
				dataSourceNotExist = DataSource
						.getSystemDataSource(systemDataSourceIdNotExist);
				//如果能走到这里，说明systemDataSourceIdNotExist是存在的系统数据源，不符合测试预期：系统数据源不存在
				assertFalse(true);
			} catch (Exception e) {
				assertNull(dataSourceNotExist);
			}
			
			DataSource dataSourceExist = DataSource.getSystemDataSource(systemDataSourceIdExist);
			assertNotNull(dataSourceExist);
			
		} catch (SQLException e) {
			assertFalse(true);
		}
	}

	@Test
	public void testTestConnection() {
		try {
			boolean result = false;
			try {
				//系统数据源，数据源id不存在
				DataSource dataSource = initSystemDataSourceAndDataSourceIdNotExist();
				result = dataSource.testConnection();
				
				assertFalse(true);
			} catch (Exception e) {
				assertTrue(!result);
			}
			
			//系统数据源，数据源id存在
			DataSource dataSource2 = initSystemDataSourceAndDataSourceIdExist();
			boolean result2 = dataSource2.testConnection();
			assertTrue(result2);
			
			//自定义数据源，不能连接
			DataSource dataSource3 = initCustomDataSourceCannotConnect();
			boolean result3 = dataSource3.testConnection();
			assertTrue(!result3);
			
			//自定义数据源，能连接
			DataSource dataSource4 = initCustomDataSourceCanConnect();
			boolean result4 = dataSource4.testConnection();
			assertTrue(result4);
			
		} catch (Exception e) {
			assertFalse(true);
		}
		
	}

	@Test
	public void testGenerateConnection() {
		try {
			Connection connection = null;
			try {
				//系统数据源，数据源id不存在
				DataSource dataSource = initSystemDataSourceAndDataSourceIdNotExist();
				connection = dataSource.generateConnection();
				
				assertFalse(true);
			} catch (Exception e) {
				assertNull(connection);
			}
			
			//系统数据源，数据源id存在
			DataSource dataSource2 = initSystemDataSourceAndDataSourceIdExist();
			Connection connection2 = dataSource2.generateConnection();
			assertNotNull(connection2);
			
			//自定义数据源，不能连接
			DataSource dataSource3 = initCustomDataSourceCannotConnect();
			Connection connection3 = dataSource3.generateConnection();
			assertNull(connection3);
			
			//自定义数据源，能连接
			DataSource dataSource4 = initCustomDataSourceCanConnect();
			Connection connection4 = dataSource4.generateConnection();
			assertNotNull(connection4);
			
		} catch (Exception e) {
			assertFalse(true);
		}
	}
	
	@Test
	public void testExisted(){
		DataSource dataSource = new DataSource();
		boolean result = dataSource.existed();
		assertTrue(!result);
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
	
	/**
	 * 初始化自定义数据源，不能连接
	 * @return
	 */
	private DataSource initCustomDataSourceCannotConnect(){
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
	private DataSource initCustomDataSourceCanConnect(){
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.CUSTOM_DATA_SOURCE);
		dataSource.setJdbcDriver("org.h2.Driver");
		dataSource.setConnectUrl("jdbc:h2:~/db/test");
		dataSource.setUsername("sa");
		dataSource.setPassword("");

		return dataSource;
	}
	
	private void save(){
		dataSource.save();
		boolean existed = dataSource.existed();
		assertTrue(existed);
	}
	
	private void remove(){
		dataSource.remove();
		boolean notExisted = dataSource.notExisted();
		assertNull(notExisted);
	}

}
