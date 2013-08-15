/**
 * 
 */
package com.dayatang.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

/**
 * 
 *  该对象将撤销，其持久化功能转移到AbstractEntity。因为Java的单根继承特性，使用本对象在现实中会导致一些局限。
 * 
 * @author yang
 * 
 */
@Deprecated
public abstract class AggregateRootEntity extends AbstractEntity {

	private static final long serialVersionUID = 2364892694478974374L;

	private static EntityRepository repository;

	@Transient
	private StringBuilder validationMessageBuilder = new StringBuilder();

	public static EntityRepository getRepository() {
		if (repository == null) {
			repository = InstanceFactory.getInstance(EntityRepository.class);
		}
		return repository;
	}

	public static void setRepository(EntityRepository repository) {
		AggregateRootEntity.repository = repository;
	}

	public void save() {
		getRepository().save(this);
	}

	public void remove() {
		getRepository().remove(this);
	}

	public static <T extends Entity> boolean exists(Class<T> clazz, Serializable id) {
		return getRepository().exists(clazz, id);
	}

	public static <T extends Entity> T get(Class<T> clazz, Serializable id) {
		return getRepository().get(clazz, id);
	}

	public static <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
		return getRepository().getUnmodified(clazz, entity);
	}

	public static <T extends Entity> T load(Class<T> clazz, Serializable id) {
		return getRepository().load(clazz, id);
	}

	public static <T extends Entity> List<T> findAll(Class<T> clazz) {
		return getRepository().find(QuerySettings.create(clazz));
	}
}
