package org.openkoala.koala.util;
import javax.sql.DataSource;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringInstanceProvider;

/**
 * Koala提供的整合
 * 
 * @author lingen
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional(propagation = Propagation.REQUIRED)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/root.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class KoalaDBUnitTestCase implements ApplicationContextAware {

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		InstanceFactory.setInstanceProvider(new SpringInstanceProvider(
				applicationContext));
	}

	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(Object.class.getResourceAsStream(getDataXmlFile()));
	}

	/**
	 * 由具体实现的类去指定的XML文件
	 * 
	 * @return
	 */
	public abstract String getDataXmlFile();
	
	public abstract IDatabaseTester getIDatabaseTester();

	protected DatabaseOperation getSetUpOperation() throws Exception {
		return DatabaseOperation.INSERT;
	}

	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.DELETE;
	}
	
	

	private IDatabaseTester databaseTester ;
	@Before
	public void setUp() throws Exception {
		
		databaseTester = getIDatabaseTester();//new DataSourceDatabaseTester(getDataSource());
        
		IDataSet dataSet = getDataSet();
		
		databaseTester.setDataSet( dataSet );
		
		databaseTester.onSetup();
	}

	@After
	public void tearDown() throws Exception {
		databaseTester.onTearDown();
	}

}
