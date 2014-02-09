package org.openkoala.auth.application; 

import java.util.List;

import org.openkoala.auth.application.vo.ResourceTypeVO;

import com.dayatang.querychannel.support.Page;

/**
 * 资源类型应用层接口
 * @author Ken
 * @since 2013-03-12 10:40
 *
 */
public interface ResourceTypeApplication {

	/**
	 * 判断资源类型是否存在
	 * @param resourceTypeVO
	 * @return
	 */
	boolean isExist(ResourceTypeVO resourceTypeVO);
	
	/**
	 * 保存资源类型
	 * @param resourceTypeVO
	 */
	void save(ResourceTypeVO resourceTypeVO);
	
	/**
	 * 删除资源类型
	 * @param resourceTypeVO
	 */
	void delete(ResourceTypeVO resourceTypeVO);
	
	/**
	 * 批量删除资源类型
	 * @param resourceTypeVOs
	 */
	void delete(ResourceTypeVO[] resourceTypeVOs);
	
	/**
	 * 更新资源类型
	 * @param resourceTypeVO
	 */
	void update(ResourceTypeVO resourceTypeVO);
	
	/**
	 * 分页查询资源类型
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Page<ResourceTypeVO> pageQuery(int currentPage, int pageSize);
	
	/**
	 * 查找所有资源类型
	 * @return
	 */
	List<ResourceTypeVO> findResourceType();
	
	/**
	 * 查找菜单类型
	 * @return
	 */
	List<ResourceTypeVO> findMenuType();
}
