package org.openkoala.test;


import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.koala.util.KoalaDataSourceDBUnitTestCase;
import org.springframework.util.Assert;

import com.dayatang.domain.InstanceFactory;

/**
 * 写一个测试类，继承KoalaDataSourceDBUnitTestCase
 * 默认集成了Koala的Spring IOC环境
 * IOC的默认配置如下 ：
 * @Transactional(propagation = Propagation.REQUIRED)
   @ContextConfiguration(locations = { "classpath*:META-INF/spring/root.xml" })
   @TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
   
   你可以在本类上，使用上述Annotation,修改默认的这些定义
 * @author lingen
 *
 */
public class KoalaDBUnitDataSourceTest extends KoalaDataSourceDBUnitTestCase {

	@Override
	/**
	 * 指定DBUnit的XML配置文件所在
	 */
	public String getDataXmlFile() {
		return "/dataxml/test.xml";
	}

	/**
	 * 指定IOC容器中的DataSourceName
	 */
	@Override
	public String getDataSourceName() {
		return "dataSource";
	}
	
	@Test
	@Ignore
	/**
	 * 使用Junit进行测试，你可以方便的使用InstanceFactory进行IOC定义中的BEAN的获取
	 */
	public void test(){
		Assert.isTrue(InstanceFactory.getInstance(DataSource.class)!=null);
	}

}
