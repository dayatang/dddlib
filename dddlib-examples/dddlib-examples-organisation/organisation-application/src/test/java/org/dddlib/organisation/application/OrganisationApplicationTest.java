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
package org.dddlib.organisation.application;

import java.util.Date;

import org.dayatang.domain.AbstractEntity;
import org.dayatang.domain.EntityRepository;
import org.dayatang.utils.DateUtils;

import static org.hamcrest.CoreMatchers.*;

import org.dddlib.organisation.domain.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author yyang
 */
public abstract class OrganisationApplicationTest {

    private OrganisationApplication instance;

    protected EntityRepository repository;

    @Before
    public void setUp() {
        instance = createInstance();
        repository = mock(EntityRepository.class);
        AbstractEntity.setRepository(repository);
    }

    @After
    public void tearDown() {
        AbstractEntity.setRepository(null);
    }

    /**
     * Test of createOrganization method, of class OrganisationApplication.
     */
    @Test
    public void testCreateOrganization() {
        System.out.println("createOrganization");
        Organization orgToCreate = new Department("dept");
        Organization parent = new Company("headquarter");
        Date date = DateUtils.date(2012, 1, 1);
        instance.createOrganization(orgToCreate, parent, date);
        assertThat(orgToCreate.getCreateDate(), is(date));
        verify(repository).save(orgToCreate);
        verify(repository).save(new OrgLineMgmt(parent, orgToCreate, date));
    }

    /**
     * Test of terminateParty method, of class OrganisationApplication.
     */
    @Ignore
    @Test
    public void testTerminateParty() {
        System.out.println("terminateParty");
        Party party = mock(Party.class);
        Date date = DateUtils.date(2012, 1, 1);
        instance.terminateParty(party, date);
        verify(party).terminate(date);
    }

    /**
     * Test of changeParentOfOrganization method, of class
     * OrganisationApplication.
     */
    @Ignore
    @Test
    public void testChangeParentOfOrganization() {
        System.out.println("changeParentOfOrganization");
        Organization organization = null;
        Organization newParent = null;
        Date date = null;
        instance.changeParentOfOrganization(organization, newParent, date);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createPostUnderOrganization method, of class
     * OrganisationApplication.
     */
    @Ignore
    @Test
    public void testCreatePostUnderOrganization() {
        System.out.println("createPostUnderOrganization");
        Post post = null;
        Organization organization = null;
        Date date = null;
        instance.createPostUnderOrganization(post, organization, date);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    protected abstract OrganisationApplication createInstance();

}
