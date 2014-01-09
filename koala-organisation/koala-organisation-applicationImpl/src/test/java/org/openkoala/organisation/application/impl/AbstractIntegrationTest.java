package org.openkoala.organisation.application.impl;

import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 集成测试基类。
 * 
 * @author xmfang
 * 
 */
@TransactionConfiguration(transactionManager = "transactionManager_org",
        defaultRollback = true)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/organisation-root.xml"})
public abstract class AbstractIntegrationTest extends KoalaBaseSpringTestCase {
	
}
