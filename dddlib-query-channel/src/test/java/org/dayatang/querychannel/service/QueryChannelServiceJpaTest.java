package org.dayatang.querychannel.service;

import org.dayatang.querychannel.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dayatang.btm.BtmUtils;
import org.dayatang.dbunit.DbUnitUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.ioc.spring.factory.SpringInstanceProvider;
import org.dayatang.querychannel.domain.MyEntity;
import org.dayatang.querychannel.impl.QueryChannelServiceJpa;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/ApplicationContext-jpa.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class QueryChannelServiceJpaTest {

    private static final Log logger = LogFactory.getLog("QueryChannelServiceJpaTest");

    @Inject
    private ApplicationContext ctx;

    @Inject
    private QueryChannelServiceJpa queryJpa;

    private static BtmUtils btmUtils;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        btmUtils = BtmUtils.readConfigurationFromClasspath("/datasources.properties");
        btmUtils.setupDataSource();
        //DbUnitUtils.configFromClasspath("/jdbc.properties").importDataFromClasspath("/sample-data.xml");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        btmUtils.closeDataSource();
        btmUtils = null;
        System.out.println("================================================");
        System.out.println("关闭BTM");
    }

    @Before
    public void setUp() throws Exception {
        InstanceFactory.setInstanceProvider(new SpringInstanceProvider(ctx));
        DbUnitUtils.configFromClasspath("/jdbc.properties").importDataFromClasspath("/sample-data.xml");
    }

    @Test
    public void testQuerySingleResult() {
        String queryStr = "from MyEntity";
        Object[] params = null;
        MyEntity result = queryJpa.querySingleResult(queryStr, params);
        assertNotNull(result);
    }

    @Test
    public void testQueryResultSize() {
        String queryStr = "from MyEntity e where e.name like ?";
        Object[] params = new Object[]{"entity"};
        long result = queryJpa.queryResultSize(queryStr, params);

        assertTrue(result == 0);
    }

    @Test
    public void testQueryPagedResult() {
        String queryStr = "from MyEntity e where e.name like ?";
        Object[] params = new Object[]{"entity%"};
        int firstRow = 1;
        int pageSize = 5;
        Page<MyEntity> result = queryJpa.queryPagedResult(queryStr, params,
                firstRow, pageSize);

        assertTrue(result.getTotalCount() > 0);
    }

    @Test
    public void testQueryPagedResultByPageNo() {
        String queryStr = "from MyEntity e";
        Object[] params = null;
        int currentPage = 1;
        int pageSize = 5;
        Page<MyEntity> result = queryJpa.queryPagedResultByPageNo(queryStr,
                params, currentPage, pageSize);

        assertTrue(result.getTotalCount() > 0);
    }

    @Test
    public void testQueryMapResult() {
        // TODO not implemented yet !

    }

    @Test
    public void testQueryPagedResultByNamedQuery() {
        // TODO not implemented yet !
    }

    @Test
    public void testQueryPagedResultByPageNoAndNamedQuery() {
        // TODO not implemented yet !
    }

    @Test
    public void testQueryPagedMapResult() {
        // TODO not implemented yet !

    }

    @Test
    public void testQueryPagedMapResultByNamedQuery() {
        // TODO not implemented yet !
    }

    @Test
    public void testQueryPagedByQuerySettings() {
        // TODO not implemented yet !
    }

}
