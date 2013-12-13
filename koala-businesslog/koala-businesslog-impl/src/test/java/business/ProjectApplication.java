package business;

import java.util.List;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 10:05 AM
 */
public interface ProjectApplication {
    Project findByProjectName(String projectName);

    List<Project> findSomeProjects(List<String> projectNames);

}
