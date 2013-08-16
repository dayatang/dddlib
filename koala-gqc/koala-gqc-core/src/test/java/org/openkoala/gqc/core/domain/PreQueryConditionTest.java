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
public class PreQueryConditionTest{

	/**
	 * 声明实例
	 */
	private PreQueryCondition preQueryCondition;
	
	/**
	 * 初始化实例
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		preQueryCondition = this.createAndInitPreQueryConditionOneValue();
	}

	/**
	 * 销毁实例
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		preQueryCondition = null;
	}

	/**
	 * 测试
	 */
	@Test
	public void testGenerateConditionStatment() {
		String queryCondition = preQueryCondition.generateConditionStatment();
		assertEquals(" and name like '%test%'", queryCondition);
		
		preQueryCondition = this.createAndInitPreQueryConditionTwoValues();
		queryCondition = preQueryCondition.generateConditionStatment();
		assertEquals(" and age between '18' and '30'", queryCondition);
	}
	
	/**
	 * 创建PreQueryCondition实例，查询条件范围使用LIKE
	 * @return
	 */
	private PreQueryCondition createAndInitPreQueryConditionOneValue(){
		PreQueryCondition preQueryCondition = new PreQueryCondition();
		preQueryCondition.setFieldName("name");
		preQueryCondition.setQueryOperation(QueryOperation.LIKE);
		preQueryCondition.setValue("test");
		return preQueryCondition;
	}
	
	/**
	 * 创建PreQueryCondition实例，查询条件范围使用BETWEEN
	 * @return
	 */
	private PreQueryCondition createAndInitPreQueryConditionTwoValues(){
		PreQueryCondition preQueryCondition = new PreQueryCondition();
		preQueryCondition.setFieldName("age");
		preQueryCondition.setQueryOperation(QueryOperation.BETWEEN);
		preQueryCondition.setStartValue("18");
		preQueryCondition.setEndValue("30");
		return preQueryCondition;
	}

}
