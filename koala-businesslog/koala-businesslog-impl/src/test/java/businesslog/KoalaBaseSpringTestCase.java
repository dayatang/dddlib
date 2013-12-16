/**
 * @(#)TestApp.java
 * 
 * Copyright csair.All rights reserved.
 * This software is the XXX system. 
 *
 * @Version: 1.0
 * @JDK: jdk jdk1.6.0_10
 * @Module: koalaDemo-core
 */ 
 /*- 				History
 **********************************************
 *  ID      DATE           PERSON       REASON
 *  1     2013-3-25     Administrator    Created
 **********************************************
 */

/*
 * Copyright (c) OpenKoala 2011 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package businesslog;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringInstanceProvider;

/**
 *
 * @author  lingen.liu  <a href="mailto:lingen.liu@gmail.com">lingen.liu@gmail.com<a>
 * 
 * @since Koala 1.0
 * 
 * 功能描述：具体功能做描述。	
 * 
 * 版权信息：Copyright (c) 2011 openkoala All Rights Reserved
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional(propagation = Propagation.REQUIRED)
@ContextConfiguration(locations = {"classpath*:context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager_businessLog",defaultRollback = true)
public abstract class KoalaBaseSpringTestCase  extends AbstractTransactionalJUnit4SpringContextTests {

    protected static final Logger logger = LoggerFactory.getLogger(KoalaBaseSpringTestCase.class);

    @Before
    public void setup() {
        logger.debug("applicationContext: "+applicationContext);
        InstanceFactory.setInstanceProvider(new SpringInstanceProvider(applicationContext));
    }

    @After
    public void teardown() {}
    
    

}
