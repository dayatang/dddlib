package org.openkoala.opencis.jenkins.project;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.scm.ScmConfigStrategy;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 5:40 PM
 */
public interface ProjectCreateStrategy {

    void create(Project project);

}
