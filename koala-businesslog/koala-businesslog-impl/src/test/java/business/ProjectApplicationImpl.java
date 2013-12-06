package business;

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
}
