package org.openkoala.opencis.trac;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.exception.CreateProjectException;
import org.openkoala.opencis.exception.HostCannotConnectException;
import org.openkoala.opencis.exception.UserOrPasswordErrorException;
import org.openkoala.opencis.support.CommandExecutor;
import org.openkoala.opencis.support.SSHConnectConfig;
import org.openkoala.opencis.trac.command.TracAssignUserToRoleCommand;
import org.openkoala.opencis.trac.command.TracCommand;
import org.openkoala.opencis.trac.command.TracCreateProjectCommand;
import org.openkoala.opencis.trac.command.TracCreateUserCommand;
import org.openkoala.opencis.trac.command.TracCreateUserToRoleCommand;
import org.openkoala.opencis.trac.command.TracRemoveProjectCommand;
import org.openkoala.opencis.trac.command.TracRemoveUserCommand;
import org.openkoala.opencis.trac.command.TracRemoveUserFromRoleCommand;

import com.trilead.ssh2.Connection;


/**
 * Trac的CISClient实现类<br>
 * Trac其实没有角色的概念，只内置了一些默认权限，用户组就是所谓的角色。<br>
 * 创建用户时，需要把用户跟角色(用户组)挂钩。<br>
 * 目前还没有定义好Key的标准
 *
 * @author 赵健华
 *         2013-9-22 上午10:10:26
 */
public class TracCISClient implements CISClient {

    private SSHConnectConfig configuration = null;
    private static final Logger logger = Logger.getLogger(TracCISClient.class);

    private CommandExecutor executor = new CommandExecutor();
    
    private Connection conn;
    /**
     * 执行结果
     */
    private boolean success = false;
    private String errors;

    public TracCISClient() {
        // TODO Auto-generated constructor stub
    }

    public TracCISClient(SSHConnectConfig configuration) {
        this.configuration = configuration;
    }

    @Override
    public void close() {
//    	if(conn!=null){
//			conn.close();
//			conn=null;
//		}
    }


    @Override
    public void createProject(Project project) {
        //使用java SSH来创建项目
        //1、先检测项目是否存在，如果存在则不需要创建
        //2、用命令CommandExecutor来执行TracCreateProjecCommand子类
        //初始化命令
        TracCommand command = new TracCreateProjectCommand(configuration, project);
        try {
            success = executor.executeSync(command);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new CreateProjectException("创建Trac项目" + project.getProjectName() + "失败，原因：" 
            		+ e.getMessage(), e);
        }

    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
    	 //使用java SSH来创建角色
        //1、读取project的配置信息，包括该角色(用户组)默认的权限
        //2、用命令CommandExecutor来执行TracCreateRoleCommand子类
    	TracCommand createUserCommand = new TracCreateUserCommand(configuration, project, developer);
        TracCommand createUserToRoleCommand = new TracCreateUserToRoleCommand(configuration, developer.getId(), project);
        
        try {
        	executor.addCommand(createUserCommand);
        	executor.addCommand(createUserToRoleCommand);
            success = executor.executeBatch();
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
        }
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {
        // TODO Auto-generated method stub
       
    }

    public boolean assignUserToRole(Project project, String usrId, String role) {
        //使用java SSH来分配用户到某个角色，如果是连续分配，个人认为不应该关闭Connection，直到循环完毕才close
        //1、读取project的配置信息
        //2、用命令CommandExecutor来执行TracAssignUserToRoleCommand子类
//        TracCommand command = new TracAssignUserToRoleCommand(usrId, role, configuration, project);
//        try {
//            success = executor.executeSync(command);
//        } catch (Exception e) {
//        	logger.error(e.getMessage(),e);
//            return false;
//        }
        return true;
    }


	@Override
	public void removeProject(Project project) {
		// TODO Auto-generated method stub
		TracCommand command = new TracRemoveProjectCommand(configuration,project);
		try {
			success = executor.executeSync(command);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
	}

	@Override
	public void removeUser(Project project, Developer developer) {
		// TODO Auto-generated method stub
		TracCommand removeUserFromRoleCommand = new TracRemoveUserFromRoleCommand(configuration,developer.getId(),project);
		TracCommand removeUserCommand = new TracRemoveUserCommand(developer, configuration, project);
		try {
			executor.addCommand(removeUserFromRoleCommand);
			executor.addCommand(removeUserCommand);
			success = executor.executeBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
	}


	@Override
	public boolean authenticate() {
		// TODO Auto-generated method stub
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
	

	@Override
	public void assignUsersToRole(Project project, String role, Developer... developers) {
		// TODO Auto-generated method stub
		for(Developer developer:developers){
			assignUserToRole(project, developer.getId(), role);
		}
	}


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
