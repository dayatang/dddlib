package org.openkoala.opencis.api;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.opencis.ProjectValidateFailureException;

import java.util.List;
import java.util.Map;

/**
 * 项目相关属性的接口
 *
 * @author lingen
 */
public class Project {

    private String artifactId;

    private String groupId;

    private String projectName;

    /**
     * 项目负责人
     */
    private String projectLead;

    /**
     * 在硬盘中的物理路径
     */
    private String physicalPath;

    private String description;

    private List<Developer> developers;

    // TODO 校验
    public boolean validate() {
        if (projectName.length() < 2) {
            throw new ProjectValidateFailureException("project.validateFailure");
        }

        if (StringUtils.isBlank(groupId)) {
            throw new ProjectValidateFailureException("project.groupId.null");
        }

        if (StringUtils.isBlank(artifactId)) {
            throw new ProjectValidateFailureException("project.artifactId.null");

        }

        for (int i = 0; i < projectName.length(); i++) {
            char c = projectName.charAt(i);
            if (!((c >= 65 && c <= 90) || (c >= 97 && c <= 120))) {
                throw new ProjectValidateFailureException("project.validateFailure");
            }
        }
        return true;
    }


    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPhysicalPath() {
        return physicalPath;
    }

    public void setPhysicalPath(String physicalPath) {
        this.physicalPath = physicalPath;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectLead() {
        return projectLead;
    }

    public void setProjectLead(String projectLead) {
        this.projectLead = projectLead;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Project [artifactId=" + artifactId + ", projectName="
                + projectName + ", projectPath=" + physicalPath
                + ", projectDeveloper=" + developers + "]";
    }
}
