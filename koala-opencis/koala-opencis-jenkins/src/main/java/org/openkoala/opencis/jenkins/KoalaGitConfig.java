package org.openkoala.opencis.jenkins;

import nl.tudelft.jenkins.jobs.GitScmConfig;
import nl.tudelft.jenkins.jobs.ScmConfig;

/**
 * User: zjzhai
 * Date: 1/22/14
 * Time: 10:56 AM
 */
public class KoalaGitConfig extends KoalaScmConfig {


    public KoalaGitConfig(String scmAddress) {
        super(scmAddress);
    }

    @Override
    protected ScmConfig getScmConfig() {
        return new GitScmConfig(getScmAddress());
    }
}
