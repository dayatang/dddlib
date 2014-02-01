package org.dayatang.springtest.test;

import org.dayatang.dbunit.DataSetStrategy;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.springtest.AbstractIntegratedTestCase;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

public class SpringTest extends AbstractIntegratedTestCase {

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

	// @Override
	// protected String[] getDataSetFilePaths() {
	// return new String[] { "sample-data.xml" };
	// }

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
		CustomBean2 b2 = InstanceFactory.getInstance(CustomBean2.class);
		b2.aa();
	}
}
