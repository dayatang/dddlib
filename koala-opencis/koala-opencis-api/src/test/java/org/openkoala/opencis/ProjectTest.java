package org.openkoala.opencis;

import org.junit.Test;
import org.openkoala.opencis.api.Project;

/**
 * User: zjzhai
 * Date: 1/19/14
 * Time: 10:20 AM
 */
public class ProjectTest {
    @Test(expected = ProjectValidateFailureException.class)
    public void testName() throws Exception {

        Project project = new Project();
        project.setProjectName("x");
        project.validate();

        project.setProjectName("中文名");
        project.validate();

    }


    @Test()
    public void testName1() throws Exception {

        Project project = new Project();
        project.setProjectName("xxxx");
        assert project.validate();

    }
}
