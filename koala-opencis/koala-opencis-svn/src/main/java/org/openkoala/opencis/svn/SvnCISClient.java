package org.openkoala.opencis.svn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.exception.CreateProjectException;
import org.openkoala.opencis.exception.CreateUserException;
import org.openkoala.opencis.exception.CreateUserGroupException;
import org.openkoala.opencis.exception.HostBlankException;
import org.openkoala.opencis.exception.HostCannotConnectException;
import org.openkoala.opencis.exception.PasswordBlankException;
import org.openkoala.opencis.exception.ProjectBlankException;
import org.openkoala.opencis.exception.ProjectExistenceException;
import org.openkoala.opencis.exception.RemoveProjectException;
import org.openkoala.opencis.exception.RoleBlankException;
import org.openkoala.opencis.exception.UserBlankException;
import org.openkoala.opencis.exception.UserExistenceException;
import org.openkoala.opencis.exception.UserListBlankException;
import org.openkoala.opencis.exception.UserOrPasswordErrorException;
import org.openkoala.opencis.support.CommandExecutor;
import org.openkoala.opencis.support.SSHConnectConfig;
import org.openkoala.opencis.svn.command.CheckExistsAuthCommand;
import org.openkoala.opencis.svn.command.CheckExistsUserGroupCommand;
import org.openkoala.opencis.svn.command.SvnAuthzCommand;
import org.openkoala.opencis.svn.command.SvnClearProjectPasswdFileContentCommand;
import org.openkoala.opencis.svn.command.SvnCommand;
import org.openkoala.opencis.svn.command.SvnCreateAuthFileCommand;
import org.openkoala.opencis.svn.command.SvnCreateGroupAndAddGroupUsersCommand;
import org.openkoala.opencis.svn.command.SvnCreateProjectCommand;
import org.openkoala.opencis.svn.command.SvnCreateUserCommand;
import org.openkoala.opencis.svn.command.SvnIsUserExistenceCommand;
import org.openkoala.opencis.svn.command.SvnRemoveProjectCommand;
import org.openkoala.opencis.svn.command.SvnRemoveUserCommand;

import com.trilead.ssh2.Connection;

/**
 * svn的CISClient实现类<br>
 */
public class SvnCISClient implements CISClient {

    private final String CONFIGURATION_KEY_HOST = "HOST";
    private final String CONFIGURATION_KEY_USER = "USER";
    private final String CONFIGURATION_KEY_PASSWORD = "PASSWORD";

    private SSHConnectConfig configuration;

    private Connection conn;

    private CommandExecutor executor = new CommandExecutor();

    /**
     * 执行结果
     */
    private boolean success = false;
    private String errors;

    protected void setConn(Connection conn) {
        this.conn = conn;
    }

    private SvnCISClient() {

    }

    public SvnCISClient(SSHConnectConfig configuration) {
        this.configuration = configuration;
        isConfigurationNotBlank();
    }

    private boolean isConfigurationNotBlank() {
        if (StringUtils.isBlank(configuration.getHost())) {
            throw new HostBlankException("配置信息HOST不能为空！");
        }
        if (StringUtils.isBlank(configuration.getUsername())) {
            throw new UserBlankException("配置信息USER不能为空！");
        }
        if (StringUtils.isBlank(configuration.getPassword())) {
            throw new PasswordBlankException("配置信息PASSWORD不能为空！");
        }
        return true;
    }

    @Override
    public void close() {
        // do nothing
//    	if(conn!=null){
//    		conn.close();
//    		conn=null;
//    	}
    }

    @Override
    public void createProject(Project project) {
        isProjectInfoNotBlank(project);
        SvnCommand command = new SvnCreateProjectCommand(configuration, project);
        SvnCommand clearProjectPasswdFileContentCommand = new SvnClearProjectPasswdFileContentCommand(configuration, project);
        SvnCommand createAuthzFileCommand = new SvnCreateAuthFileCommand(configuration, project);
        try {
            executor.addCommand(command);
            executor.addCommand(clearProjectPasswdFileContentCommand);
            executor.addCommand(createAuthzFileCommand);
            success = executor.executeBatch();
        } catch (ProjectExistenceException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateProjectException("创建项目异常", e);
        }

    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        isProjectInfoNotBlank(project);
        isUserInfoBlank(developer);
        SvnCommand isUserExistencecommand = new SvnIsUserExistenceCommand(developer.getId(),
                configuration, project);
        try {
            executor.executeSync(isUserExistencecommand);

            SvnCommand createUsercommand = new SvnCreateUserCommand(developer, configuration, project);
            success = executor.executeSync(createUsercommand);
        } catch (UserExistenceException e) {
            //用户存在，则不再创建用户
//            return true;
        } catch (Exception e) {
            throw new CreateUserException("创建用户异常，原因：" + e.getMessage(), e);
        }
//        return false;
    }

    private boolean isProjectInfoNotBlank(Project project) {
        if (StringUtils.isBlank(project.getProjectName())) {
            throw new ProjectBlankException("项目名不能为空！");
        }
        return true;
    }

    private boolean isUserInfoBlank(Developer developer) {
        if (StringUtils.isBlank(developer.getId())) {
            throw new UserBlankException("用户名不能为空！");
        }
        if (StringUtils.isBlank(developer.getPassword())) {
            throw new PasswordBlankException("密码不能为空！");
        }
        return true;
    }


    public void createRoleIfNecessary(Project project, String roleName) {
        //svn创建角色和角色授权是一起执行的，即在角色授权时同时创建角色，因此这里不单独实现创建角色
    }

    public boolean assignUserToRole(Project project, String userName, String role) {
        //使用assignUsersToRole方法来实现
        return true;
    }

//    /**
//     * 该授权分两步：
//     * 1. 创建角色并为用户分配角色
//     * 2. 为角色授予读写权限
//     */
//    public boolean assignUsersToRole(Project project, List<String> userNames, String role) {
//        isAuthInfoNotBlank(project, userNames, role);
//        createGroupAndAddGroupUsers(project, userNames, role);
//        authz(project, role);
//        return true;
//    }

    private boolean isAuthInfoNotBlank(Project project, List<String> userNames, String role) {
        isProjectInfoNotBlank(project);
        isRoleNotBlank(role);
        isUserListNotBlank(userNames);
        return true;
    }

    private boolean isRoleNotBlank(String role) {
        if (StringUtils.isBlank(role)) {
            throw new RoleBlankException("角色不能为空！");
        }
        return true;
    }

    private boolean isUserListNotBlank(List<String> userNames) {
        if (userNames == null || userNames.isEmpty()) {
            throw new UserListBlankException("用户列表不能为空！");
        }
        return true;
    }

    /**
     * 创建组合添加组用户到authz文件
     * @param project
     * @param userNames
     * @param role
     */
    private void createGroupAndAddGroupUsers(Project project, List<String> userNames, String role) {
        SvnCommand command = new SvnCreateGroupAndAddGroupUsersCommand(userNames, role, configuration, project);
        try {
            success = executor.executeSync(command);
        } catch (Exception e) {
            throw new CreateUserGroupException("创建用户组到Authz异常", e);
        }

    }
    
    /**
     * 检查是否重复配置用户-用户组
     * @param role
     * @param userNames
     * @param project
     * @param config
     * @return
     */
    private boolean checkExistsUserGroup(String role, List<String> userNames, Project project, SSHConnectConfig config){
    	SvnCommand command = new CheckExistsUserGroupCommand(userNames, role, config, project);
    	try {
			success = executor.executeSync(command);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

		return true;
    }
    
    /**
     * 检查是否重复授权
     * @param role
     * @param userNames
     * @param project
     * @param config
     * @return
     */
    
    private boolean checkExistsAuth(String role, List<String> userNames, Project project, SSHConnectConfig config){
    	SvnCommand command = new CheckExistsAuthCommand(userNames, role, config, project);
    	try {
			success = executor.executeSync(command);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

		return true;
    }

    private void authz(Project project, String role) {
        SvnCommand command = new SvnAuthzCommand(role, configuration, project);
        try {
            success = executor.executeSync(command);
        } catch (Exception e) {
            throw new RuntimeException("授权认证异常", e);
        }

    }

	@Override
	public void removeProject(Project project) {
        SvnCommand command = new SvnRemoveProjectCommand(configuration, project);
        try {
            success = executor.executeSync(command);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean canConnect() {
        return connectToHost();
    }

    private boolean connectToHost() {
        try {
            if (conn != null) {
                return true;
            }
            conn = new Connection(configuration.getHost());
            conn.connect();
            //登陆linux
            boolean isAuthenticated = conn.authenticateWithPassword(configuration.getUsername(),
                    configuration.getPassword());
            if (!isAuthenticated) {
                conn.close();
                conn = null;
                throw new UserOrPasswordErrorException("账号或密码错误！");
            }
            return true;
        } catch (IOException e) {
            conn.close();
            conn = null;
            throw new HostCannotConnectException("无法连接到主机！");
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


	@Override
	public void removeUser(Project project, Developer developer) {
		// TODO Auto-generated method stub
		SvnCommand command = new SvnRemoveUserCommand(developer, configuration, project);
		try {
            success = executor.executeSync(command);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	/**
	 * 分配多个用户到一个角色里
	 */
	@Override
	public void assignUsersToRole(Project project, String role,Developer... developers) {
		// TODO Auto-generated method stub
		List<String> userNames = new ArrayList<String>();
		
		for(Developer developer:developers){
			userNames.add(developer.getId());
		}
		
		//如果条件不满足，则流程结束
		if(!isAuthInfoNotBlank(project, userNames, role)){
			return ;
		}
		
		if(!checkExistsUserGroup(role, userNames, project, configuration)){
			return ;
		}
		
		boolean canGroupAuth= checkExistsAuth(role, userNames, project, configuration);
		
		SvnCommand cmdAddUsersGroupAuth = new SvnCreateGroupAndAddGroupUsersCommand(userNames, role, configuration, project);
		SvnCommand cmdAuthz = new SvnAuthzCommand(role, configuration, project);
		try {
			executor.addCommand(cmdAddUsersGroupAuth);
			if(canGroupAuth){
				executor.addCommand(cmdAuthz);
			}
			success = executor.executeBatch();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
//        createGroupAndAddGroupUsers(project, userNames, role);
//        authz(project, role);
	}

	@Override
	public boolean authenticate() {
		// TODO Auto-generated method stub
		return connectToHost();
	}

}
