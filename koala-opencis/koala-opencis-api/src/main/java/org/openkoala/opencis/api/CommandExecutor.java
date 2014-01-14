package org.openkoala.opencis.api;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令执行器
 * @author 赵健华
 * 2013-9-23 下午6:28:28
 */
public class CommandExecutor {

	/**异步执行的命令列表*/
	private List<Command> commands;
	
	public CommandExecutor() {
		commands = new ArrayList<Command>();
	}
	
	public void addCommand(Command command){
		commands.add(command);
	}
	
	public void removeCommand(Command command){
		commands.remove(command);
	}
	
	/**
	 * 立即执行同步命令
	 * @param command
	 * @return
	 * @throws Exception 
	 */
	public boolean executeSync(Command command) throws Exception{
		try {
			command.execute();
			return true;
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/**
	 * 批量异步执行命令
	 */
	public boolean executeBatch(){
		try {
			for(Command command:commands){
				command.execute();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
}
