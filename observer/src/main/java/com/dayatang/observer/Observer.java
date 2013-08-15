package com.dayatang.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.domain.AbstractEntity;

@SuppressWarnings("rawtypes")
@Entity
@Table(name = "COMMONS_OBSERVER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "OBSERVER_CATEGORY", discriminatorType = DiscriminatorType.STRING)
public abstract class Observer<T extends Subject> extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4841919329150188032L;

	private static final Logger logger = LoggerFactory
			.getLogger(Observer.class);

	@ElementCollection
	@CollectionTable(name = "COMMONS_OBSERVER_SUBJECTKEY", joinColumns = @JoinColumn(name = "OBSERVER_ID"))
	@Column(name = "SUBJECT_KEY")
	private Set<String> subjectKeys = new HashSet<String>();

	public Set<String> getSubjectKeys() {
		return subjectKeys;
	}

	public void setSubjectKeys(Set<String> subjectKeys) {
		this.subjectKeys = subjectKeys;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/*
	 * =======================================
	 * 
	 * action
	 * 
	 * ========================================
	 */

	public abstract void process(T subject);

	public static Observer get(Long id) {
		return getRepository().get(Observer.class, id);
	}

	public static List<Observer> findBySubject(Subject subject) {
		String queryString = "select o from Observer o where :subjectKey in elements(o.subjectKeys))";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectKey", subject.getSubjectKey());
		List<Object> observers = getRepository().find(queryString, params, Object.class);

		if (logger.isDebugEnabled()) {
			if (observers.isEmpty()) {
				logger.debug("没有找到一个观察者：subjectKey为【{}】",
						subject.getSubjectKey());
			} else {
				for (Object observer : observers) {
					logger.debug("找到一个观察者：subjectKey为【{}】，observer为【{}】",
							subject.getSubjectKey(), observer);
				}
			}
		}
		List<Observer> results = new ArrayList<Observer>();
		for (Object observer : observers) {
			results.add((Observer) observer);
		}
		return results;
	}
}
