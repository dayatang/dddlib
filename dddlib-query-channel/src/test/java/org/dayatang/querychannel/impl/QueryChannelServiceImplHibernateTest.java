/*
 * Copyright 2014 Dayatang Open Source..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dayatang.querychannel.impl;

import javax.inject.Inject;
import org.dayatang.btm.BtmUtils;
import org.dayatang.dbunit.DbUnitUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.ioc.spring.factory.SpringInstanceProvider;
import org.dayatang.utils.Page;
import org.dayatang.querychannel.domain.MyEntity;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/ApplicationContext-hibernate.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class QueryChannelServiceImplHibernateTest {

    @Inject
    private ApplicationContext ctx;

    @Inject
    private QueryChannelServiceImpl instance;

    private static BtmUtils btmUtils;

    private String jpqlNamedParam =  "select o from MyEntity o where o.name like :name order by o.id";

    private String jpqlPosParam =  "select o from MyEntity o where o.name like ? order by o.id";

    private String sqlNamedParam =  "select o.* from pay_test_myentity as o where o.name like :name order by o.id";

    private String sqlPosParam =  "select o.* from pay_test_myentity as o where o.name like ? order by o.id";

    private String queryNameNamedParam = "MyEntity.findByName1";

    private String queryNamePosParam = "MyEntity.findByName";

    private String paramKey = "name";

    private String paramValue = "entity%";

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

    //JpqlQuery

    @Test
    public void testJpqlQueryList() {
        List<MyEntity> results = instance.createJpqlQuery(jpqlNamedParam)
                .addParameter(paramKey, paramValue).list();
        assertEquals(12, results.size());
    }

    @Test
    public void testJpqlQueryPagedList() {
        Page<MyEntity> results = instance.createJpqlQuery(jpqlPosParam)
                .setParameters(paramValue).setFirstResult(3).setPageSize(5).pagedList();
        assertTrue(results.getData().contains(MyEntity.get(4L)));
        assertTrue(results.getData().contains(MyEntity.get(8L)));
        assertFalse(results.getData().contains(MyEntity.get(3L)));
        assertFalse(results.getData().contains(MyEntity.get(9L)));

        results = instance.createJpqlQuery(jpqlPosParam)
                .setParameters(paramValue).setPage(1, 5).pagedList();
        assertTrue(results.getData().contains(MyEntity.get(6L)));
        assertTrue(results.getData().contains(MyEntity.get(10L)));
        assertFalse(results.getData().contains(MyEntity.get(5L)));
        assertFalse(results.getData().contains(MyEntity.get(11L)));
    }

    @Test
    public void testJpqlQueryGetSingleResult() {
        MyEntity result = instance.createJpqlQuery("select o from MyEntity o where o.id = 1").singleResult();
        assertEquals(MyEntity.get(1L), result);
    }

    @Test
    public void testJpqlQueryGetResultCount() {
        assertEquals(12, instance.createJpqlQuery(jpqlNamedParam)
            .addParameter(paramKey, paramValue).queryResultCount());
    }


    //NamedQuery

    @Test
    public void testNamedQueryList() {
        List<MyEntity> results = instance.createNamedQuery(queryNameNamedParam)
                .addParameter(paramKey, paramValue).list();
        assertEquals(12, results.size());
    }

    @Test
    public void testNamedQueryPagedList() {
        Page<MyEntity> results = instance.createNamedQuery(queryNameNamedParam)
                .addParameter(paramKey, paramValue).setFirstResult(3).setPageSize(5).pagedList();
        assertTrue(results.getData().contains(MyEntity.get(4L)));
        assertTrue(results.getData().contains(MyEntity.get(8L)));
        assertFalse(results.getData().contains(MyEntity.get(3L)));
        assertFalse(results.getData().contains(MyEntity.get(9L)));

        results = instance.createNamedQuery(queryNamePosParam)
                .setParameters(paramValue).setPage(1, 5).pagedList();
        assertTrue(results.getData().contains(MyEntity.get(6L)));
        assertTrue(results.getData().contains(MyEntity.get(10L)));
        assertFalse(results.getData().contains(MyEntity.get(5L)));
        assertFalse(results.getData().contains(MyEntity.get(11L)));
    }

    @Test
    public void testNamedQueryGetSingleResult() {
        MyEntity result = instance.createNamedQuery("MyEntity.single").singleResult();
        assertEquals(MyEntity.get(1L), result);
    }

    @Test
    public void testNamedQueryGetResultCount() {
        assertEquals(12, instance.createNamedQuery(queryNameNamedParam)
            .addParameter(paramKey, paramValue).queryResultCount());
    }

    //SqlQuery

    @Test
    public void testSqlQueryList() {
        List<MyEntity> results = instance.createSqlQuery(sqlNamedParam)
                .setResultEntityClass(MyEntity.class)
                .addParameter(paramKey, paramValue).list();
        assertEquals(12, results.size());
    }

    @Test
    public void testSqlQueryPagedList() {
        Page<MyEntity> results = instance.createSqlQuery(sqlNamedParam)
                .setResultEntityClass(MyEntity.class)
                .addParameter(paramKey, paramValue)
                .setFirstResult(3).setPageSize(5).pagedList();
        assertTrue(results.getData().contains(MyEntity.get(4L)));
        assertTrue(results.getData().contains(MyEntity.get(8L)));
        assertFalse(results.getData().contains(MyEntity.get(3L)));
        assertFalse(results.getData().contains(MyEntity.get(9L)));

        results = instance.createSqlQuery(sqlPosParam)
                .setResultEntityClass(MyEntity.class)
                .setParameters(paramValue).setPage(1, 5).pagedList();
        assertTrue(results.getData().contains(MyEntity.get(6L)));
        assertTrue(results.getData().contains(MyEntity.get(10L)));
        assertFalse(results.getData().contains(MyEntity.get(5L)));
        assertFalse(results.getData().contains(MyEntity.get(11L)));
    }

    @Test
    public void testSqlQueryGetSingleResult() {
        MyEntity result = instance.createSqlQuery("select * from pay_test_myentity o where o.id = 1")
                .setResultEntityClass(MyEntity.class)
                .singleResult();
        assertEquals(MyEntity.get(1L), result);
    }

    @Test
    public void testSqlQueryGetResultCount() {
        assertEquals(12, instance.createSqlQuery(sqlNamedParam)
            .setResultEntityClass(MyEntity.class)
            .addParameter(paramKey, paramValue).queryResultCount());
    }
}
