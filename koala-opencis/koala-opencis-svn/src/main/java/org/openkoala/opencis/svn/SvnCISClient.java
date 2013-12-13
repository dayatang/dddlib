package org.openkoala.opencis.svn;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.CommandExecutor;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.svn.command.SvnAssignUserToRoleCommand;
import org.openkoala.opencis.svn.command.SvnCommand;
import org.openkoala.opencis.svn.command.SvnCreateProjectCommand;
import org.openkoala.opencis.svn.command.SvnCreateRoleCommand;
import org.openkoala.opencis.svn.command.SvnCreateUserCommand;
import org.openkoala.opencis.svn.command.SvnIsUserExistenceCommand;
import org.openkoala.opencis.svn.command.SvnRemoveProjectCommand;
import org.openkoala.opencis.svn.command.SvnClearProjectPasswdFileContentCommand;

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
		isConfigurationCorrect();
	}
	
	protected boolean isConfigurationCorrect(){
		if(StringUtils.isBlank(configuration.getString(CONFIGURATION_KEY_HOST))){
			throw new HostBlankException("配置信息HOST不能为空！");
		}
		if(StringUtils.isBlank(configuration.getString(CONFIGURATION_KEY_USER))){
			throw new UserBlankException("配置信息USER不能为空！");
		}
		if(StringUtils.isBlank(configuration.getString(CONFIGURATION_KEY_PASSWORD))){
			throw new PasswordBlankException("配置信息PASSWORD不能为空！");
		}
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

	@Override
	public void createProject(Project project) {
		isProjectInfoBlank(project);
		SvnCommand command = new SvnCreateProjectCommand(configuration,project);
		try {
			success = executor.executeSync(command);
			
			SvnCommand clearProjectPasswdFileContentCommand = new SvnClearProjectPasswdFileContentCommand(configuration, project);
			executor.executeSync(clearProjectPasswdFileContentCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createUserIfNecessary(Project project,Developer developer) {
		isProjectInfoBlank(project);
		isUserInfoBlank(developer);
		SvnCommand isUserExistencecommand = new SvnIsUserExistenceCommand(developer.getName(), configuration, project);
		try {
			executor.executeSync(isUserExistencecommand);
			
			SvnCommand createUsercommand = new SvnCreateUserCommand(developer, configuration, project);
			executor.executeSync(createUsercommand);
		}catch (UserExistenceException e) {
			//用户存在，则不再创建用户
			return;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isProjectInfoBlank(Project project){
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
	public void createRoleIfNessceary(Project project,String roleName) {
		//svn创建角色和角色授权是一起执行的，即在角色授权时同时创建角色，因此这里不单独实现创建角色
	}

	@Override
	/**
	 * 该授权分两步：
	 * 1. 创建角色并为用户分配角色
	 * 2. 为角色授予读写权限
	 */
	public void assignUserToRole(Project project,String usrId, String role) {
		SvnCommand command = new SvnAssignUserToRoleCommand(usrId, role, configuration, project);
		try {
			success = executor.executeSync(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void removeProjcet(Project project){
		SvnCommand command = new SvnRemoveProjectCommand(configuration,project);
		try {
			success = executor.executeSync(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean canConnect() {
		try {
			return connectToHost();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
