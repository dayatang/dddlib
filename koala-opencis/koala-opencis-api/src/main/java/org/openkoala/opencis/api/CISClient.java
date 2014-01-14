package org.openkoala.opencis.api;

import java.util.List;

/**
 * CIS的能用接口
 *
 * @author lingen
 */
public interface CISClient {


    /**
     * 认证
     *
     * @return
     */
    boolean authenticate();


    /**
     * 关闭与具体工具的连接
     */
    void close();

    /**
     * 返回错误
     *
     * @return
     */
    String getErrors();


    /**
     * 创建CIS某个工具中创建一个项目
     *
     * @param project
     */
    boolean createProject(Project project);

    /**
     * 在CIS某个工具中创建一个用户，如果此用户不存在的话
     *
     * @param developer
     */
    boolean createUserIfNecessary(Project project, Developer developer);

    /**
     * 在 CIS 某个工具中创建一个角色，如果此角色不存在
     *
     * @param roleName
     */
    boolean createRoleIfNecessary(Project project, String roleName);

    /**
     * 在 CIS 某个工具中将角色同某个用户关联
     *
     * @param project
     * @param userId
     * @param role
     * @return
     */
    boolean assignUserToRole(Project project, String userId, String role);

    /**
     * 在 CIS 某个工具中将角色同某个用户列表关联
     *
     * @param project
     * @param userName
     * @param role
     * @return
     */
    boolean assignUsersToRole(Project project, List<String> userName, String role);


}
