package org.openkoala.opencis.api;

/**
 * CIS的能用接口
 *
 * @author lingen
 */
public interface CISClient {


    /**
     * 创建CIS某个工具中创建一个项目
     *
     * @param project
     */
    void createProject(Project project);

    /**
     * 删除
     *
     * @param project
     */
    void removeProject(Project project);

    /**
     * 在CIS某个工具中创建一个用户，如果此用户不存在的话
     *
     * @param developer
     */
    void createUserIfNecessary(Project project, Developer developer);

    /**
     * 删除用户
     *
     * @param developer
     */
    void removeUser(Project project, Developer developer);


    /**
     * 在 CIS 某个工具中创建一个角色，如果此角色不存在
     *
     * @param roleName
     */
    void createRoleIfNecessary(Project project, String roleName);


    /**
     * 在 CIS 某个工具中将角色同某个用户列表关联
     */
    void assignUsersToRole(Project project, String role, Developer... developers);


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


}
