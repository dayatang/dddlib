package org.openkoala.koala.auth.core.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dayatang.domain.QuerySettings;
import com.dayatang.utils.DateUtils;

/**
 * 责任关系。这是人力资源两个关键抽象之一（另一个是当事人Party），代表当事人之间的责任关系。 通过不同的子类分别代表归属、聘用、管理、合同等关系。
 * 
 * @author yyang
 * 
 */
@MappedSuperclass
public abstract class Accountability extends TimeIntervalEntity {
	
	private static final long serialVersionUID = -4407018237346614465L;

	private static final String ABOLISH_DATE = "abolishDate";

	private static final String CREATE_DATE = "createDate";

	private static final String RESPONSIBLE = "responsible";

	private static final String COMMISSIONER = "commissioner";

	@Column(name = "scheduled_abolish_date")
	@Temporal(TemporalType.DATE)
	private Date scheduledAbolishDate;

	@Column(length = 512)
	private String remark;

	public Accountability() {
	}

	public Accountability(Date createDate) {
		super(createDate);
	}

	public Accountability(Date createDate, Date scheduledAbolishDate) {
		this(createDate);
		if (scheduledAbolishDate != null) {
			this.scheduledAbolishDate = scheduledAbolishDate;
		}
	}

	public Date getScheduledAbolishDate() {
		return scheduledAbolishDate;
	}

	public void setScheduledAbolishDate(Date scheduledAbolishDate) {
		this.scheduledAbolishDate = scheduledAbolishDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public void validate() {
		if (scheduledAbolishDate == null) {
			scheduledAbolishDate = DateUtils.MAX_DATE;
		}
	}

	/**
	 * 延长责任关系的结束期限
	 * @param date 计划延长到的日期
	 */
	public void extendTo(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("extendAbolishDate.isNull");
		}
		setScheduledAbolishDate(date);
		getRepository().save(this);
	}

	/**
	 * 根据ID获取Accountability。
	 * 
	 * @param id
	 * @return
	 */
	public static Accountability get(Long id) {
		return getRepository().get(Accountability.class, id);
	}
	
	/**
	 * 取得在指定日期，指定commissioner和responsible之间的指定类型的责任关系
	 * @param <T>
	 * @param entityClass
	 * @param commissioner
	 * @param responsible
	 * @param queryDate
	 * @return
	 */
	public static <T extends Accountability> T get(Class<T> entityClass, Party commissioner, Party responsible, Date queryDate) {
		QuerySettings<T> settings = QuerySettings.create(entityClass)
				.eq(COMMISSIONER, commissioner)
				.eq(RESPONSIBLE, responsible)
				.le(CREATE_DATE, queryDate)
				.gt(ABOLISH_DATE, queryDate);
		List<T> results =  getRepository().find(settings);
		return results.isEmpty() ? null : results.get(0);
	}
	
	/**
	 * 列出所有指定类型的责任关系，按责任发生时间的逆序排列。
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public static <T extends Accountability> List<T> findAccountabilities(Class<T> entityClass, Date queryDate) {
		return getRepository().find(QuerySettings.create(entityClass)
				.le(CREATE_DATE, queryDate)
				.gt(ABOLISH_DATE, queryDate));
	}
	
	/**
	 * 列出指定的当事人作为commissioner参加的责任关系,按责任发生时间的逆序排列。
	 * @param <T>
	 * @param entityClass
	 * @param commissioner
	 * @return
	 */
	public static <T extends Accountability> List<T> findByCommissioner(Class<T> entityClass, Party commissioner) {
		return getRepository().find(QuerySettings.create(entityClass)
				.eq(COMMISSIONER, commissioner)
				.asc(CREATE_DATE));
	}
	
	/**
	 * 列出指定日期指定的当事人作为commissioner参加的责任关系,按责任发生时间的逆序排列。
	 * @param <T>
	 * @param entityClass
	 * @param commissioner
	 * @param queryDate
	 * @return
	 */
	public static <T extends Accountability> List<T> findByCommissioner(Class<T> entityClass, Party commissioner, Date queryDate) {
		return getRepository().find(QuerySettings.create(entityClass)
				.eq(COMMISSIONER, commissioner)
				.le(CREATE_DATE, queryDate)
				.gt(ABOLISH_DATE, queryDate));
	}
	
	/**
	 * 列出指定日期指定的当事人集合作为commissioner参加的责任关系,按责任发生时间的逆序排列。
	 * @param <T>
	 * @param entityClass
	 * @param commissioners
	 * @param queryDate
	 * @return
	 */
	public static <T extends Accountability> List<T> findByCommissioners(Class<T> entityClass, Collection<? extends Party> commissioners, Date queryDate) {
		if (commissioners == null || commissioners.isEmpty()) {
			return new ArrayList<T>();
		}
		return getRepository().find(QuerySettings.create(entityClass)
				.in(COMMISSIONER, commissioners)
				.le(CREATE_DATE, queryDate)
				.gt(ABOLISH_DATE, queryDate));
	}
	
	/**
	 * 列出指定的当事人作为responsible参加的责任关系,按责任发生时间的逆序排列。
	 * @param <T>
	 * @param entityClass
	 * @param responsible
	 * @return
	 */
	public static <T extends Accountability> List<T> findByResponsible(Class<T> entityClass, Party responsible) {
		return getRepository().find(QuerySettings.create(entityClass)
				.eq(RESPONSIBLE, responsible)
				.asc(CREATE_DATE));
	}
	
	/**
	 * 列出在指定日期指定的当事人作为responsible参加的责任关系,按责任发生时间的逆序排列。
	 * @param <T>
	 * @param entityClass
	 * @param responsible
	 * @param queryDate
	 * @return
	 */
	public static <T extends Accountability> List<T> findByResponsible(Class<T> entityClass, Party responsible, Date queryDate) {
		return getRepository().find(QuerySettings.create(entityClass)
				.eq(RESPONSIBLE, responsible)
				.le(CREATE_DATE, queryDate)
				.gt(ABOLISH_DATE, queryDate));
	}
	
	/**
	 * 列出在指定日期指定的当事人集合作为responsible参加的责任关系,按责任发生时间的逆序排列。
	 * @param <T>
	 * @param entityClass
	 * @param responsibles
	 * @param queryDate
	 * @return
	 */
	public static <T extends Accountability> List<T> findByResponsibles(Class<T> entityClass, Collection<? extends Party> responsibles, Date queryDate) {
		if (responsibles == null || responsibles.isEmpty()) {
			return new ArrayList<T>();
		}
		return getRepository().find(QuerySettings.create(entityClass)
				.in(RESPONSIBLE, responsibles)
				.le(CREATE_DATE, queryDate)
				.gt(ABOLISH_DATE, queryDate));
	}

	/**
	 * 查找在指定日期有指定当事人参与的责任关系，无论它作为commissioner方还是responsible方。
	 * 
	 * @param party
	 * @param queryDate
	 * @return 当前有当事人参与的责任关系的集合
	 */
	public static Set<Accountability> findByParty(Party party, Date queryDate) {
		Set<Accountability> results = new HashSet<Accountability>();
		results.addAll(findByResponsible(Accountability.class, party, queryDate));
		results.addAll(findByCommissioner(Accountability.class, party, queryDate));
		return results;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		
		if (!(other instanceof Accountability)) {
			return false;
		}
		Accountability that = (Accountability) other;
		return new EqualsBuilder()
			.append(this.getCreateDate(), that.getCreateDate())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getCreateDate())
			.toHashCode();
	}

	@Override
	public String toString() {
		return  "createDate: " + getCreateDate()
			+ ", abolishDate: " + getAbolishDate();
	}
}