package org.openkoala.opencis.authorize;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

/**
 * 授权
 * User: zjzhai
 * Date: 1/7/14
 * Time: 8:51 PM
 */
public interface CISAuthorization<T> {

    boolean authorize(String jenkinsBaseUrl, Project project, T context, Developer... developers);

    String getErrors();
}
