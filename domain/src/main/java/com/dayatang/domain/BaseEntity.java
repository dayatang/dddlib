/**
 * 
 */
package com.dayatang.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 抽象实体类，可作为所有领域实体的基类，提供ID和版本属性。
 * 
 */
@MappedSuperclass
@Deprecated
public abstract class BaseEntity implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2364892694478974374L;

	private static EntityRepository repository;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	protected Long id;

	@Version
	@Column(name = "VERSION")
	protected int version;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isNew() {
		return getId() == null;
	}

	public static EntityRepository getRepository() {
		if (repository == null) {
			repository = InstanceFactory.getInstance(EntityRepository.class);
		}
		return repository;
	}

	public static void setRepository(EntityRepository repository) {
		BaseEntity.repository = repository;
	}

	public void save() {
		getRepository().save(this);
	}

	public void remove() {
		getRepository().remove(this);
	}

	public static <T extends Entity> boolean exists(Class<T> clazz,
			Serializable id) {
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

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object arg0);

	@Override
	public String toString() {
		// return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
		// .append(recordId).append(tariffNo).toString();
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
