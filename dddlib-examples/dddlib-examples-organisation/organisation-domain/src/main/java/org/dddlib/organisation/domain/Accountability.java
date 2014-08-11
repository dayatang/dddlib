package org.dddlib.organisation.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.dayatang.domain.AbstractEntity;
import org.dayatang.utils.DateUtils;

@Entity
@Table(name = "accountabilities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
@NamedNativeQueries({
    @NamedNativeQuery(name = "Accountability.findAccountabilitiesByParty",
            query = "select o from Accountability o where (o.commissioner = :party or o.responsible = :party) and o.fromDate <= :date and o.toDate > :date")})
public abstract class Accountability<C extends Party, R extends Party> extends AbstractEntity {

    private static final long serialVersionUID = 3456398163374995470L;

    @ManyToOne(targetEntity = Party.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "commissioner_id")
    private C commissioner;

    @ManyToOne(targetEntity = Party.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "responsible_id")
    private R responsible;

    @Temporal(TemporalType.DATE)
    @Column(name = "from_date")
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date")
    private Date toDate;

    protected Accountability() {
    }

    public Accountability(C commissioner, R responsible, Date fromDate) {
        this.commissioner = commissioner;
        this.responsible = responsible;
        this.fromDate = new Date(fromDate.getTime());
        this.toDate = DateUtils.MAX_DATE;
    }

    public C getCommissioner() {
        return commissioner;
    }

    public void setCommissioner(C commissioner) {
        this.commissioner = commissioner;
    }

    public R getResponsible() {
        return responsible;
    }

    public void setResponsible(R responsible) {
        this.responsible = responsible;
    }

    public Date getFromDate() {
        return new Date(fromDate.getTime());
    }

    void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Override
    public String[] businessKeys() {
        return new String[]{"commissioner", "responsible", "fromDate", "toDate"};
    }

    public void terminate(Date date) {
        this.toDate = date;
        save();
    }

    @SuppressWarnings("rawtypes")
    public static <T extends Accountability> List<T> findAccountabilities(Class<T> accountabilityClass, Date date) {
        return getRepository().createCriteriaQuery(accountabilityClass).le("fromDate", date).gt("toDate", date).list();
    }

    @SuppressWarnings("rawtypes")
    public static List<Accountability> findAccountabilitiesByParty(Party party, Date date) {
        return getRepository().createNamedQuery("Accountability.findAccountabilitiesByParty")
                .addParameter("party", party).addParameter("date", date).list();
    }

}
