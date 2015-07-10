package org.dddlib.organisation.application.impl;

import org.dddlib.organisation.application.OrganisationApplication;

import java.util.Date;
import javax.inject.Inject;
import org.dayatang.domain.Entity;
import org.dayatang.domain.EntityRepository;
import org.dddlib.organisation.domain.OrgLineMgmt;
import org.dddlib.organisation.domain.Organization;
import org.dddlib.organisation.domain.Party;
import org.dddlib.organisation.domain.Post;

public class OrganisationApplicationImpl implements OrganisationApplication {

    @Inject
    private EntityRepository repository;

    public OrganisationApplicationImpl(EntityRepository repository) {
        this.repository = repository;
    }
    
    
    @Override
    public <T extends Entity> T getEntity(Class<T> entityClass, Long entityId) {
        return repository.get(entityClass, entityId);
    }

    @Override
    public void createOrganization(Organization orgToCreate,
                                   Organization parent, Date date) {
        orgToCreate.setCreateDate(date);
        orgToCreate.save();
        new OrgLineMgmt(parent, orgToCreate, date).save();
    }

    @Override
    public void terminateParty(Party party, Date date) {
        party.terminate(date);

    }

    @Override
    public void changeParentOfOrganization(Organization organization,
                                           Organization newParent, Date date) {
        OrgLineMgmt.getByResponsible(organization, date).terminate(date);
        new OrgLineMgmt(newParent, organization, date).save();
    }

    @Override
    public void createPostUnderOrganization(Post post,
                                            Organization organization, Date date) {
        post.setOrganization(organization);
        post.setCreateDate(date);
        post.save();
    }

}
