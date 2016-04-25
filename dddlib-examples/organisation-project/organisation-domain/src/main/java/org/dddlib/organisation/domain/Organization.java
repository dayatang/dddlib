package org.dddlib.organisation.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public abstract class Organization extends Party {

    private static final long serialVersionUID = -8953682430610195006L;

    public Organization() {
    }

    public Organization(String name) {
        super(name);
    }

    public Organization getParent(Date date) {
        return OrgLineMgmt.getParentOfOrganization(this, date);
    }

    public List<Organization> getChildren(Date date) {
        return OrgLineMgmt.findChildrenOfOrganization(this, date);
    }

    public Set<Post> getPosts(Date date) {
        return new HashSet<Post>(Post.findByOrganization(this, date));
    }

    @Override
    public void terminate(Date date) {
        if (hasEmployees(date)) {
            throw new TerminateNotEmptyOrganizationException();
        }
        for (Post post : getPosts(date)) {
            post.terminate(date);
        }
        for (Organization child : getChildren(date)) {
            child.terminate(date);
        }
        super.terminate(date);
    }

    private boolean hasEmployees(Date date) {
        for (Post post : getPosts(date)) {
            if (!(post.getEmployees(date).isEmpty())) {
                return true;
            }
        }
        for (Organization child : getChildren(date)) {
            if (child.hasEmployees(date)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getName()).build();
    }

}
