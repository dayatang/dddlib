package org.openkoala.gqc.core.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
		String queryCondition = dynamicQueryCondition.generateConditionStatment().getStatment();
		assertEquals(" and name like ?", queryCondition);
		
		List<Object> values = new ArrayList<Object>();
		values.add("%test%");
		assertEquals(values, dynamicQueryCondition.generateConditionStatment().getValues());
		
		dynamicQueryCondition = this.createAndInitDynamicQueryConditionTwoValues();
		queryCondition = dynamicQueryCondition.generateConditionStatment().getStatment();
		assertEquals(" and age between ? and ?", queryCondition);
		
		values = new ArrayList<Object>();
		values.add("18");
		values.add("30");
		assertEquals(values, dynamicQueryCondition.generateConditionStatment().getValues());
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
