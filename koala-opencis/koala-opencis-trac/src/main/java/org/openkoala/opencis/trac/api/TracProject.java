package org.openkoala.opencis.trac.api;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;

/**
 * Trac的Project实现类
 * @author 赵健华
 * 2013-9-22 下午3:33:32
 */
public class TracProject implements Project {

	
	private Configuration configuration;
	
	public TracProject() {
		// TODO Auto-generated constructor stub
		configuration = new ConfigurationFactory().fromClasspath("");
	}
	@Override
	public String getArtifactId() {
		// TODO Auto-generated method stub
		return configuration.getString("trac.artifactId");
	}

	@Override
	public String getProjectName() {
		// TODO Auto-generated method stub
		return configuration.getString("trac.projectName");
	}

	@Override
	public String getProjectPath() {
		// TODO Auto-generated method stub
		return configuration.getString("trac.projectPath") + "/" + configuration.getString("trac.projectName");
	}

	@Override
	public List<Developer> getProjectDeveloper() {
		// TODO Auto-generated method stub
		//这里暂时没想到如何读取，如果是基于properties的话，
		//如果是xml，相对比较容易，基于properties的话，需要用特殊字符串作为分隔点
		return new ArrayList<Developer>();
	}

}
