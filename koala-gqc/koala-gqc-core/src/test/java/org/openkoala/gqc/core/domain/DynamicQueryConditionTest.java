package org.openkoala.gqc.core.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(transactionManager = "transactionManager_gqc",defaultRollback = true) 
public class DynamicQueryConditionTest extends KoalaBaseSpringTestCase{
	
	private DynamicQueryCondition dynamicQueryCondition;

	@Before
	public void setUp() throws Exception {
		dynamicQueryCondition = this.createAndInitDynamicQueryConditionOneValue();
	}

	@After
	public void tearDown() throws Exception {
		dynamicQueryCondition = null;
	}

	@Test
	public void testGenerateConditionStatment() {
		String queryCondition = dynamicQueryCondition.generateConditionStatment();
		assertEquals("", queryCondition);
	}
	
	private DynamicQueryCondition createAndInitDynamicQueryConditionOneValue(){
		DynamicQueryCondition dynamicQueryCondition = new DynamicQueryCondition();
		dynamicQueryCondition.setFieldName("name");
		dynamicQueryCondition.setLabel("姓名");
		dynamicQueryCondition.setQueryOperation(QueryOperation.LIKE);
		dynamicQueryCondition.setWidgetType(WidgetType.TEXT);
		return dynamicQueryCondition;
	}
	
	private DynamicQueryCondition createAndInitDynamicQueryConditionTwoValues(){
		DynamicQueryCondition dynamicQueryCondition = new DynamicQueryCondition();
		dynamicQueryCondition.setFieldName("name");
		dynamicQueryCondition.setLabel("姓名");
		dynamicQueryCondition.setQueryOperation(QueryOperation.BETWEEN);
		dynamicQueryCondition.setWidgetType(WidgetType.TEXT);
		
		return dynamicQueryCondition;
	}

}
