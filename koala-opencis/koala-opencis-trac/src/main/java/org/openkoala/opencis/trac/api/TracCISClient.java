package org.openkoala.opencis.trac.api;

import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

/**
 * Trac的CISClient实现类
 * @author 赵健华
 * 2013-9-22 上午10:10:26
 */
public class TracCISClient implements CISClient {

	@Override
	public void createProject(Project project) {
		//使用java SSH来创建项目
		
	}

	@Override
	public void createUserIfNecessary(Developer developer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createRoleIfNessceary(String roleName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void assignUserToRole(String usrId, String role) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canConnect() {
		// TODO Auto-generated method stub
		return false;
	}

}
