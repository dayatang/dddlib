package org.dayatang.dsmonitor.datasource;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.dsmonitor.dao.Dao;
import org.dayatang.dsmonitor.monitor.GeminiConnectionLogTimeoutMonitor;
import org.dayatang.springtest.PureSpringTestCase;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class DSTest extends PureSpringTestCase {

	@Ignore("在jdk 7 中会出错。")
	@Test
	public void testApp() throws InterruptedException {
		GeminiConnectionLogTimeoutMonitor monitor = InstanceFactory
				.getInstance(GeminiConnectionLogTimeoutMonitor.class,
						"connectionMonitor");
		Assert.assertEquals(0, monitor.getTimeoutConnections().size());
	}

	@Test
	public void testAppNotCloseConnection() throws InterruptedException {
		Dao dao = InstanceFactory.getInstance(Dao.class, "baseDao");
		dao.listResultWithoutCloseConnection(
				"from CommonsTestChild", new Object[] {});

		Thread.sleep(12000);

		GeminiConnectionLogTimeoutMonitor monitor = InstanceFactory
				.getInstance(GeminiConnectionLogTimeoutMonitor.class,
						"connectionMonitor");
		Assert.assertEquals(1, monitor.getAliveTimeoutConnections().size());
		Assert.assertEquals(0, monitor.getClosedTimeoutConnections().size());

		Thread.sleep(10000);
	}

}
