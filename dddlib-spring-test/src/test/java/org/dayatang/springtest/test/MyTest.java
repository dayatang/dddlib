package org.dayatang.springtest.test;

import org.dayatang.dbunit.DataSetStrategy;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.springtest.AbstractIntegratedTestCase;
import org.dayatang.springtest.application.MyApplication;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MyTest extends AbstractIntegratedTestCase {

	private static MyApplication service;

	public static MyApplication getService() {
		if (service == null) {
			service = InstanceFactory.getInstance(MyApplication.class);
		}
		return service;
	}

	public static void setService(MyApplication service) {
		MyTest.service = service;
	}

	protected boolean rollback() {
		return true;
	}

	protected void action4SetUp() {
	}

	protected void action4TearDown() {
	}

	@Override
	protected DataSetStrategy getDataSetStrategy() {
		return DataSetStrategy.Xml;
	}

	@Override
	protected String[] getDataSetFilePaths() {
		return new String[] { "sample-data.xml" };
	}

	@Override
	protected DatabaseOperation setUpOp() {
		return DatabaseOperation.CLEAN_INSERT;
	}

	@Override
	protected DatabaseOperation tearDownOp() {
		return DatabaseOperation.DELETE_ALL;
	}

	@Test
	public void cc() {
		assertNotNull(getService());

		System.out.println("aaaa = " + getService().getMyEntityList());
		
		System.out.println("true = " + getService().getMyTrueEntityList());
		
	}
}
