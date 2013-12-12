package org.openkoala.businesslog.model;

import org.openkoala.businesslog.BusinessLog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Map;

import static org.openkoala.businesslog.common.ContextKeyConstant.*;

/**
 * User: zjzhai
 * Date: 12/12/13
 * Time: 11:23 AM
 */
@Entity
@DiscriminatorValue(value = "default")
public class DefaultBusinessLog extends AbstractBusinessLog {

    private String user;

    private String ip;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    public static DefaultBusinessLog createBy(BusinessLog businessLog) {
        DefaultBusinessLog myBusinessLog = new DefaultBusinessLog();
        Map<String, Object> context = businessLog.getContext();

        if (context.get(BUSINESS_OPERATION_USER) != null) {
            myBusinessLog.setUser((String) context.get(BUSINESS_OPERATION_USER));
        }

        if (context.get(BUSINESS_OPERATION_TIME) != null) {
            myBusinessLog.setTime((Date) context.get(BUSINESS_OPERATION_TIME));
        }

        if (context.get(BUSINESS_OPERATION_IP) != null) {
            myBusinessLog.setIp((String) context.get(BUSINESS_OPERATION_IP));
        }


        return myBusinessLog;


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultBusinessLog)) return false;

        DefaultBusinessLog that = (DefaultBusinessLog) o;

        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MyBusinessLog{" +
                "user='" + user + '\'' +
                ", ip='" + ip + '\'' +
                ", time=" + time +
                '}';
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
