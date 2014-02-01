package com.dayatang.querychannel.service;

import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dayatang.querychannel.HibernateUtils;
import com.dayatang.querychannel.domain.MyEntity;
import com.dayatang.querychannel.service.impl.QueryChannelServiceHibernate;
import com.dayatang.querychannel.support.Page;

public class QueryChannelServiceHibernateTest {

	private static QueryChannelServiceHibernate queryHibernate;
	private static SessionFactory sessionFactory;
	private static final Log logger = LogFactory.getLog(QueryChannelServiceHibernateTest.class);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		sessionFactory = HibernateUtils.getSessionFactory();
		queryHibernate = new QueryChannelServiceHibernate(sessionFactory.openSession());

		Session session = queryHibernate.getSession();
		Transaction tx = session.beginTransaction();


		String hql = "delete from MyEntity";
		Query query = session.createQuery(hql);
		query.executeUpdate();
		
		logger.info("清洗测试数据完毕.");
		
		MyEntity entity = null;
		for (int i = 1; i < 5; i++) {

			entity = new MyEntity();

			entity.setName("entity" + i);
			entity.setVersion(1);

			Long id = (Long)session.save(entity);
			logger.info("初始化测试数据:MyEntity[{id="+id+",name="+entity.getName()+",version="+entity.getVersion()+"}]");
		}

		tx.commit();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		HibernateUtils.close();
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testQuerySingleResult() {
		String queryStr = "from MyEntity e where e.name = ?";
		Object[] params = new Object[]{"entity1"};
		
		MyEntity result = queryHibernate.querySingleResult(queryStr, params);
		Assert.assertNotNull(result);
	}

	@Test
	public void testQueryResultSize() {
		String queryStr = "from MyEntity e where e.name = ?";
		Object[] params = new Object[] { "entity" };
	
		long result = queryHibernate.queryResultSize(queryStr, params);

		Assert.assertTrue(result == 0);
	}

	@Test
	public void testQueryMapResult() {
		String queryStr = "select e from MyEntity e where e.name like ?";
		Object[] params = new Object[] { "entity%" };

		int currentPage = 1;
		int pageSize = 5;

		Page<Map<String, Object>> result = queryHibernate.queryPagedMapResult(
				queryStr, params, currentPage, pageSize);
		Assert.assertTrue(result.getTotalCount() == 4);

	}

	@Test
	public void testQueryPagedResult() {
		String queryStr = "from MyEntity e where e.name like ?";
		Object[] params = new Object[] { "entity%" };
		int firstRow = 1;
		int pageSize = 5;
		Page<MyEntity> result = queryHibernate.queryPagedResult(queryStr,
				params, firstRow, pageSize);

		Assert.assertTrue(result.getTotalCount() == 4);
	}

	@Test
	public void testQueryPagedResultByPageNo() {
		String queryStr = "from MyEntity e";
		Object[] params = null;
		int currentPage = 1;
		int pageSize = 5;
		Page<MyEntity> result = queryHibernate.queryPagedResultByPageNo(
				queryStr, params, currentPage, pageSize);

		Assert.assertTrue(result.getTotalCount() == 4);
	}

	@Test
	public void testQueryPagedResultByNamedQuery() {
		String queryName = "MyEntity.findByName";
		Object[] params = new Object[] { "entity%" };
		int firstRow = 1;
		int pageSize = 5;

		Page<MyEntity> result = queryHibernate.queryPagedResultByNamedQuery(
				queryName, params, firstRow, pageSize);

		Assert.assertTrue(result.getTotalCount() == 4);

	}

	@Test
	public void testQueryPagedResultByPageNoAndNamedQuery() {
		String queryName = "MyEntity.findByName";
		Object[] params = new Object[] { "entity%" };
		int currentPage = 1;
		int pageSize = 5;

		Page<MyEntity> result = queryHibernate
				.queryPagedResultByPageNoAndNamedQuery(queryName, params,
						currentPage, pageSize);

		Assert.assertTrue(result.getTotalCount() == 4);

	}

	@Test
	public void testQueryPagedMapResult() {
		String queryStr = "select e from MyEntity e where e.name like ?";
		Object[] params = new Object[] { "entity%" };
		int currentPage = 1;
		int pageSize = 5;

		Page<Map<String, Object>> result = queryHibernate.queryPagedMapResult(
				queryStr, params, currentPage, pageSize);
		Assert.assertTrue(result.getTotalCount() == 4);

	}

	@Test
	public void testQueryPagedMapResultByNamedQuery() {
		String queryName = "MyEntity.findByName";
		Object[] params = new Object[] { "entity%" };
		int currentPage = 1;
		int pageSize = 5;
		Page<Map<String, Object>> result = queryHibernate.queryPagedMapResultByNamedQuery(queryName, params,currentPage, pageSize);

		Assert.assertTrue(result.getTotalCount() == 4);
	}

	@Test
	public void testQueryPagedByQuerySettings() {
		// TODO not implemented yet !
	}

}
