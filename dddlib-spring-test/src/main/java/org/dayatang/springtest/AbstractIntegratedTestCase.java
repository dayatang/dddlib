package org.dayatang.springtest;

import org.dayatang.dbunit.DataSetStrategy;
import org.dayatang.dbunit.Dbunit;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.ioc.spring.factory.SpringInstanceProvider;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 一个集成测试基类，提供了访问IoC容器（通过Spring实现）和访问数据库（通过DBUnit）的能力。
 *
 */
public abstract class AbstractIntegratedTestCase extends Dbunit {

	protected Session session;

	private Transaction tx;

	HibernateTransactionManager tm;

	TransactionStatus ts;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InstanceFactory
				.setInstanceProvider(new SpringInstanceProvider(springXmlPath()));
	}

	private static String[] springXmlPath() {
		return new String[] { "classpath*:spring/*.xml" };
	}

	@Override
	public final void setUp() throws Exception {
		super.setUp();

		SessionFactory sessionFactory = InstanceFactory
				.getInstance(SessionFactory.class);
		tm = InstanceFactory.getInstance(HibernateTransactionManager.class);
		if (tm != null) {
			ts = tm.getTransaction(new DefaultTransactionDefinition());
		}
		if (sessionFactory != null) {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
		}
		action4SetUp();
	}

	@Override
	public final void tearDown() throws Exception {
		super.tearDown();
		if (rollback()) {
			if (ts != null && tm != null) {
				tm.rollback(ts);
			}
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		} else {
			if (ts != null && tm != null) {
				tm.commit(ts);
			}
			if (tx != null && tx.isActive()) {
				tx.commit();
			}
		}
		if (session != null && session.isOpen()) {
			session.close();
		}
		session = null;
		action4TearDown();
	}

	/**
	 * 设置单元测试方法是否回滚，true=回滚
	 * 
	 * @return 单元测试方法是否回滚
	 */
	protected boolean rollback() {
		return true;
	}

	/**
	 * 单元测试方法执行前的操作
	 */
	protected void action4SetUp() {
	}

	/**
	 * 单元测试方法执行后的操作
	 */
	protected void action4TearDown() {
	}

	@Override
	protected DataSetStrategy getDataSetStrategy() {
		return DataSetStrategy.FlatXml;
	}

	@Override
	protected DatabaseOperation setUpOp() {
		return DatabaseOperation.CLEAN_INSERT;
	}

	@Override
	protected DatabaseOperation tearDownOp() {
		return DatabaseOperation.DELETE_ALL;
	}

}
