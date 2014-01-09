package org.openkoala.opencis.jenkins.configureApi;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 5:57 PM
 */
public interface ScmConfigStrategy {

    boolean config(Object context);

    String getError();



}
