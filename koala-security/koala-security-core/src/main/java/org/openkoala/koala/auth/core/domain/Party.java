/**
 * 
 */
package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import com.dayatang.domain.QuerySettings;

/**
 * "当事人"实体类。是机构（Organization）、岗位（Post）和员工（Employee）的共同基类。
 * “当事人”是系统核心类型，一个非常关键的抽象。
 * 在一个确定的时间点，serialNumber必须是唯一的。即使是不同类型的当事人，也不允许有相同的serialNumber。
 * 
 * @author yyang
 * 
 */
@MappedSuperclass
public abstract class Party extends TimeIntervalEntity {

	private static final long serialVersionUID = 4024294648318993439L;
	
	@Column(name="NAME",nullable = false)
	//@Size(min = 1, message = "name.is.empty")
	private String name;

	@Column(name = "SERIAL_NUMBER")
	//@Size(min = 1, message = "serialNumber.is.empty")
	private String serialNumber;

	@Column(name = "SORT_ORDER")
	private int sortOrder;

	public Party() {
	}

	public Party(String name, String serialNumber) {
		this.name = name;
		this.serialNumber = serialNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public void validate() {
		super.validate();
	}

	/**
	 * 根据标识从仓储中获取实体
	 * @param id
	 * @return
	 */
	public static Party get(Long id) {
		return getRepository().get(Party.class, id);
	}

	/**
	 * 根据编号从仓储中获取实体
	 * @param serialNumber
	 * @return
	 */
	public static <T extends Party> T getBySerialNumber(Class<T> partyClass, String serialNumber, Date queryDate) {
		return getRepository().getSingleResult(QuerySettings.create(partyClass)
				.eq("serialNumber", serialNumber)
				.le("createDate", queryDate)
				.gt("abolishDate", queryDate));
	}

	/**
	 * 在指定的日期撤销当事人。对机构或岗位是撤销，对员工是离职或退休。
	 * @param abolishDate
	 */
	public void abolish(Date abolishDate) {
		if (abolishDate == null) {
			throw new IllegalArgumentException("abolishDate.isNull");
		}
		if (isAbolished(abolishDate)) {
			return;
		}
		for (Accountability accountability : Accountability.findByParty(this, abolishDate)) {
			accountability.abolish(abolishDate);
		}
		super.abolish(abolishDate);
	}
	
	/**
	 * 根据名称从仓储中获取实体
	 * @param serialNumber
	 * @return
	 */
	public static <T extends Party> T findByName(Class<T> partyClass, String name) {
		return getRepository().getSingleResult(QuerySettings.create(partyClass)
				.eq("Name", name));
	}


	@Override
	public String toString() {
		return "[" + serialNumber + "]" + name;
	}
}

