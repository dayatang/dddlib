package org.openkoala.koala.auth.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QuerySettings;

/**
 * 时刻时段类实体。代表仅在一个确定的时间段内有效的一大类实体。
 * 所有的时刻时段实体类都有创建日期和废除日期。未废除的实体，其废除日期是一个遥远的未来日期。
 * @author yyang
 *
 */
@MappedSuperclass
public abstract class TimeIntervalEntity extends AbstractEntity {

	private static final long serialVersionUID = 858481853210607590L;

	private static EntityRepository repository;
	
	
	@Column(name = "CREATE_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "createDate.is.null")
	private Date createDate;

	@Column(name = "ABOLISH_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "abolishDate.is.null")
	private Date abolishDate;

	public TimeIntervalEntity() {
	}

	public TimeIntervalEntity(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getAbolishDate() {
		return abolishDate;
	}

	public void setAbolishDate(Date abolishDate) {
		this.abolishDate = abolishDate;
	}

	public boolean isAbolished(Date queryDate) {
		if (abolishDate == null) {
			return false;
		}
		return abolishDate.before(queryDate) || abolishDate.equals(queryDate);
	}

	
	public void validate() {
//		if (createDate == null) {
//			createDate = new Date();
//		}
//		if (abolishDate == null) {
//			abolishDate = DateUtils.MAX_DATE;
//		}
//		if (abolishDate.before(createDate)) {
//			addValidationException("abolishDate.before.createDate");
//		}
//		super.validate();
	}

	public static EntityRepository getRepository() {
		if (repository == null) {
			repository = InstanceFactory.getInstance(EntityRepository.class,"repository_ss");
		}
		return repository;
	}
	/**
	 * 在指定的日期废除HR实体。
	 * @param abolishDate
	 */
	public void abolish(Date abolishDate) {
		if (abolishDate == null) {
			throw new IllegalArgumentException("abolishDate.isNull");
		}
		if (isAbolished(abolishDate)) {
			return;
		}
		setAbolishDate(abolishDate);
		getRepository().save(this);
	}
	
	public static <T extends Entity> List<T> findAll(Class<T> clazz, Date queryDate) {
		return getRepository().find(QuerySettings.create(clazz)
				.le("createDate", queryDate)
				.gt("abolishDate", queryDate));
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
	
	public static <T extends Entity> List<T> findByNamedQuery(String queryName, Object[] params, Class<T> clazz)
	{
		return getRepository().findByNamedQuery(queryName, params, clazz);
	}
}

