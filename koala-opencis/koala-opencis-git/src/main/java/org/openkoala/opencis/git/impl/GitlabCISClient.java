package org.openkoala.opencis.git.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.CISClientBaseRuntimeException;

/**
 * 连接GitLab服务器并实现创建项目等操作的客户端.
 *
 * @author xmfang
 */
public class GitlabCISClient implements CISClient {


    /*
     * gitlab配置
     */
    private GitlabConfiguration gitLabConfiguration;

    private GitlabAPI gitlabAPI;

    private GitlabHTTPRequestor gitlabHTTPRequestor;

    /*
     * 当前gitlab上的用户集
     */
    private Set<GitlabUser> currentGitlabUsers;


    private GitlabCISClient() {
    }

    public GitlabCISClient(GitlabConfiguration gitLabConfiguration) {
        this.gitLabConfiguration = gitLabConfiguration;
        gitlabAPI = GitlabAPI.connect(gitLabConfiguration.getGitHostURL(), gitLabConfiguration.getToken());
        gitlabHTTPRequestor = new GitlabHTTPRequestor(gitlabAPI);
        currentGitlabUsers = getCurrentGitlabUsers(gitlabHTTPRequestor);
    }


    private Set<GitlabUser> getCurrentGitlabUsers(GitlabHTTPRequestor gitlabHTTPRequestor) {
        Set<GitlabUser> currentGitlabUsers = new HashSet<GitlabUser>();
        List<LinkedHashMap<String, Object>> usersMap = null;
        try {
            usersMap = gitlabHTTPRequestor.method("GET").to(GitlabUser.URL, List.class);
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab.getUsers.IOException", e);
        }
        for (LinkedHashMap<String, Object> userMap : usersMap) {
            GitlabUser user = new GitlabUser();
            user.setId((Integer) userMap.get("id"));
            user.setEmail(String.valueOf(userMap.get("email")));
            user.setUsername(String.valueOf(userMap.get("username")));
            user.setName(String.valueOf(userMap.get("name")));
            user.setState(String.valueOf(userMap.get("state")));
            currentGitlabUsers.add(user);
        }
        return currentGitlabUsers;
    }


    @Override
    public void close() {
        // do nothing
    }

    @Override
    public void createProject(Project project) {
        createProjectInGitLab(project);
        pushProjectToGitLab(project);
    }

    @Override
    public void removeProject(Project project) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 在gitlab中创建项目.
     *
     * @param project
     */
    private GitlabProject createProjectInGitLab(Project project) {
        GitlabProject gitlabProject = null;
        try {
            gitlabProject = gitlabHTTPRequestor.method("POST")
                    .with("name", project.getProjectName()).with("description", project.getDescription()).with("public", true)
                    .to(GitlabProject.URL, GitlabProject.class);
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab.createProject.IOException");
        }
        return gitlabProject;
    }

    /**
     * 分配Gitlab用户到所创建的项目中，用户如果不存在则创建之.
     *
     * @param project
     */
    private void addProjectTeamMember(Project project, GitlabProject gitlabProject) {
        for (Developer developer : project.getDevelopers()) {
            createUserIfNecessary(project, developer);
            Integer userId = getUserIdByUsername(developer.getId());
            if (userId == null) {
                continue;
            }
            String wsUrl = "/projects/" + gitlabProject.getId() + "/members";
            try {
                gitlabHTTPRequestor.method("POST")
                        .with("id", gitlabProject.getId()).with("user_id", userId).with("access_level", "30")
                        .to(wsUrl, gitlabProject);
            } catch (IOException e) {
                throw new CISClientBaseRuntimeException("gitlab.addProjectTeamMember.IOException", e);
            }
        }
    }

    /**
     * 根据用户帐号获取Gitlab用户的id.
     *
     * @param username
     * @return
     */
    private Integer getUserIdByUsername(String username) {
        for (GitlabUser user : currentGitlabUsers) {
            if (user.getUsername().equals(username)) {
                return user.getId();
            }
        }
        return null;
    }

    /**
     * 推送项目到Gitlab中.
     *
     * @param project
     */
    private boolean pushProjectToGitLab(Project project) {
        Repository repository = null;
        InitCommand init = new InitCommand();

        String projectPath = project.getPhysicalPath();
        if (StringUtils.isBlank(projectPath)) {
            return false;
        }

        init.setDirectory(new File(projectPath));
        try {
            Git git = init.call();
            repository = git.getRepository();
            StoredConfig config = repository.getConfig();
            RemoteConfig remoteConfig = new RemoteConfig(config, "origin");

            URIish uri = new URIish(gitLabConfiguration.getGitHostURL()
                    + "/" + getCurrentUser().getUsername()
                    + "/" + project.getProjectName().toLowerCase() + ".git");

            RefSpec refSpec = new RefSpec("+refs/heads/*:*");
            remoteConfig.addFetchRefSpec(refSpec);
            remoteConfig.addPushRefSpec(refSpec);
            remoteConfig.addURI(uri);
            remoteConfig.addPushURI(uri);

            remoteConfig.update(config);
            config.save();
            repository.close();

            git.add().addFilepattern(".").call();
            git.commit().setCommitter(gitLabConfiguration.getAdminUsername(),
                    gitLabConfiguration.getAdminEmail()).setMessage("init project").call();

            CredentialsProvider credentialsProvider =
                    new UsernamePasswordCredentialsProvider(gitLabConfiguration.getAdminUsername(), gitLabConfiguration.getAdminPassword());
            git.push().setCredentialsProvider(credentialsProvider).call();
        } catch (URISyntaxException e) {
            throw new CISClientBaseRuntimeException("gitlab.pushProjectToGitLab.URISyntaxException");
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab.pushProjectToGitLab.IOException");
        } catch (GitAPIException e) {
            throw new CISClientBaseRuntimeException("gitlab.pushProjectToGitLab.GitAPIException");
        }
        return true;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    private GitlabUser getCurrentUser() {
        GitlabUser result = null;
        try {
            result = gitlabHTTPRequestor.method("GET").to("/user", GitlabUser.class);
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab.getCurrentUser.IOException");
        }
        return result;
    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        if (isUserExist(developer.getId())) {
            return;
        }
        createNewUser(developer);
    }

    @Override
    public void removeUser(Project project, Developer developer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {

    }

    @Override
    public boolean authenticate() {
        return true;
    }

    /**
     * 根据用户帐号检查该帐号是否已经存在
     *
     * @param userAccount
     * @return
     */
    private boolean isUserExist(String userAccount) {
        for (GitlabUser user : currentGitlabUsers) {
            if (user.getUsername().equals(userAccount)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建新用户
     *
     * @param developer
     */
    private void createNewUser(Developer developer) {
        GitlabUser gitlabUser;
        try {
            gitlabUser = gitlabHTTPRequestor.method("POST")
                    .with("email", developer.getEmail()).with("username",
                            developer.getId()).with("name", developer.getName()).with("password", developer.getId())
                    .to(GitlabUser.URL, GitlabUser.class);
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab.createUser.IOException", e);
        }

        currentGitlabUsers.add(gitlabUser);
    }


}
