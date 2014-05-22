package org.dayatang.persistence.jpa;

import org.dayatang.btm.BtmUtils;
import org.dayatang.domain.AbstractEntity;
import org.dayatang.domain.InstanceFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.SystemException;

public class AbstractIntegrationTest {

    private static BtmUtils btmUtils;

    private static EntityManagerFactory emf;

    protected EntityManager entityManager;

    private EntityTransaction tx;

    protected EntityRepositoryJpa repository;

    @BeforeClass
    public static void setUpClass() throws Exception {
        btmUtils = BtmUtils.readConfigurationFromClasspath("/datasources.properties");
        btmUtils.setupDataSource();
        emf = Persistence.createEntityManagerFactory("default");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        emf.close();
        btmUtils.closeDataSource();
        btmUtils = null;
        System.out.println("================================================");
        System.out.println("关闭BTM");
    }

    @Before
    public void setUp() {
        InstanceFactory.bind(EntityManagerFactory.class, emf);
        repository = new EntityRepositoryJpa(emf);
        AbstractEntity.setRepository(repository);
        entityManager = repository.getEntityManager();
        tx = entityManager.getTransaction();
        tx.begin();
    }

    @After
    public void tearDown() throws IllegalStateException, SystemException {
        tx.rollback();
        entityManager.close();
        repository = null;
        AbstractEntity.setRepository(null);
    }
}
