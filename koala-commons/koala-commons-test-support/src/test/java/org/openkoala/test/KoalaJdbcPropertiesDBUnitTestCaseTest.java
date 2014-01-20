package org.openkoala.test;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.koala.util.KoalaJdbcPropertiesDBUnitTestCase;
import org.springframework.util.Assert;

import com.dayatang.domain.InstanceFactory;

public class KoalaJdbcPropertiesDBUnitTestCaseTest extends
		KoalaJdbcPropertiesDBUnitTestCase {

	@Override
	public String getDataXmlFile() {
		return "dataxml/test.xml";
	}

	@Override
	public String getPropertiesFile() {
		return "META-INF/props/database2.properties";
	}
	
	@Test
	@Ignore
	public void test(){
		Assert.isTrue(InstanceFactory.getInstance(DataSource.class)!=null);
	}

}
