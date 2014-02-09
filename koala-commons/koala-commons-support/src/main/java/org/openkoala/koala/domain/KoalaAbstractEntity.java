package org.openkoala.koala.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.QuerySettings;

@MappedSuperclass
public abstract class KoalaAbstractEntity implements Entity {

	private static final long serialVersionUID = 8882145540383345037L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Version
	@Column(name = "VERSION")
	private int version;


	/**
	 * 获得实体的标识
	 * 
	 * @return 实体的标识
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置实体的标识
	 * 
	 * @param id
	 *            要设置的实体标识
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获得实体的版本号。持久化框架以此实现乐观锁。
	 * 
	 * @return 实体的版本号
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * 设置实体的版本号。持久化框架以此实现乐观锁。
	 * 
	 * @param version
	 *            要设置的版本号
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public boolean isNew() {
		return id == null || id.intValue() == 0;
	}


	@Override
	public boolean notExisted() {
		return ! existed();
	}
	
	public boolean existed() {
		if (isNew()) {
			return false;
		}
		return getDataSource().exists(getClass(), id);
	}
	
	@Override
	public boolean existed(String propertyName, Object propertyValue) {
		List<?> entities = getDataSource().find(QuerySettings.create(getClass()).eq(propertyName, propertyValue)); 
		return !(entities.isEmpty());
	}


	public void save() {
		getDataSource().save(this);
	}

	public void remove() {
		getDataSource().remove(this);
	}



	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object other);

	@Override
	public abstract String toString();
	
	public abstract EntityRepository getDataSource();
}
