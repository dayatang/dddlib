package org.openkoala.opencis.jenkins;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.CISClientAbstactIntegrationTest;
import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.configureImpl.scm.SvnConfig;

import java.net.MalformedURLException;


//@Ignore
public class JenkinsCISClientIntegrationTest extends CISClientAbstactIntegrationTest {


    @Test
    public void test() throws MalformedURLException {

        Project project = getProject("907");

        AuthenticationStrategy au = ownAuthenticationAndCreateWebDriver();

        JenkinsCISClient client = new JenkinsCISClient(jenkinsURL, au);
        client.authenticate();
        client.setScmConfig(new SvnConfig("http://location:8080/svn", "username", "password"));
        client.createProject(project);
        client.close();

        // TODO 添加自动验证
        Developer developer = getDeveloper("907");
        AuthenticationStrategy driver1 = ownAuthenticationAndCreateWebDriver();
        JenkinsCISClient client1 = new JenkinsCISClient(jenkinsURL, driver1);
        client1.authenticate();
        client1.createUserIfNecessary(project, developer);
        client1.close();

        // TODO 添加自动验证

        //授权
        AuthenticationStrategy driver2 = ownAuthenticationAndCreateWebDriver();
        JenkinsCISClient client2 = new JenkinsCISClient(jenkinsURL, driver2);
        client2.authenticate();
        client2.assignUsersToRole(project, "role", developer);
        client2.close();

    }


}
