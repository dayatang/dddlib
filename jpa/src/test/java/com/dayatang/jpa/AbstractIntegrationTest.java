package com.dayatang.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.dayatang.btm.BtmUtils;
import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.h2.H2Server;

public class AbstractIntegrationTest {

	private static BtmUtils btmUtils;

	protected static EntityManagerFactory emf;

	@BeforeClass
	public static void setUpClass() throws Exception {
		Configuration configuration = new ConfigurationFactory().fromClasspath("/jdbc.properties");
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

	public UserTransaction getTransaction() {
		return btmUtils.getTransaction();
	}
	
}
