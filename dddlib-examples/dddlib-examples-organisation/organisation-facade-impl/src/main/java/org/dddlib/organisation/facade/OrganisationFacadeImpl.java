/*
 * Copyright 2014 Dayatang Open Source..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dddlib.organisation.facade;

import org.dddlib.organisation.domain.Organization;
import org.dddlib.organisation.domain.Party;
import org.dddlib.organisation.domain.Post;
import org.dddlib.organisation.facade.assembler.OrganizationAssembler;
import java.util.Date;
import javax.inject.Inject;
import org.dddlib.organisation.application.OrganisationApplication;
import org.dddlib.organisation.facade.dto.OrganizationDto;
import org.dddlib.organisation.facade.dto.PostDto;
import org.dddlib.organisation.facade.assembler.PostAssembler;

/**
 *
 * @author yyang
 */
public class OrganisationFacadeImpl implements OrganisationFacade {

    @Inject
    private OrganisationApplication application;

    public OrganisationFacadeImpl(OrganisationApplication application) {
        this.application = application;
    }

    @Override
    public void createOrganization(OrganizationDto orgToCreate, Long parentOrgId, Date date) {
        Organization organization = new OrganizationAssembler().toEntity(orgToCreate);
        Organization parent = application.getEntity(Organization.class, parentOrgId);
        application.createOrganization(organization, parent, date);
    }

    @Override
    public void terminateParty(Long partyId, Date date) {
        Party party = application.getEntity(Party.class, partyId);
        application.terminateParty(party, date);
    }

    @Override
    public void changeParentOfOrganization(Long organizationId, Long newParentId, Date date) {
        Organization organization = application.getEntity(Organization.class, organizationId);
        Organization parent = application.getEntity(Organization.class, newParentId);
        application.changeParentOfOrganization(organization, parent, date);
    }

    @Override
    public void createPostUnderOrganization(PostDto postDto, Long organizationId, Date date) {
        Post post = new PostAssembler().toEntity(postDto);
        Organization organization = application.getEntity(Organization.class, organizationId);
        application.createPostUnderOrganization(post, organization, date);
    }

    @Override
    public PostDto getPost(long postId) {
        Post post = application.getEntity(Post.class, postId);
        return new PostAssembler().toDto(post);
    }

}
