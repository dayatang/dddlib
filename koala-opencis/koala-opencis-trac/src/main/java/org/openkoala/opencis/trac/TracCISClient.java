package org.openkoala.opencis.trac;

import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.CommandExecutor;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.trac.command.TracAssignUserToRoleCommand;
import org.openkoala.opencis.trac.command.TracCommand;
import org.openkoala.opencis.trac.command.TracCreateProjectCommand;
import org.openkoala.opencis.trac.command.TracCreateRoleCommand;

import com.dayatang.configuration.Configuration;

/**
 * Trac的CISClient实现类<br>
 * Trac其实没有角色的概念，只内置了一些默认权限，用户组就是所谓的角色。<br>
 * 创建用户时，需要把用户跟角色(用户组)挂钩。<br>
 * 目前还没有定义好Key的标准
 * @author 赵健华
 * 2013-9-22 上午10:10:26
 */
public class TracCISClient implements CISClient {

	private Configuration configuration = null;
	
	private CommandExecutor executor = new CommandExecutor();
	
	/**执行结果*/
	private boolean success = false;
	
	public TracCISClient() {
		// TODO Auto-generated constructor stub
	}
	
	public TracCISClient(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void createProject(Project project) {
		//使用java SSH来创建项目
		//1、先检测项目是否存在，如果存在则不需要创建
		//2、用命令CommandExecutor来执行TracCreateProjecCommand子类
		
		//初始化命令
		TracCommand command = new TracCreateProjectCommand(configuration,project);
		executor.executeSync(command);
	}

	@Override
	public void createUserIfNecessary(Project project,Developer developer) {
		//Trac在创建用户时就已经指派了角色了，所以，这里不需要执行了 
	}

	@Override
	public void createRoleIfNessceary(Project project,String roleName) {
		// TODO Auto-generated method stub
		//使用java SSH来创建角色
		//1、读取project的配置信息，包括该角色(用户组)默认的权限
		//2、用命令CommandExecutor来执行TracCreateRoleCommand子类
		TracCommand command = new TracCreateRoleCommand(configuration, roleName, project);
		executor.executeSync(command);
	}

	@Override
	public void assignUserToRole(Project project,String usrId, String role) {
		//使用java SSH来分配用户到某个角色，如果是连续分配，个人认为不应该关闭Connection，直到循环完毕才close
		//1、读取project的配置信息
		//2、用命令CommandExecutor来执行TracAssignUserToRoleCommand子类
		TracCommand command = new TracAssignUserToRoleCommand(usrId, role, configuration, project);
		executor.executeSync(command);
	}

	@Override
	public boolean canConnect() {
		return false;
	}

}
