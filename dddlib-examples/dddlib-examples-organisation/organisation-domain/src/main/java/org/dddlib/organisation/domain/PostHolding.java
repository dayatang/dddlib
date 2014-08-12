package org.dddlib.organisation.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("PostHolding")
@NamedQueries({
        @NamedQuery(name = "PostHolding.getPostsOfEmployee", query = "select o.commissioner from PostHolding o where o.responsible = :employee and o.fromDate <= :date and o.toDate > :date"),
        @NamedQuery(name = "PostHolding.getEmployeesOfPost", query = "select o.responsible from PostHolding o where o.commissioner = :post and o.fromDate <= :date and o.toDate > :date")})
public class PostHolding extends Accountability<Post, Employee> {

    private static final long serialVersionUID = 7390804525640459582L;

    protected PostHolding() {
    }

    public PostHolding(Post post, Employee employee, Date date) {
        super(post, employee, date);
    }

    public static List<Post> findPostsOfEmployee(Employee employee, Date date) {
        return getRepository().createNamedQuery("PostHolding.getPostsOfEmployee")
                .addParameter("employee", employee).addParameter("date", date).list();
    }

    public static List<Employee> findEmployeesOfPost(Post post, Date date) {
        return getRepository().createNamedQuery("PostHolding.getEmployeesOfPost")
                .addParameter("post", post).addParameter("date", date).list();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getCommissioner())
                .append(getResponsible()).build();
    }

}
