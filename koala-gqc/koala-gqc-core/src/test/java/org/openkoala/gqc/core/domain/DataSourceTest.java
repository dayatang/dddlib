package org.openkoala.gqc.core.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.gqc.core.domain.utils.SystemDataSourceNotExistException;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 
 * @author lambo
 *
 */
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

	/**
	 * 测试
	 */
	@Test
	public void testIsNew() {
		boolean isNew = dataSource.isNew();
		assertTrue("应该是未入库的！",isNew);
	}

	/**
	 * 测试
	 */
	@Test
	public void testNotExisted() {
		boolean notExisted = dataSource.notExisted();
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
	public void testExists() {
		Boolean existed = dataSource.existed();
		assertTrue("应该是未入库的！",!existed);
	}

	/**
	 * 测试
	 */
	@Test
	public void testGet() {
		Boolean notExisted = dataSource.notExisted();
		assertTrue("应该是未入库的！",notExisted);
	}

	/**
	 * 测试
	 */
	@Test
	public void testGetUnmodified() {
		this.save();
		DataSource dataSourceBean = DataSource.getUnmodified(DataSource.class, dataSource);
		assertNotNull(dataSourceBean);
	}

	/**
	 * 测试
	 */
	@Test
	public void testLoad() {
		this.save();
		
		DataSource dataSourceBean = DataSource.load(DataSource.class, dataSource.getId());

		assertEquals(dataSource.getId(), dataSourceBean.getId());
	}

	/**
	 * 测试
	 */
	@Test
	public void testFindAll() {
		this.save();
		
		List<DataSource> list = DataSource.findAll(DataSource.class);
		
		assertEquals(1,list.size());
	}
	
	/**
	 * 测试
	 */
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

	/**
	 * 测试
	 */
	@Test
	public void testTestConnection() {
		//系统数据源，数据源id存在
		DataSource dataSource = initSystemDataSourceAndDataSourceIdExist();
		boolean result = dataSource.testConnection();
		assertTrue(result);
		
		//自定义数据源，能连接
		DataSource dataSource2 = initCustomDataSourceCanConnect();
		boolean result2 = dataSource2.testConnection();
		assertTrue(result2);
	}
	
	/**
	 * 测试系统数据源id不存在
	 */
	@Test(expected = SystemDataSourceNotExistException.class)
	public void testConnectionSystemDataSourceNotExist() {
		DataSource dataSource = initSystemDataSourceAndDataSourceIdNotExist();
		dataSource.testConnection();
	}
	
	/**
	 * 测试系统数据源id不存在
	 */
	@Test(expected = RuntimeException.class)
	public void testConnectionCustomDataSourceNotExist() {
		DataSource dataSource = initCustomDataSourceCannotConnect();
		dataSource.testConnection();
	}

	/**
	 * 测试
	 */
	@Test
	public void testGenerateConnection() {
		//系统数据源，数据源id存在
		DataSource dataSource2 = initSystemDataSourceAndDataSourceIdExist();
		Connection connection2 = dataSource2.generateConnection();
		assertNotNull(connection2);
		
		//自定义数据源，能连接
		DataSource dataSource4 = initCustomDataSourceCanConnect();
		Connection connection4 = dataSource4.generateConnection();
		assertNotNull(connection4);
	}
	
	/**
	 * 测试系统数据源id不存在
	 */
	@Test(expected = SystemDataSourceNotExistException.class)
	public void testGenerateConnectionSystemDataSourceNotExist() {
		DataSource dataSource = initSystemDataSourceAndDataSourceIdNotExist();
		dataSource.generateConnection();
	}
	
	/**
	 * 测试系统数据源id不存在
	 */
	@Test(expected = RuntimeException.class)
	public void testGenerateConnectionCustomDataSourceNotExist() {
		DataSource dataSource = initCustomDataSourceCannotConnect();
		dataSource.generateConnection();
	}
	
	/**
	 * 测试
	 */
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
	
	/**
	 * 保存
	 */
	private void save(){
		dataSource.save();
		boolean existed = dataSource.existed();
		assertTrue(existed);
	}
	
	/**
	 * 删除
	 */
	private void remove(){
		dataSource.remove();
		boolean notExisted = dataSource.notExisted();
		assertTrue(notExisted);
	}

}
