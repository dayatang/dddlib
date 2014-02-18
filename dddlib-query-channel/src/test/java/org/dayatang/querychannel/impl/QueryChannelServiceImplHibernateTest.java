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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
    private QueryChannelServiceHibernate instance;

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
    public void testCreateJpqlQuery() {
    }

    @Test
    public void testCreateNamedQuery() {
    }

    @Test
    public void testCreateSqlQuery() {
    }

    @Test
    public void testList() {
    }

    @Test
    public void testPagedList() {
    }

    @Test
    public void testGetSingleResult() {
    }

    @Test
    public void testGetResultCount() {
    }
    
}
