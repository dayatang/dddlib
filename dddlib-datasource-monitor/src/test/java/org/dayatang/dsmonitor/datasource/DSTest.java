package org.dayatang.dsmonitor.datasource;

import org.dayatang.dsmonitor.dao.Dao;
import org.dayatang.dsmonitor.monitor.GeminiConnectionLogTimeoutMonitor;
import org.dayatang.springtest.AbstractSpringIntegrationTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

public class DSTest extends AbstractSpringIntegrationTest {

    @Inject
    @Named("connectionMonitor")
    private GeminiConnectionLogTimeoutMonitor monitor;

    @Inject
    private Dao dao;

    @Ignore("在jdk 7 中会出错。")
    @Test
    public void testApp() throws InterruptedException {
        Assert.assertEquals(0, monitor.getTimeoutConnections().size());
    }

    @Test
    public void testAppNotCloseConnection() throws InterruptedException {
        dao.listResultWithoutCloseConnection(
                "from CommonsTestChild", new Object[]{});

        Thread.sleep(12000);

        Assert.assertEquals(2, monitor.getAliveTimeoutConnections().size());
        //Assert.assertEquals(1, monitor.getAliveTimeoutConnections().size());
        Assert.assertEquals(0, monitor.getClosedTimeoutConnections().size());

        Thread.sleep(10000);
    }

}
