package org.openkoala.gqc.core.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author lambo
 *
 */
public class DynamicQueryConditionTest{
	
	/**
	 * 实例
	 */
	private DynamicQueryCondition dynamicQueryCondition;

	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		dynamicQueryCondition = this.createAndInitDynamicQueryConditionOneValue();
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		dynamicQueryCondition = null;
	}

	/**
	 * 测试
	 */
	@Test
	public void testGenerateConditionStatment() {
		String queryCondition = dynamicQueryCondition.generateConditionStatment();
		assertEquals(" and name like '%test%'", queryCondition);
		
		dynamicQueryCondition = this.createAndInitDynamicQueryConditionTwoValues();
		queryCondition = dynamicQueryCondition.generateConditionStatment();
		assertEquals(" and age between '18' and '30'", queryCondition);
	}
	
	/**
	 * 创建DynamicQueryCondition实例，查询条件范围使用LIKE
	 * @return
	 */
	private DynamicQueryCondition createAndInitDynamicQueryConditionOneValue(){
		DynamicQueryCondition dynamicQueryCondition = new DynamicQueryCondition();
		dynamicQueryCondition.setFieldName("name");
		dynamicQueryCondition.setLabel("姓名");
		dynamicQueryCondition.setQueryOperation(QueryOperation.LIKE);
		dynamicQueryCondition.setWidgetType(WidgetType.TEXT);
		dynamicQueryCondition.setValue("test");
		return dynamicQueryCondition;
	}
	
	/**
	 * 创建DynamicQueryCondition实例，查询条件范围使用BETWEEN
	 * @return
	 */
	private DynamicQueryCondition createAndInitDynamicQueryConditionTwoValues(){
		DynamicQueryCondition dynamicQueryCondition = new DynamicQueryCondition();
		dynamicQueryCondition.setFieldName("age");
		dynamicQueryCondition.setLabel("年龄");
		dynamicQueryCondition.setQueryOperation(QueryOperation.BETWEEN);
		dynamicQueryCondition.setWidgetType(WidgetType.TEXT);
		dynamicQueryCondition.setStartValue("18");
		dynamicQueryCondition.setEndValue("30");
		return dynamicQueryCondition;
	}

}
