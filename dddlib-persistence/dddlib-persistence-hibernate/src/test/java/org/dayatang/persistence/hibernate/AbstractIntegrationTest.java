package org.dayatang.persistence.hibernate;

import org.dayatang.btm.BtmUtils;
import org.dayatang.domain.AbstractEntity;
import org.dayatang.domain.InstanceFactory;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class AbstractIntegrationTest {

    private static BtmUtils btmUtils;
    protected static SessionFactory sessionFactory;
    private UserTransaction tx;
    protected EntityRepositoryHibernate repository;

    @BeforeClass
    public static void setUpClass() throws Exception {
        btmUtils = BtmUtils.readConfigurationFromClasspath("/datasources.properties");
        btmUtils.setupDataSource();
        sessionFactory = HibernateUtils.getSessionFactory();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        sessionFactory.close();
        btmUtils.closeDataSource();
        btmUtils = null;
    }

    @Before
    public void setUp() throws Exception {
        InstanceFactory.bind(SessionFactory.class, sessionFactory);
        repository = new EntityRepositoryHibernate();
        AbstractEntity.setRepository(repository);
        tx = getTransaction();
        tx.begin();
    }

    @After
    public void tearDown() throws Exception {
        tx.rollback();
        repository = null;
        AbstractEntity.setRepository(null);
    }

    private UserTransaction getTransaction() {
        return btmUtils.getTransaction();
    }

}
