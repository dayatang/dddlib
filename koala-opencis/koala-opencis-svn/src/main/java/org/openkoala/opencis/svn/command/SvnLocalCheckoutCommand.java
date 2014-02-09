package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.LocalCommand;
import org.openkoala.opencis.support.SvnConfig;

/**
 * SVN本地Checkout
 * @author zjh
 *
 */
public class SvnLocalCheckoutCommand extends LocalCommand {

	public SvnLocalCheckoutCommand() {
		// TODO Auto-generated constructor stub
	}

	public SvnLocalCheckoutCommand(SvnConfig config, Project project) {
		super(config, project);
		// TODO Auto-generated constructor stub
	}


	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		// http://10.108.1.138/svn/ddd    test/test
		String strCmd = "svn checkout " + svnAddress 
				+ " " + CommonUtil.validatePath(project.getPhysicalPath()) + project.getProjectName()
				+ " --username " + userName + " --password " + password;
		return strCmd;
	}

}
