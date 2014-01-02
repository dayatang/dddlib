package org.openkoala.opencis.utils;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.opencis.JenkinsHomeNotFoundException;

import java.io.File;

/**
 * User: zjzhai
 * Date: 12/31/13
 * Time: 10:35 AM
 */
public class JenkinsConfUtil {

    private static final String JENKINS_HOME_SYS_PROPERTY = "JENKINS_HOME";

    private static final String JENKINS_DIR = ".jenkins";


    public static String getJenkinsDir() {

        String jenkinsHome = System.getProperty(JENKINS_HOME_SYS_PROPERTY);

        if (StringUtils.isNotBlank(jenkinsHome)) {
            return jenkinsHome;
        }


        jenkinsHome = System.getProperty("user.home") + File.separator + JENKINS_DIR + File.separator;

        if (!new File(jenkinsHome).exists()) {
            throw new JenkinsHomeNotFoundException();
        }

        return jenkinsHome;

    }

    public static String getJenkinsConfigXmlPath() {
        return getJenkinsDir() + "config.xml";
    }


    public static String getJobConfigXMLPath(String jobName) {
        return getJenkinsDir() + "jobs" + File.separator + jobName + File.separator + "config.xml";
    }

}
