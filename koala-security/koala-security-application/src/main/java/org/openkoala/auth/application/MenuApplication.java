package org.openkoala.auth.application;

import java.util.List;

import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;

import com.dayatang.querychannel.support.Page;

public interface MenuApplication {

	/**
	 * 根据菜单ID获取菜单信息
	 * @param menuId
	 * @return
	 */
    ResourceVO getMenu(Long menuId);

    /**
     * 保存菜单 
     * @param menuVO
     * @return
     */
    ResourceVO saveMenu(ResourceVO menuVO);

    /**
     * 保存菜单并且指定父级菜单
     * @param menuVO	当前菜单 
     * @param parent	父级菜单
     * @return
     */
    ResourceVO saveAndAssignParent(ResourceVO menuVO, ResourceVO parent);

    /**
     * 修改菜单 
     * @param menuVO
     */
    void updateMenu(ResourceVO menuVO);

    /**
     * 根据ID删除菜单
     * @param menuId
     */
    void removeMenu(Long menuId);

    /**
     * 查找所有菜单
     * @return
     */
    List<ResourceVO> findAllMenu();

    /**
     * 分页查询菜单
     * @param currentPage	当前页
     * @param pageSize		页大小
     * @return
     */
    Page<ResourceVO> pageQueryMenu(int currentPage, int pageSize);

    /**
     * 为子菜单指定父菜单 
     * @param parent
     * @param child
     */
    void assign(ResourceVO parent, ResourceVO child);

    /**
     * 查找菜单树
     * @return
     */
    List<ResourceVO> findMenuTree();

    /**
     * 获取用户的一级目录
     * @param userAccount
     * @return
     */
    public List<ResourceVO> findTopMenuByUser(String userAccount);
    /**
     * 根据用户账号查找所有菜单
     * @param userAccount
     * @return
     */
    List<ResourceVO> findAllMenuByUser(String userAccount);

    /**
     * 根据用户账号和父菜单查找所有子菜单
     * @param menuVO
     * @param userAccount
     * @return
     */
    List<ResourceVO> findAllChildByParentAndUser(ResourceVO menuVO, String userAccount);

    /**
     * 根据用户账号和父菜单查找子菜单
     * @param menuVO
     * @param userAccount
     * @return
     */
    List<ResourceVO> findChildByParentAndUser(ResourceVO menuVO, String userAccount);

    /**
     * 根据角色查找子菜单并带选中状态
     * @param parent
     * @param roleVO
     * @return
     */
    List<ResourceVO> findChildSelectItemByRole(ResourceVO parent, RoleVO roleVO);

    /**
     * 根据角色查找菜单树并带选中状态
     * @param roleVO
     * @return
     */
    List<ResourceVO> findAllTreeSelectItemByRole(RoleVO roleVO);

    /**
     * 更新菜单排序号
     * @param resourceVOs
     */
	void updateMeunOrder(List<ResourceVO> resourceVOs);
}
