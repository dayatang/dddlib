package com.dayatang.querychannel.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dayatang.querychannel.domain.MyEntity;
import com.dayatang.querychannel.service.impl.QueryChannelServiceJpa;
import com.dayatang.querychannel.support.Page;

public class QueryChannelServiceJpaTest {

	private static EntityManagerFactory entityManagerFactory;
	private static QueryChannelServiceJpa queryJpa;
	private static Log logger = LogFactory.getLog("QueryChannelServiceJpaTest");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory("default");
		queryJpa = new QueryChannelServiceJpa(entityManagerFactory.createEntityManager());

		EntityManager em = queryJpa.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();

		Session session = (Session) em.getDelegate();

		String hql = "delete from MyEntity";
		Query query = session.createQuery(hql);

		query.executeUpdate();

		logger.info("清洗测试数据完毕.");

		MyEntity entity = null;
		for (int i = 1; i < 5; i++) {
			entity = new MyEntity();
			entity.setName("entity" + i);
			entity.setVersion(1);

			Long id = (Long) session.save(entity);

			logger.info("初始化测试数据:MyEntity[{id=" + id + ",name="
					+ entity.getName() + ",version=" + entity.getVersion()
					+ "}]");
		}

		et.commit();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		entityManagerFactory.close();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testQuerySingleResult() {
		String queryStr = "from MyEntity";
		Object[] params = null;
		MyEntity result = queryJpa.querySingleResult(queryStr, params);
		Assert.assertNotNull(result);
	}

	@Test
	public void testQueryResultSize() {
		String queryStr = "from MyEntity e where e.name like ?";
		Object[] params = new Object[] { "entity" };
		long result = queryJpa.queryResultSize(queryStr, params);

		Assert.assertTrue(result == 0);
	}

	@Test
	public void testQueryPagedResult() {
		String queryStr = "from MyEntity e where e.name like ?";
		Object[] params = new Object[] { "entity%" };
		int firstRow = 1;
		int pageSize = 5;
		Page<MyEntity> result = queryJpa.queryPagedResult(queryStr, params,
				firstRow, pageSize);

		Assert.assertTrue(result.getTotalCount() > 0);
	}

	@Test
	public void testQueryPagedResultByPageNo() {
		String queryStr = "from MyEntity e";
		Object[] params = null;
		int currentPage = 1;
		int pageSize = 5;
		Page<MyEntity> result = queryJpa.queryPagedResultByPageNo(queryStr,
				params, currentPage, pageSize);

		Assert.assertTrue(result.getTotalCount() > 0);
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
