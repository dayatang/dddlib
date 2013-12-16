package business;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 10:07 AM
 */
public class Project {
    private String name;

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static Project findByContract(Contract contract) {
        return new Project("项目xxxx");
    }

    public static String[] getProjectsName(Project... projects) {
        List<String> list = new ArrayList<String>();
        for (Project project : projects) {
            list.add(project.name);
        }
        return list.toArray(new String[list.size()]);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;

        Project project = (Project) o;

        if (name != null ? !name.equals(project.name) : project.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                '}';
    }
}
