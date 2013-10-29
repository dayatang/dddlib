package org.openkoala.organisation.domain;

import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 集成测试基类。
 * 
 * @author xmfang
 * 
 */
@TransactionConfiguration(transactionManager = "transactionManager_org",defaultRollback = true)
public class AbstractIntegrationTest extends KoalaBaseSpringTestCase {
	
}
