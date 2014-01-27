package org.openkoala.auth.application;

import java.util.List;

import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;

import com.dayatang.querychannel.support.Page;

public interface UserApplication {

    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return
     */
    UserVO getUser(Long userId);

    /**
     * 保存用户
     * @param userVO
     * @return
     */
    UserVO saveUser(UserVO userVO);

    /**
     * 修改用户
     * @param userVO
     */
    void updateUser(UserVO userVO);

    /**
     * 删除用户
     * @param userId
     */
    void removeUser(Long userId);

    /**
     * 查询所有用户
     * @return
     */
    List<UserVO> findAllUser();

    /**
     * 分页查询用户
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<UserVO> pageQueryUser(int currentPage, int pageSize);

    /**
     * 自定义分页查询用户
     * @param currentPage
     * @param pageSize
     * @param query
     * @return
     */
    Page<UserVO> pageQueryUserCustom(int currentPage, int pageSize, QueryConditionVO query);

    /**
     * 根据登录帐户查询用户信息
     * @param userAccount
     * @return
     */
    UserVO findByUserAccount(String userAccount);

    /**
     * 为用户分配角色
     * @param userVO
     * @param roleVO
     */
    void assignRole(UserVO userVO, RoleVO roleVO);

    /**
     * 为用户分配角色(批量)
     * @param userVO
     * @param roleVOs
     */
    void assignRole(UserVO userVO, List<RoleVO> roleVOs);

    /**
     * 重置用户密码
     * @param userVO
     */
    void resetPassword(UserVO userVO);

    /**
     * 废除角色
     * @param userVO
     * @param roles
     */
    void abolishRole(UserVO userVO, List<RoleVO> roles);

    /**
     * 根据用户查询没有被分配的角色 
     * @param currentPage
     * @param pageSize
     * @param userVO
     * @return
     */
    Page<RoleVO> pageQueryNotAssignRoleByUser(int currentPage, int pageSize, UserVO userVO);

    /**
	 * 更新用户密码
	 * @param userVO
	 * @param oldPass
	 * @return
	 */
    boolean updatePassword(UserVO userVO, String oldPass);
    
    /**
     * 修改用户最后登录时间
     * @param useraccount
     */
    void modifyLastLoginTime(String useraccount);
    
    /**
     * 根据用户账号查找用户邮箱
     * @param email
     * @return
     */
    UserVO findByEmail(String email);
    
}
