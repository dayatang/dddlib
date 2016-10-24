package org.dayatang.dsmonitor;

import org.dayatang.dsmonitor.dao.Dao;
import org.dayatang.dsmonitor.monitor.GeminiConnectionLogTimeoutMonitor;
import org.dayatang.springtest.AbstractSpringIntegrationTest;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

public class DSTest extends AbstractSpringIntegrationTest {

    @Inject
    @Named("connectionMonitor")
    private GeminiConnectionLogTimeoutMonitor monitor;

    @Inject
    private Dao dao;

    @Test
    public void testAppNotCloseConnection() throws InterruptedException {
        dao.listResultWithoutCloseConnection(
                "select o from CommonsTestChild o", new Object[]{});

        Thread.sleep(12000);

        Assert.assertEquals(2, monitor.getAliveTimeoutConnections().size());

        Assert.assertEquals(5, monitor.getConnectionCount());

        Thread.sleep(10000);
    }

}
