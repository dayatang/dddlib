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
		//使用java SSH来创建项目
		//1、先检测项目是否存在，如果存在则不需要创建
		//2、用命令CommandExecutor来执行TracCreateProjecCommand子类
		//初始化命令
		SvnCommand command = new SvnCreateProjectCommand(configuration,project);
		success = executor.executeSync(command);
		
	}

	@Override
	public void createUserIfNecessary(Project project,Developer developer) {
		//Trac在创建用户时就已经指派了角色了，所以，这里不需要执行了 
	}

	@Override
	public void createRoleIfNessceary(Project project,String roleName) {
		//使用java SSH来创建角色
		//1、读取project的配置信息，包括该角色(用户组)默认的权限
		//2、用命令CommandExecutor来执行TracCreateRoleCommand子类
		SvnCommand command = new SvnCreateRoleCommand(configuration, roleName, project);
		success = executor.executeSync(command);
	}

	@Override
	public void assignUserToRole(Project project,String usrId, String role) {
		//使用java SSH来分配用户到某个角色，如果是连续分配，个人认为不应该关闭Connection，直到循环完毕才close
		//1、读取project的配置信息
		//2、用命令CommandExecutor来执行TracAssignUserToRoleCommand子类
		SvnCommand command = new SvnAssignUserToRoleCommand(usrId, role, configuration, project);
		success = executor.executeSync(command);
	}
	
	protected void removeProjcet(Project project){
		SvnCommand command = new SvnRemoveProjectCommand(configuration,project);
		success = executor.executeSync(command);
	}

	@Override
	public boolean canConnect() {
		return false;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
