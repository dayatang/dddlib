package com.dayatang.rule;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.rule.examples.Person;
import com.dayatang.rule.impl.StatelessRuleServiceJsr94;

public class DayatangRuleTest {

	private String ruleDrl = "/rule/Gender.drl";
	private Person chencao;
	private Person xishi;
	private Person yyang;
	
	@Before
	public void setUp() {
		chencao = new Person(1L, "chencao", "male");
		xishi = new Person(2L, "xishi", "female");
		yyang = new Person(3L, "yyang", "male");
	}
	
	@Test
	public void stateless() throws Exception {
		StatelessRuleService ruleService = new StatelessRuleServiceJsr94(new RuleServiceProviderImpl());

		// Execute rule
		ruleService.executeRules(getClass().getResourceAsStream(ruleDrl), null, null, Arrays.asList(chencao, xishi, yyang));

		// Validate
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());
	}

}
