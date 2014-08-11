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

package org.dddlib.organisation.application.impl;

import java.util.Date;
import org.dddlib.organisation.domain.Organization;
import org.dddlib.organisation.domain.Party;
import org.dddlib.organisation.domain.Post;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author yyang
 */
public class OrganisationApplicationImplIntegratedTest {
    
    public OrganisationApplicationImplIntegratedTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createOrganization method, of class OrganisationApplicationImpl.
     */
    @Ignore
    @Test
    public void testCreateOrganization() {
        System.out.println("createOrganization");
        Organization orgToCreate = null;
        Organization parent = null;
        Date date = null;
        OrganisationApplicationImpl instance = new OrganisationApplicationImpl();
        instance.createOrganization(orgToCreate, parent, date);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of terminateParty method, of class OrganisationApplicationImpl.
     */
    @Ignore
    @Test
    public void testTerminateParty() {
        System.out.println("terminateParty");
        Party party = null;
        Date date = null;
        OrganisationApplicationImpl instance = new OrganisationApplicationImpl();
        instance.terminateParty(party, date);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of changeParentOfOrganization method, of class OrganisationApplicationImpl.
     */
    @Ignore
    @Test
    public void testChangeParentOfOrganization() {
        System.out.println("changeParentOfOrganization");
        Organization organization = null;
        Organization newParent = null;
        Date date = null;
        OrganisationApplicationImpl instance = new OrganisationApplicationImpl();
        instance.changeParentOfOrganization(organization, newParent, date);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createPostUnderOrganization method, of class OrganisationApplicationImpl.
     */
    @Ignore
    @Test
    public void testCreatePostUnderOrganization() {
        System.out.println("createPostUnderOrganization");
        Post post = null;
        Organization organization = null;
        Date date = null;
        OrganisationApplicationImpl instance = new OrganisationApplicationImpl();
        instance.createPostUnderOrganization(post, organization, date);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
