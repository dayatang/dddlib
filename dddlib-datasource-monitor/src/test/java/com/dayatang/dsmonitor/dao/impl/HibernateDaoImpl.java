package com.dayatang.dsmonitor.dao.impl;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dayatang.dsmonitor.dao.Dao;

@SuppressWarnings("rawtypes")
public class HibernateDaoImpl implements Dao {
	
	@Inject
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return sessionFactory.openSession();
	}

	public List listResult(final String queryStr, final Object... values) {
		return createQuery(queryStr, values).list();
	}

	public List listResultWithoutCloseConnection(String queryStr,
			Object... values) {
		return createQuery(queryStr, values).list();
	}

	private Query createQuery(String queryStr, Object... values) {
		Query query = getSession().createQuery(queryStr);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

}
