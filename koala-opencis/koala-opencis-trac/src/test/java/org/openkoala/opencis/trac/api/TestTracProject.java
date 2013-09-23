package org.openkoala.opencis.trac.api;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

public class TestTracProject implements Project {

	@Override
	public String getArtifactId() {
		// TODO Auto-generated method stub
		return "testProject";
	}

	@Override
	public String getProjectName() {
		// TODO Auto-generated method stub
		return "testProject";
	}

	@Override
	public String getProjectPath() {
		// TODO Auto-generated method stub
		return "/usr/share/trac/projects/" + this.getArtifactId();
	}

	@Override
	public List<Developer> getProjectDeveloper() {
		// TODO Auto-generated method stub
		//新增一个角色
		List<String> roles = new ArrayList<String>();
		roles.add("TRAC_ADMIN");
		//创建一个开发者
		Developer developer = new Developer();
		developer.setId("");
		developer.setName("zjh");
		developer.setEmail("");
		developer.setRoles(roles);
		//创建一个开发者列表
		List<Developer> developers = new ArrayList<Developer>();
		developers.add(developer);
		
		return developers;
	}

}
