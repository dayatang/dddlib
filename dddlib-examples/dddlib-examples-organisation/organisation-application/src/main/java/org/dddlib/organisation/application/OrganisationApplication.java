package org.dddlib.organisation.application;

import org.dddlib.organisation.domain.Organization;
import org.dddlib.organisation.domain.Party;
import org.dddlib.organisation.domain.Post;

import java.util.Date;

public interface OrganisationApplication {
    void createOrganization(Organization orgToCreate, Organization parent,
                            Date date);

    void terminateParty(Party party, Date date);

    void changeParentOfOrganization(Organization organization,
                                    Organization newParent, Date date);

    void createPostUnderOrganization(Post post, Organization organization,
                                     Date date);
}
