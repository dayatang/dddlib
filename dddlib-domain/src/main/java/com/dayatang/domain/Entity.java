package com.dayatang.domain;

import java.io.Serializable;

/**
 * 领域实体接口。所有实体类都要直接或间接实现这个接口。它主要起标记作用，以便于统一处理系统中的实体等。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 * 
 */
public interface Entity extends Serializable {

	/**
	 * 取得实体的Id。实体类的每个实例都必须有个唯一Id以标识自身。
	 * 实体Id必须是可序列化的。
	 * @return 实体实例的 Id.
	 */
	Serializable getId();
	
	/**
	 * 是否新建实体（未持久化到数据库）
	 * @return
	 */
	boolean isNew();
	
	/**
	 * 是否在数据库中已经存在
	 * @return
	 */
	boolean existed();
	
	/**
	 * 是否在数据库中不存在
	 * @return
	 */
	boolean notExisted();
	
	/**
	 * 是否在数据库中已经存在属性为指定值的实体
	 * @return
	 */
	boolean existed(String propertyName, Object propertyValue);
}
