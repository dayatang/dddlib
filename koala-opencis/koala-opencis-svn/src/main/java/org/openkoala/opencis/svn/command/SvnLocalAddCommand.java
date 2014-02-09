package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.LocalCommand;
import org.openkoala.opencis.support.SvnConfig;

/**
 * SVN本地添加项目文件到版本库
 * @author zjh
 *
 */
public class SvnLocalAddCommand extends LocalCommand {

	public SvnLocalAddCommand() {
		// TODO Auto-generated constructor stub
	}

	public SvnLocalAddCommand(SvnConfig config, Project project) {
		super(config, project);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String strCmd = "svn add " + CommonUtil.validatePath(project.getPhysicalPath()) + project.getProjectName()
				+ "/*";
		return strCmd;
	}

}
