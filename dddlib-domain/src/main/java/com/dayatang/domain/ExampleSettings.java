package com.dayatang.domain;

import java.util.HashSet;
import java.util.Set;

public class ExampleSettings<T> {
	private Class<T> entityClass;
	private final Set<String> excludedProperties = new HashSet<String>();
	private boolean likeEnabled = false;
	private boolean ignoreCaseEnabled = false;
	private boolean excludeNone = false;
	private boolean excludeZeroes = false;

	public static <T extends Entity> ExampleSettings<T> create(Class<T> entityClass) {
		return new ExampleSettings<T>(entityClass);
	}
	
	private ExampleSettings(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the entityClass
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * @return the excludedProperties
	 */
	public Set<String> getExcludedProperties() {
		return excludedProperties;
	}

	/**
	 * @return the likeEnabled
	 */
	public boolean isLikeEnabled() {
		return likeEnabled;
	}

	/**
	 * @return the ignoreCaseEnabled
	 */
	public boolean isIgnoreCaseEnabled() {
		return ignoreCaseEnabled;
	}

	/**
	 * @return the excludeNone
	 */
	public boolean isExcludeNone() {
		return excludeNone;
	}

	/**
	 * @return the excludeZeroes
	 */
	public boolean isExcludeZeroes() {
		return excludeZeroes;
	}

	public ExampleSettings<T> enableLike() {
		likeEnabled = true;
		return this;
	}

	public ExampleSettings<T> ignoreCase() {
		ignoreCaseEnabled = true;
		return this;
	}

	public ExampleSettings<T> excludeNone() {
		excludeNone = true;
		return this;
	}

	public ExampleSettings<T> excludeZeroes() {
		excludeZeroes = true;
		return this;
	}

	public ExampleSettings<T> exclude(String propName) {
		excludedProperties.add(propName);
		return this;
	}
}
