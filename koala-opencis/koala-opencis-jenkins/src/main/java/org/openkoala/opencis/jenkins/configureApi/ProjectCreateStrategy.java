package org.openkoala.opencis.jenkins.configureApi;

import org.openkoala.opencis.api.Project;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 5:40 PM
 */
public interface ProjectCreateStrategy<T> {

    boolean createAndConfig(String jenkinsBaseUrl, Project project, T context);

    String getErrors();


}
