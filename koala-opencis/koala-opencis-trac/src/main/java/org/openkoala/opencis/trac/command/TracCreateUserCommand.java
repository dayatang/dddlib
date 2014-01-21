package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * Trac创建用户、密码命令
 * @author zjh
 *
 */
public class TracCreateUserCommand extends TracCommand {

	private Developer developer;
	
	public TracCreateUserCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public TracCreateUserCommand(SSHConnectConfig configuration,Project project,Developer developer) {
		super(configuration, project);
		this.developer = developer;
	}


	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String filePath = storePath + project.getProjectName() + "/conf";
		String param = "";
		//如果文件不存在，要增加c参数选项
		if(!CommonUtil.fileExists(filePath,"passwd")){
			param = "c";
		}
		String createUserCommand = "htpasswd -b" + param + " " +  filePath + " "
				+ developer.getId() + " " + developer.getPassword();
		return createUserCommand;
	}

}
