package org.dddlib.organisation.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("Post")
@NamedQueries(
        @NamedQuery(name = "Party.findByOrganization", query = "select o from Post o where o.organization = :organization and o.createDate <= :date and o.terminateDate > :date"))
public class Post extends Party {

    private static final long serialVersionUID = -2205967098970951498L;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Post() {
        super();
    }

    public Post(String name) {
        super(name);
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public static List<Post> findByOrganization(Organization organization,
            Date date) {
        return getRepository().createNamedQuery("Party.findByOrganization")
                .addParameter("organization", organization).addParameter("date", date).list();
    }

    public Set<Employee> getEmployees(Date date) {
        return new HashSet<Employee>(PostHolding.findEmployeesOfPost(this, date));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getName()).build();
    }

}
