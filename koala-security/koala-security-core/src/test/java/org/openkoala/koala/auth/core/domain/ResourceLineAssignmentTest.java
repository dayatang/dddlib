package org.openkoala.koala.auth.core.domain;

import java.util.List;

import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public class ResourceLineAssignmentTest extends KoalaBaseSpringTestCase {

	@Test
	public void testFindAllResourceLineByUseraccount() {
		List<ResourceLineAssignment> list = ResourceLineAssignment.findAllResourceLineByUseraccount("test");
		System.out.println(list);
	}
}
