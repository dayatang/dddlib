package com.dayatang.observer.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.hibernate.EntityRepositoryHibernate;
import com.dayatang.observer.HibernateUtils;

public class ObserverTest {

	private static SessionFactory sessionFactory;
	
	private Session session;

	private Transaction tx;

	private static EntityRepositoryHibernate repository;

	private MotherObserver observer_1;
	private FatherObserver observer_2;

	@BeforeClass
	public static void setUpClass() throws Exception {
		sessionFactory = HibernateUtils.getSessionFactory();
	}
	
	@AfterClass
	public static void tearDownClass() {
		sessionFactory.close();
	}
	
	@Before
	public void setUp() {
		session = sessionFactory.getCurrentSession();
		InstanceFactory.bind(Session.class, session);
		tx = session.beginTransaction();
		repository = new EntityRepositoryHibernate();
		AbstractEntity.setRepository(repository);
		observer_1 = createMotherObserver(false);
		observer_1.setSubjectKeys(Collections.singleton("BABY-SUBJECT"));
		observer_2 = createFatherObserver(false);
		observer_2.setSubjectKeys(Collections.singleton("BABY-SUBJECT"));
	}

	@After
	public void tearDown() {
		tx.rollback();
		if (session.isOpen()) {
			session.close();
		}
		AbstractEntity.setRepository(null);
	}
	
	private FatherObserver createFatherObserver(boolean startCar) {
		FatherObserver result = new FatherObserver();
		result.setStartCar(startCar);
		result.save();
		return result;
	}

	private MotherObserver createMotherObserver(boolean buyFood) {
		MotherObserver result = new MotherObserver();
		result.setBuyFood(buyFood);
		result.save();
		return result;
	}

	@Test
	public void process() {
		Baby baby = new Baby();
		baby.cry();

		//MotherObserver observer_1 = (MotherObserver) Observer.get(1L);
		assertTrue(observer_1.getBuyFood());
		//FatherObserver observer_2 = (FatherObserver) Observer.get(2L);
		assertTrue(observer_2.getStartCar());
	}

	@Test
	public void getKeys() {
		//MotherObserver observer_1 = (MotherObserver) Observer.get(1L);
		Set<String> keys = observer_1.getSubjectKeys();

		assertEquals("BABY-SUBJECT", keys.iterator().next());

		keys = new HashSet<String>();
		keys.add("1");
		observer_1.setSubjectKeys(keys);
		observer_1.save();

		keys = observer_1.getSubjectKeys();

		assertEquals("1", keys.iterator().next());

	}
}
