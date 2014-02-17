package org.dayatang.querychannel.service;

import org.dayatang.querychannel.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dayatang.btm.BtmUtils;
import org.dayatang.dbunit.DbUnitUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.ioc.spring.factory.SpringInstanceProvider;
import org.dayatang.querychannel.domain.MyEntity;
import org.dayatang.querychannel.impl.QueryChannelServiceHibernate;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/ApplicationContext-hibernate.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class QueryChannelServiceHibernateTest {
    
    private static final Log logger = LogFactory.getLog(QueryChannelServiceHibernateTest.class);

    @Inject
    private ApplicationContext ctx;

    @Inject
    private QueryChannelServiceHibernate queryHibernate;

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


    @After
    public void tearDown() throws Exception {
        InstanceFactory.setInstanceProvider(null);
    }

    @Test
    public void testQuerySingleResult() {
        String queryStr = "from MyEntity e where e.name = ?";
        Object[] params = new Object[]{"entity1"};

        MyEntity result = queryHibernate.querySingleResult(queryStr, params);
        assertNotNull(result);
    }

    @Test
    public void testQueryResultSize() {
        String queryStr = "from MyEntity e where e.name = ?";
        Object[] params = new Object[]{"entity"};

        long result = queryHibernate.queryResultSize(queryStr, params);

        assertTrue(result == 0);
    }

    @Test
    public void testQueryMapResult() {
        String queryStr = "select e from MyEntity e where e.name like ?";
        Object[] params = new Object[]{"entity%"};

        int currentPage = 1;
        int pageSize = 5;

        Page<Map<String, Object>> result = queryHibernate.queryPagedMapResult(
                queryStr, params, currentPage, pageSize);
        assertEquals(4, result.getTotalCount());

    }

    @Test
    public void testQueryPagedResult() {
        String queryStr = "from MyEntity e where e.name like ?";
        Object[] params = new Object[]{"entity%"};
        int firstRow = 1;
        int pageSize = 5;
        Page<MyEntity> result = queryHibernate.queryPagedResult(queryStr,
                params, firstRow, pageSize);

        assertTrue(result.getTotalCount() == 4);
    }

    @Test
    public void testQueryPagedResultByPageNo() {
        String queryStr = "from MyEntity e";
        Object[] params = null;
        int currentPage = 1;
        int pageSize = 5;
        Page<MyEntity> result = queryHibernate.queryPagedResultByPageNo(
                queryStr, params, currentPage, pageSize);

        assertTrue(result.getTotalCount() == 4);
    }

    @Test
    public void testQueryPagedResultByNamedQuery() {
        String queryName = "MyEntity.findByName";
        Object[] params = new Object[]{"entity%"};
        int firstRow = 1;
        int pageSize = 5;

        Page<MyEntity> result = queryHibernate.queryPagedResultByNamedQuery(
                queryName, params, firstRow, pageSize);

        assertTrue(result.getTotalCount() == 4);

    }

    @Test
    public void testQueryPagedResultByPageNoAndNamedQuery() {
        String queryName = "MyEntity.findByName";
        Object[] params = new Object[]{"entity%"};
        int currentPage = 1;
        int pageSize = 5;

        Page<MyEntity> result = queryHibernate
                .queryPagedResultByPageNoAndNamedQuery(queryName, params,
                        currentPage, pageSize);

        assertTrue(result.getTotalCount() == 4);

    }

    @Test
    public void testQueryPagedMapResult() {
        String queryStr = "select e from MyEntity e where e.name like ?";
        Object[] params = new Object[]{"entity%"};
        int currentPage = 1;
        int pageSize = 5;

        Page<Map<String, Object>> result = queryHibernate.queryPagedMapResult(
                queryStr, params, currentPage, pageSize);
        assertTrue(result.getTotalCount() == 4);

    }

    @Test
    public void testQueryPagedMapResultByNamedQuery() {
        String queryName = "MyEntity.findByName";
        Object[] params = new Object[]{"entity%"};
        int currentPage = 1;
        int pageSize = 5;
        Page<Map<String, Object>> result = queryHibernate.queryPagedMapResultByNamedQuery(queryName, params, currentPage, pageSize);

        assertTrue(result.getTotalCount() == 4);
    }

    @Test
    public void testQueryPagedByQuerySettings() {
        // TODO not implemented yet !
    }

}
