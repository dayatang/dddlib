package org.openkoala.businesslog.model;

import javax.persistence.*;
import java.util.Date;

/**
 * koala默认提供的业务日志实体
 * User: zjzhai
 * Date: 12/11/13
 * Time: 10:58 AM
 */
@Entity
@DiscriminatorValue(value = "SIMPLE")
public class SimpleBusinessLog extends AbstractBusinessLog {

    private String user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(name = "BUSINESS_METHOD")
    private String businessMethod;

    private String log;

    /**
     * 执行失败的异常信息
     */
    private String exception;

    /**
     * 业务方法执行成功
     *
     * @return
     */
    public boolean isBusinessMethodExecuteSuccess() {
        return null == exception || "".equals(exception.trim());
    }

    /**
     * 业务方法执行失败
     *
     * @return
     */
    public boolean isBusinessMethodExecuteFailure() {
        return !isBusinessMethodExecuteSuccess();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getBusinessMethod() {
        return businessMethod;
    }

    public void setBusinessMethod(String businessMethod) {
        this.businessMethod = businessMethod;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }


    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleBusinessLog)) return false;

        SimpleBusinessLog that = (SimpleBusinessLog) o;

        if (businessMethod != null ? !businessMethod.equals(that.businessMethod) : that.businessMethod != null)
            return false;
        if (exception != null ? !exception.equals(that.exception) : that.exception != null) return false;
        if (log != null ? !log.equals(that.log) : that.log != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (businessMethod != null ? businessMethod.hashCode() : 0);
        result = 31 * result + (log != null ? log.hashCode() : 0);
        result = 31 * result + (exception != null ? exception.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleBusinessLog{" +
                "user='" + user + '\'' +
                ", time=" + time +
                ", businessMethod='" + businessMethod + '\'' +
                ", log='" + log + '\'' +
                ", exception='" + exception + '\'' +
                '}';
    }
}
