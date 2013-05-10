package com.dayatang.tapestry.hibernate;

import java.lang.reflect.Method;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.plastic.MethodInvocation;

import com.dayatang.annotations.Transactional;

public class HibernateTransactionAdvisorImpl implements HibernateTransactionAdvisor {
	
	private final HibernateSessionManager manager;

	/**
	 * The rules for advice are the same for any method: commit on success or
	 * checked exception, abort on thrown exception ... so we can use a single
	 * shared advice object.
	 */
	private final MethodAdvice advice = new MethodAdvice() {
		public void advise(MethodInvocation invocation) {
			try {
				invocation.proceed();
			} catch (RuntimeException ex) {
				manager.abort();

				throw ex;
			}

			// For success or checked exception, commit the transaction.

			manager.commit();
		}
	};

	public HibernateTransactionAdvisorImpl(HibernateSessionManager manager) {
		this.manager = manager;
	}

	public void addTransactionCommitAdvice(MethodAdviceReceiver receiver) {
		for (Method m : receiver.getInterface().getMethods()) {
			if (m.getAnnotation(Transactional.class) != null) {
				receiver.adviseMethod(m, advice);
			}
		}
	}

}
