package com.dayatang.hibernate;

import javax.transaction.UserTransaction;

import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.dayatang.btm.BtmUtils;
import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.h2.H2Server;

public class AbstractIntegrationTest {

	private static BtmUtils btmUtils;
	
	protected static SessionFactory sessionFactory;

	@BeforeClass
	public static void setUpClass() throws Exception {
		Configuration configuration = new ConfigurationFactory().fromClasspath("/jdbc.properties");
		btmUtils = BtmUtils.readConfigurationFromClasspath("/datasources.properties");
		btmUtils.setupDataSource();
		sessionFactory = HibernateUtils.getSessionFactory();
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		sessionFactory.close();
		btmUtils.closeDataSource();
		btmUtils = null;
		System.out.println("================================================");
		System.out.println("关闭BTM");
	}

	public UserTransaction getTransaction() {
		return btmUtils.getTransaction();
	}
	
}
