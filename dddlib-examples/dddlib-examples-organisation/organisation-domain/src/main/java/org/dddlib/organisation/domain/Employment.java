package org.dddlib.organisation.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("Employment")
@NamedQueries({
    @NamedQuery(name = "Employment.getEmployer", query = "select o.commissioner from Employment o where o.responsible = :employee and o.fromDate <= :date and o.toDate > :date"),
    @NamedQuery(name = "Employment.getEmployees", query = "select o.responsible from Employment o where o.commissioner = :employer and o.fromDate <= :date and o.toDate > :date")})
public class Employment extends Accountability<Company, Employee> {

    private static final long serialVersionUID = 7390804525640459582L;

    protected Employment() {
    }

    public Employment(Company company, Employee employee, Date date) {
        super(company, employee, date);
    }

    public static Company getEmployer(Employee employee, Date date) {
        return getRepository().createNamedQuery("Employment.getEmployer")
                .addParameter("employee", employee).addParameter("date", date).singleResult();
    }

    public static List<Employee> getEmployees(Company employer, Date date) {
        return getRepository().createNamedQuery("Employment.getEmployees")
                .addParameter("employer", employer).addParameter("date", date).list();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getCommissioner())
                .append(getResponsible()).build();
    }

}
