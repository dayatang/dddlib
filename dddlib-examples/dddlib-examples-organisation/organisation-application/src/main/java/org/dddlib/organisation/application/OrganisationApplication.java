package org.dddlib.organisation.application;

import org.dayatang.domain.Entity;
import org.dddlib.organisation.domain.Organization;
import org.dddlib.organisation.domain.Party;
import org.dddlib.organisation.domain.Post;

import java.util.Date;

public interface OrganisationApplication {

    public <T extends Entity> T getEntity(Class<T> entityClass, Long entityId);

    void createOrganization(Organization orgToCreate, Organization parent, Date date);

    void terminateParty(Party party, Date date);

    void changeParentOfOrganization(Organization organization, Organization newParent, Date date);

    void createPostUnderOrganization(Post post, Organization organization, Date date);
}
