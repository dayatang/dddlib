package org.openkoala.opencis.jenkins;

import nl.tudelft.jenkins.jobs.SVNScmConfig;
import nl.tudelft.jenkins.jobs.ScmConfig;

/**
 * User: zjzhai
 * Date: 1/22/14
 * Time: 10:56 AM
 */
public class KoalaSvnConfig extends KoalaScmConfig {


    protected KoalaSvnConfig(String scmAddress) {
        super(scmAddress);
    }

    @Override
    protected ScmConfig getScmConfig() {
        return new SVNScmConfig(getScmAddress());
    }
}
