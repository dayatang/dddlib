package org.openkoala.test;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.koala.util.KoalaJdbcPropertiesDBUnitTestCase;
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
public class KoalaJdbcPropertiesDBUnitTestCaseTest extends
		KoalaJdbcPropertiesDBUnitTestCase {

	/**
	 * 指定Dbunit的xml位置所在
	 */
	@Override
	public String getDataXmlFile() {
		return "/dataxml/test.xml";
	}

	/**
	 * 指定jdbc属性配置文件所在
	 */
	@Override
	public String getPropertiesFile() {
		return "/META-INF/props/database2.properties";
	}
	
	/**
	 * 使用Junit进行测试，在这里可以使用IOC容器获取你想要的
	 */
	@Test
	@Ignore
	public void test(){
		Assert.isTrue(InstanceFactory.getInstance(DataSource.class)!=null);
	}

}
