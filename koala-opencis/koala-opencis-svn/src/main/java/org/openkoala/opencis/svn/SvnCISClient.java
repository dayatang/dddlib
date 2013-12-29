package org.openkoala.opencis.svn;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.CommandExecutor;
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
import org.openkoala.opencis.svn.command.SvnAuthzCommand;
import org.openkoala.opencis.svn.command.SvnClearProjectPasswdFileContentCommand;
import org.openkoala.opencis.svn.command.SvnCommand;
import org.openkoala.opencis.svn.command.SvnCreateGroupAndAddGroupUsersCommand;
import org.openkoala.opencis.svn.command.SvnCreateProjectCommand;
import org.openkoala.opencis.svn.command.SvnCreateUserCommand;
import org.openkoala.opencis.svn.command.SvnIsUserExistenceCommand;
import org.openkoala.opencis.svn.command.SvnRemoveProjectCommand;

import com.dayatang.configuration.Configuration;
import com.trilead.ssh2.Connection;

/**
 * svn的CISClient实现类<br>
 */
public class SvnCISClient implements CISClient {
	
	private final String CONFIGURATION_KEY_HOST = "HOST";
	private final String CONFIGURATION_KEY_USER = "USER";
	private final String CONFIGURATION_KEY_PASSWORD = "PASSWORD";

	private Configuration configuration;
	
	private Connection conn;
	
	private CommandExecutor executor = new CommandExecutor();
	
	/**执行结果*/
	private boolean success = false;
	
	protected void setConn(Connection conn) {
		this.conn = conn;
	}

	public SvnCISClient() {
		
	}
	
	public SvnCISClient(Configuration configuration) {
		this.configuration = configuration;
		isConfigurationNotBlank();
	}
	
	private boolean isConfigurationNotBlank(){
		if(StringUtils.isBlank(configuration.getString(CONFIGURATION_KEY_HOST))){
			throw new HostBlankException("配置信息HOST不能为空！");
		}
		if(StringUtils.isBlank(configuration.getString(CONFIGURATION_KEY_USER))){
			throw new UserBlankException("配置信息USER不能为空！");
		}
		if(StringUtils.isBlank(configuration.getString(CONFIGURATION_KEY_PASSWORD))){
			throw new PasswordBlankException("配置信息PASSWORD不能为空！");
		}
		return true;
	}
	
	@Override
	public void createProject(Project project) {
		isProjectInfoNotBlank(project);
		SvnCommand command = new SvnCreateProjectCommand(configuration,project);
		try {
			executor.executeSync(command);
			
			SvnCommand clearProjectPasswdFileContentCommand = new SvnClearProjectPasswdFileContentCommand(configuration, project);
			success = executor.executeSync(clearProjectPasswdFileContentCommand);
		} catch (ProjectExistenceException e) {
			throw e;
		}catch (Exception e) {
			throw new CreateProjectException("创建项目异常",e);
		}
	}

	@Override
	public void createUserIfNecessary(Project project,Developer developer) {
		isProjectInfoNotBlank(project);
		isUserInfoBlank(developer);
		SvnCommand isUserExistencecommand = new SvnIsUserExistenceCommand(developer.getName(), configuration, project);
		try {
			executor.executeSync(isUserExistencecommand);
			
			SvnCommand createUsercommand = new SvnCreateUserCommand(developer, configuration, project);
			success = executor.executeSync(createUsercommand);
		}catch (UserExistenceException e) {
			//用户存在，则不再创建用户
			return;
		}catch (Exception e) {
			throw new CreateUserException("创建用户异常",e);
		}
	}
	
	private boolean isProjectInfoNotBlank(Project project){
		if(StringUtils.isBlank(project.getProjectName())){
			throw new ProjectBlankException("项目名不能为空！");
		}
		return true;
	}
	
	private boolean isUserInfoBlank(Developer developer){
		if(StringUtils.isBlank(developer.getName())){
			throw new UserBlankException("用户名不能为空！");
		}
		if(StringUtils.isBlank(developer.getPassword())){
			throw new PasswordBlankException("密码不能为空！");
		}
		return true;
	}
	
	

	@Override
	public void createRoleIfNecessary(Project project, String roleName) {
		//svn创建角色和角色授权是一起执行的，即在角色授权时同时创建角色，因此这里不单独实现创建角色
	}

	@Override
	public void assignUserToRole(Project project,String userName, String role) {
		//使用assignUsersToRole方法来实现
	}
	
	@Override
	/**
	 * 该授权分两步：
	 * 1. 创建角色并为用户分配角色
	 * 2. 为角色授予读写权限
	 */
	public void assignUsersToRole(Project project, List<String> userNames, String role) {
		isAuthInfoNotBlank(project, userNames, role);
		createGroupAndAddGroupUsers(project, userNames, role);
		authz(project, role);
	}
	
	private boolean isAuthInfoNotBlank(Project project, List<String> userNames, String role){
		isProjectInfoNotBlank(project);
		isRoleNotBlank(role);
		isUserListNotBlank(userNames);
		return true;
	}

	private boolean isRoleNotBlank(String role){
		if(StringUtils.isBlank(role)){
			throw new RoleBlankException("角色不能为空！");
		}
		return true;
	}
	
	private boolean isUserListNotBlank(List<String> userNames){
		if(userNames==null || userNames.isEmpty()){
			throw new UserListBlankException("用户列表不能为空！");
		}
		return true;
	}

	private void createGroupAndAddGroupUsers(Project project, List<String> userNames, String role) {
		SvnCommand command = new SvnCreateGroupAndAddGroupUsersCommand(userNames, role, configuration, project);
		try {
			success = executor.executeSync(command);
		} catch (Exception e) {
			throw new CreateUserGroupException("创建用户组异常",e);
		}
		
	}
	
	private void authz(Project project, String role) {
		SvnCommand command = new SvnAuthzCommand(role, configuration, project);
		try {
			success = executor.executeSync(command);
		} catch (Exception e) {
			throw new RemoveProjectException("删除项目异常",e);
		}
		
	}
	
	protected void removeProjcet(Project project){
		SvnCommand command = new SvnRemoveProjectCommand(configuration,project);
		try {
			success = executor.executeSync(command);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean canConnect() {
		return connectToHost();
	}
	
	private boolean connectToHost(){
		try {
			if(conn != null){
				return true;
			}
			conn = new Connection(configuration.getString(CONFIGURATION_KEY_HOST));
			conn.connect();
			//登陆linux
			boolean isAuthenticated =  conn.authenticateWithPassword(configuration.getString(CONFIGURATION_KEY_USER),
					configuration.getString(CONFIGURATION_KEY_PASSWORD));
			if(!isAuthenticated){
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

}
