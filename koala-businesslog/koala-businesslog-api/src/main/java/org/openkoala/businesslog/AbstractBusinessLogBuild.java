package org.openkoala.businesslog;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 4:23 PM
 */
public abstract class AbstractBusinessLogBuild implements BusinessLogRender {

    protected StringBuilder builder = new StringBuilder();

    public BusinessLogRender render(String log) {
        builder.append(log);
        return this;
    }

    @Override
    public String build() {
        return builder.toString();
    }
}
