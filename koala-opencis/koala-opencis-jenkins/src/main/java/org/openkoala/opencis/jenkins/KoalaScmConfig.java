package org.openkoala.opencis.jenkins;

import nl.tudelft.jenkins.jobs.ScmConfig;

/**
 * User: zjzhai
 * Date: 1/21/14
 * Time: 2:26 PM
 */
public abstract class KoalaScmConfig {

    private String scmAddress;

    protected abstract ScmConfig getScmConfig();

    protected KoalaScmConfig(String scmAddress) {
        this.scmAddress = scmAddress;
    }

    public String getScmAddress() {
        return scmAddress;
    }

    public void setScmAddress(String scmAddress) {
        this.scmAddress = scmAddress;
    }
}
