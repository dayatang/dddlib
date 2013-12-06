package org.openkoala.businesslog.model;

import com.dayatang.domain.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: zjzhai
 * Date: 12/5/13
 * Time: 4:54 PM
 */
@Entity
@Table(name = "koala_businesslogs")
public class BusinessLog extends AbstractEntity {

    @Column(name = "log_content")
    private String logContent;

    public BusinessLog(String logContent) {
        this.logContent = logContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BusinessLog)) return false;

        BusinessLog that = (BusinessLog) o;

        if (logContent != null ? !logContent.equals(that.logContent) : that.logContent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return logContent != null ? logContent.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BusinessLog{" +
                "logContent='" + logContent + '\'' +
                '}';
    }
}
