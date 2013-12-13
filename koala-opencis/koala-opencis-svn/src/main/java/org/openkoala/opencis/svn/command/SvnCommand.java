package org.openkoala.opencis.svn.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.api.SSHCommand;
import org.openkoala.opencis.svn.ProjectExistenceException;

import com.dayatang.configuration.Configuration;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * 命令模式之余，此抽象类又是子类的模板，子类只需要实现对应的抽象方法即可
 */
public abstract class SvnCommand extends SSHCommand {
	
	public SvnCommand() {
		super();
	}
	
	public SvnCommand(Configuration configuration,Project project) {
		this.host = configuration.getString("HOST");
		this.userName = configuration.getString("USER");
		this.password = configuration.getString("PASSWORD");
		this.project = project;
		
	}
	
	protected String readOutput(InputStream inputStream) throws IOException {
	    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = br.readLine()) != null)
	    {
	    	System.out.println(line);
	    	sb.append(line);
	    }
	    br.close();
	    return sb.toString();
	  }
}
