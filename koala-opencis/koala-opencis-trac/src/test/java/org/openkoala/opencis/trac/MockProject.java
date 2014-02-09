package org.openkoala.opencis.trac;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

public class MockProject extends Project {

	@Override
	public String getArtifactId() {
		// TODO Auto-generated method stub
		return "testProject2";
	}

	@Override
	public String getProjectName() {
		// TODO Auto-generated method stub
		return "testProject2";
	}

	@Override
	public String getPhysicalPath() {
		// TODO Auto-generated method stub
		return "/usr/share/trac/projects/" + this.getArtifactId();
	}

	@Override
	public List<Developer> getDevelopers() {
		// TODO Auto-generated method stub
		//新增一个角色
		List<String> roles = new ArrayList<String>();
		roles.add("TRAC_ADMIN");
		//创建一个开发者
		Developer developer = new Developer();
		developer.setId("");
		developer.setName("developer");
		developer.setEmail("");
		developer.setRoles(roles);
		//创建一个开发者列表
		List<Developer> developers = new ArrayList<Developer>();
		developers.add(developer);
		
		return developers;
	}

}
