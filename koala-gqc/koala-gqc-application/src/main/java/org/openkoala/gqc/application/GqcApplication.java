package org.openkoala.gqc.application;

import java.util.Set;

import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.GeneralQueryEntity;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.querychannel.support.Page;

public interface GqcApplication {

	/**
	 * 获取通用查询实例
	 * @param clazz
	 * @param id 通用查询主键
	 * @return
	 */
	public <T extends GeneralQueryEntity> T getEntity(Class<T> clazz, Long id);
	
	/**
	 * 保存用查询实例
	 * @param entity
	 */
	public void saveEntity(GeneralQueryEntity entity);
	
	/**
	 * 更新用查询实例
	 * @param entity
	 */
	public void updateEntity(GeneralQueryEntity entity);

	/**
	 * 删除用查询实例
	 * @param entity
	 */
	public void removeEntity(GeneralQueryEntity entity);
	
	/**
	 * 批量删除用查询实例
	 * @param entities
	 */
	public <T extends GeneralQueryEntity> void removeEntities(Set<T> entities);
	
	/**
	 * 分页查询用查询实例
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	public Page<GeneralQuery> pagingQueryGeneralQueries(int currentPage, int pagesize);
	
	/**
	 * 获取通用查询实例
	 * @param id 主键
	 * @return
	 */
	public GeneralQuery getById(Long id);
	
	/**
	 * 分页查询用查询实例
	 * @param queryName 查询器名称
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	public Page<GeneralQuery> pagingQueryGeneralQueriesByQueryName(String queryName, int currentPage, int pagesize);
	
}
