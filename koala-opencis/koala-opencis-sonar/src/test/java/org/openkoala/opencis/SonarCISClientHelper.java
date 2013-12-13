package org.openkoala.opencis;

import java.util.List;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.sonar.SonarCISClient;
import org.sonar.wsclient.permissions.PermissionParameters;
import org.sonar.wsclient.permissions.internal.DefaultPermissionClient;
import org.sonar.wsclient.user.User;
import org.sonar.wsclient.user.UserQuery;
import org.sonar.wsclient.user.internal.DefaultUserClient;

public class SonarCISClientHelper extends SonarCISClient {

	public void removeUserIfNecessary(Developer developer) {
		DefaultUserClient client = new DefaultUserClient(createHttpRequestFactory());
		client.deactivate(developer.getId());
	}
	
	public User findUserByName(String username) {
		DefaultUserClient client = new DefaultUserClient(createHttpRequestFactory());
		UserQuery query = UserQuery.create().logins(username);
		List<User> users = client.find(query);
		return users != null ? users.get(0) : null;
	}
	
	public void removeUserPermission(Project project, String userId) {
		verifyProjectAndUserIdLegal(project, userId);
		String component = project.getGroupId() + ":" + project.getArtifactId();
		try {
			DefaultPermissionClient client = new DefaultPermissionClient(createHttpRequestFactory());
			PermissionParameters params = PermissionParameters.create().user(userId).component(component).permission(SONAN_PERMISSION_USER);
			client.removePermission(params);
		} catch (Exception e) {
			throw new SonarUserExistException("用户名不存在或项目角色信息有误");
		}
	}
}
