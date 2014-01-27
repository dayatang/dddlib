package org.openkoala.opencis.support;

import org.openkoala.opencis.api.Project;

/**
 * 本地抽象
 * @author zjh
 *
 */
public abstract class LocalCommand extends AbstractCommand{

	protected Project project;
	protected String userName;
	protected String password;
	protected String svnAddress;					//svn服务器地址,http://ip/xxxx/xxx
	
	
	public LocalCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public LocalCommand(SvnConfig config,Project project){
		this.project = project;
		this.userName = config.getSvnUser();
		this.password = config.getSvnPassword();
		this.svnAddress = config.getSvnAddress();
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Runtime runtime = Runtime.getRuntime();
		String strCmd = getCommand();
		Process process = runtime.exec(strCmd);
		String result = readOutput(process.getInputStream());
		System.out.println(result);
	}
	
	public abstract String getCommand();
}
