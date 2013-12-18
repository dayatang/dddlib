package business.impl;

import business.Project;
import business.ProjectApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 10:05 AM
 */
public class ProjectApplicationImpl implements ProjectApplication {

    @Override
    public Project findByProjectName(String projectName) {
        return new Project("ProjectApplicationImpl\'s project");
    }

    @Override
    public Project[] findSomeProjects(List<String> projectNames) {
        List<Project> result = new ArrayList<Project>();
        for (String name : projectNames) {
            result.add(new Project(name));
        }
        return result.toArray(new Project[result.size()]);
    }

    @Override
    public List addProject() {
        return new ArrayList();
    }
}
