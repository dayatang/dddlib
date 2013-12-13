package org.openkoala.businesslog;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 4:23 PM
 */
public abstract class AbstractBusinessLogRender implements BusinessLogRender {

    private StringBuilder builder = new StringBuilder();

    public BusinessLogRender render(String log) {
        builder.append(log);
        return this;
    }

    @Override
    public String build() {
        String result = builder.toString();
        builder.delete(0, builder.length() - 1);
        return result;
    }

    public StringBuilder getBuilder() {
        return builder;
    }
}
