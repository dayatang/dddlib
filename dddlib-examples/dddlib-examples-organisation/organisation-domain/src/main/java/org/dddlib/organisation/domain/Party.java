package org.dddlib.organisation.domain;

import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.dayatang.domain.AbstractEntity;
import org.dayatang.utils.DateUtils;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
public abstract class Party extends AbstractEntity {

    private static final long serialVersionUID = -6083088250263550905L;

    // 编码
    private String sn;

    // 名称
    private String name;

    // 创建日期
    @Temporal(TemporalType.DATE)
    private Date createDate = new Date();

    // 终结日期
    @Temporal(TemporalType.DATE)
    private Date terminateDate = DateUtils.MAX_DATE;

    public Party() {
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Party(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getTerminateDate() {
        return terminateDate;
    }

    public void setTerminateDate(Date terminateDate) {
        this.terminateDate = terminateDate;
    }

    /**
     * 判断在指定的日期是否“存活”，即参数date处于其生命周期内。
     *
     * @param date
     * @return
     */
    public boolean isActive(Date date) {
        return DateUtils.isSameDay(date, getCreateDate())
                || date.after(getCreateDate())
                && date.before(getTerminateDate());
    }

    /**
     * 终结当事人，例如撤销机构、撤销岗位、员工离职退休等
     *
     * @param date
     */
    @SuppressWarnings("rawtypes")
    public void terminate(Date date) {
        for (Accountability each : Accountability.findAccountabilitiesByParty(this, date)) {
            each.terminate(date);
        }
        terminateDate = date;
        save();
    }

    @Override
    public String[] businessKeys() {
        return new String[]{"sn", "name"};
    }
}
