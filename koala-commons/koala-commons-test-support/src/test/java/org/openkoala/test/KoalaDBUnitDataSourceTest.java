package org.openkoala.test;


import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.koala.util.KoalaDataSourceDBUnitTestCase;
import org.springframework.util.Assert;

import com.dayatang.domain.InstanceFactory;


public class KoalaDBUnitDataSourceTest extends KoalaDataSourceDBUnitTestCase {

	@Override
	public String getDataXmlFile() {
		return "/dataxml/test.xml";
	}

	@Override
	public String getDataSourceName() {
		return "dataSource";
	}
	
	@Test
	@Ignore
	public void test(){
		Assert.isTrue(InstanceFactory.getInstance(DataSource.class)!=null);
	}

}
