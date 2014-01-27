package org.openkoala.auth.application;

import java.util.List;

import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;

import com.dayatang.querychannel.support.Page;

public interface ResourceApplication {

	
	/**
	 * 判断资源是否为空
	 * @return
	 */
	public boolean isResourceEmpty();
	
	
	/**
	 * 根据资源ID获取资源信息
	 * @param resourceId
	 * @return
	 */
    ResourceVO getResource(Long resourceId);
    
    /**
     * 根据条件查询对于资源列表
     * @param params  查询条件
     * @param loadChildNode  是否加载子节点
     * @return
     */
    List<ResourceVO> findResource(ResourceVO params,boolean loadChildNode);
    
    /**
     * 查询指定资源的一级子节点
     * @param resourceId
     * @return
     */
    List<ResourceVO> loadFirstStageResource(Long resourceId);

    /**
     * 保存资源
     * @param resourceVO
     * @return
     */
    ResourceVO saveResource(ResourceVO resourceVO);

    /**
     * 修改资源
     * @param resourceVO
     */
    void updateResource(ResourceVO resourceVO);

    /**
     * 根据ID删除资源
     * @param id
     */
    void removeResource(long id);

    /**
	 * 查询所有资源
	 */
    List<ResourceVO> findAllResource();

    /**
     * 查找资源被分配的角色 
     * @param resourceVO
     * @return
     */
    List<RoleVO> findAllRoleByResource(ResourceVO resourceVO);

    /**
     * 为子资源指定父资源
     * @param parentVO
     * @param childVO
     */
    void assign(ResourceVO parentVO, ResourceVO childVO);

    /**
     * 查找资源树
     * @return
     */
    List<ResourceVO> findResourceTree();
    
    /**
     * 资源名称是否已经存在
     * @return
     */
    boolean isNameExist(ResourceVO resourceVO);
    
    /**
     * 资源标识是否已经存在
     * @return
     */
    boolean isIdentifierExist(ResourceVO resourceVO);

    /**
     * 分页查询没有被分配的角色
     * @param currentPage
     * @param pageSize
     * @param roleVO
     * @return
     */
    Page<ResourceVO> pageQueryNotAssignByRole(int currentPage, int pageSize, RoleVO roleVO);

    /**
     * 保存资源并指定父资源
     * @param resourceVO
     * @param parent
     * @return
     */
    ResourceVO saveAndAssignParent(ResourceVO resourceVO, ResourceVO parent);
    
    /**
     * 初始化资源
     * @param type
     */
    void initMenus(String type,List<String> inits);
    
}
