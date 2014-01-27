package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.LocalCommand;
import org.openkoala.opencis.support.SvnConfig;

/**
 * SVN本地提交到SVN服务器命令
 * @author zjh
 *
 */
public class SvnLocalCommitCommand extends LocalCommand {

	public SvnLocalCommitCommand() {
		// TODO Auto-generated constructor stub
	}

	public SvnLocalCommitCommand(SvnConfig config, Project project) {
		super(config, project);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String strCmd = "svn commit " + CommonUtil.validatePath(project.getPhysicalPath()) + project.getProjectName() + 
				" -m \"" + "import project " + project.getProjectName() + "\"";
		return strCmd;
	}

}
