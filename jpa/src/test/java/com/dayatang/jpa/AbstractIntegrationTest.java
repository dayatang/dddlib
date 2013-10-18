package com.dayatang.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.dayatang.commons.domain.Dictionary;
import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QuerySettings;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.dayatang.btm.BtmUtils;
import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.h2.H2Server;

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
        entityManager = emf.createEntityManager();
        InstanceFactory.bind(EntityManager.class, entityManager);
        repository = new EntityRepositoryJpa();
        AbstractEntity.setRepository(repository);
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
