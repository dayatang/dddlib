package org.dddlib.organisation.application.impl;

import org.dddlib.organisation.application.OrganisationApplication;
import org.dddlib.organisation.domain.OrgLineMgmt;
import org.dddlib.organisation.domain.Organization;
import org.dddlib.organisation.domain.Party;
import org.dddlib.organisation.domain.Post;

import java.util.Date;

public class OrganisationApplicationImpl implements OrganisationApplication {

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
