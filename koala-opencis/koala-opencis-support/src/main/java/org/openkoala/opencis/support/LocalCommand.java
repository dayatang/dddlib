package org.openkoala.opencis.support;

/**
 * 本地抽象
 * @author zjh
 *
 */
public abstract class LocalCommand extends AbstractCommand{

	public LocalCommand() {
		// TODO Auto-generated constructor stub
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
