package org.openkoala.gqc.core.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.domain.EntityRepository;

@TransactionConfiguration(transactionManager = "transactionManager_gqc",defaultRollback = true) 
public class DataSourceTest extends KoalaBaseSpringTestCase{
	
	@Mock
	private EntityRepository repository;
	
	@Before
	public void beforeTest(){
		MockitoAnnotations.initMocks(this);
		DataSource.setRepository(repository);
	}
	
	@After
	public void afterTest(){
		DataSource.setRepository(null);
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

}
