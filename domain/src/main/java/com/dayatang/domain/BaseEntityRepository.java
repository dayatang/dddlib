package com.dayatang.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 具体实体的仓储访问接口。专门用于从仓储中访问指定的实体类型。
 * 
 */
@SuppressWarnings("rawtypes")
@Deprecated
public interface BaseEntityRepository<T extends Entity, ID extends Serializable> {

	/**
	 * 将实体（无论是新的还是修改了的）保存到仓储中。
	 * 
	 * @param entity
	 *            要存储的实体实例。
	 */
	T save(T entity);

	/**
	 * 将实体从仓储中删除。如果仓储中不存在此实例将抛出EntityNotExistedException异常。
	 * 
	 * @param entity
	 *            要删除的实体实例。
	 */
	void remove(T entity);

	/**
	 * 判断仓储中是否存在指定ID的实体实例。
	 * 
	 * @param id
	 *            实体标识
	 * @return 如果实体实例存在，返回true，否则返回false
	 */
	boolean exists(ID id);

	/**
	 * 从仓储中获取指定类型、指定ID的实体
	 * 
	 * @param id
	 *            实体标识
	 * @return 一个实体实例。
	 */
	T get(ID id);

	/**
	 * 从仓储中装载指定类型、指定ID的实体
	 * 
	 * @param id
	 *            实体标识
	 * @return 一个实体实例。
	 */
	T load(ID id);

	/**
	 * 从仓储中获取entity参数所代表的未修改的实体
	 * 
	 * @param entity
	 *            要查询的实体
	 * @return 参数entity在仓储中的未修改版本
	 */
	T getUnmodified(T entity);

	/**
	 * 从仓储中查找指定类型的实体集合
	 * 
	 * @return 实体实例的集合。
	 */
	List<T> findAll();

	/**
	 * 从仓储中查找指定类型的实体集合
	 * 
	 * @param firstResult
	 *            第一个实例的位置。
	 * @param maxResults
	 *            要获取的实例总数。
	 * @return 实体实例的集合。
	 */
	List<T> findAll(int firstResult, int maxResults);

	/**
	 * 根据ID从仓储中取得指定类型的实体实例。
	 * 
	 * @param <E>
	 *            实体的类型
	 * @param entityClass
	 *            实体的类
	 * @param id
	 *            标识属性
	 * @return 类型为entityClass, ID为id的一个实体实例。
	 */
	<E extends Entity> E get(Class<E> entityClass, ID id);

	/**
	 * 从仓储中查找指定类型的实体集合
	 * 
	 * @return 实体实例的集合。
	 */
	List<T> findAll(Class<T> entityClass);

	/**
	 * 从仓储中查找指定类型的实体集合
	 * 
	 * @param firstResult
	 *            第一个实例的位置。
	 * @param maxResults
	 *            要获取的实例总数。
	 * @return 实体实例的集合。
	 */
	List<T> findAll(Class<T> entityClass, int firstResult, int maxResults);

	/**
	 * 根据查询语句和指定的参数从仓储中查询符合条件的实体集合
	 * 
	 * @param <E>
	 *            实体类型
	 * @param queryString
	 *            访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params
	 *            查询参数，以定位参数的形式代入queryString中的问号占位符
	 * @return 符合查询条件的实体的集合.
	 */
	List find(String queryString, Object[] params);

	/**
	 * 根据查询语句和指定的参数从仓储中查询符合条件的实体集合
	 * 
	 * @param <E>
	 *            要查询的目标实体的类型
	 * @param queryString
	 *            访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params
	 *            查询参数，以命名参数的形式代入queryString中的占位符
	 * @return 符合查询条件的实体的集合.
	 */
	List find(String queryString,
			Map<String, Object> params);

	/**
	 * 根据命名的查询和指定的参数从仓储中查询符合条件的实体集合
	 * 
	 * @param <E>
	 *            要查询的目标实体的类型
	 * @param queryName
	 *            命名查询的名字。
	 * @param params
	 *            查询参数，以定位参数的形式代入queryString中的问号占位符
	 * @return 符合查询条件的实体的集合.
	 */
	List findByNamedQuery(String queryName,
			Object[] params);

	/**
	 * 根据命名的查询和指定的参数从仓储中查询符合条件的实体集合
	 * 
	 * @param <E>
	 *            要查询的目标实体的类型
	 * @param queryName
	 *            命名查询的名字。
	 * @param params
	 *            查询参数，以命名参数的形式代入queryString中的占位符
	 * @return 符合查询条件的实体的集合.
	 */
	List findByNamedQuery(String queryName,
			Map<String, Object> params);

	/**
	 * 根据查询语句和指定的参数访问仓储，返回单一结果。
	 * 
	 * @param queryString
	 *            访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params
	 *            查询参数，以定位参数的形式代入queryString中的问号占位符
	 * @return 查询的单一结果
	 */
	Object getSingleResult(String queryString, Object[] params);

	/**
	 * 根据查询语句和指定的参数访问仓储，返回单一结果。
	 * 
	 * @param queryString
	 *            访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params
	 *            查询参数，以命名参数的形式代入queryString中的占位符
	 * @return 查询的单一结果
	 */
	Object getSingleResult(String queryString, Map<String, Object> params);

	/**
	 * 执行更新仓储的操作。
	 * 
	 * @param queryString
	 *            访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params
	 *            查询参数，以定位参数的形式代入queryString中的问号占位符
	 */
	void executeUpdate(String queryString, Object[] params);

	/**
	 * 执行更新仓储的操作。
	 * 
	 * @param queryString
	 *            访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params
	 *            查询参数，以命名参数的形式代入queryString中的占位符
	 */
	void executeUpdate(String queryString, Map<String, Object> params);

}
