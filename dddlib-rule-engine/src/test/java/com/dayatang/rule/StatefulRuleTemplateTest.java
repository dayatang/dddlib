package com.dayatang.rule;

import static org.junit.Assert.*;

import java.io.InputStream;

import javax.rules.StatefulRuleSession;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.rule.examples.Person;

public class StatefulRuleTemplateTest {
	
	private StatefulRuleTemplate instance;
	private String ruleDrl = "/rule/Gender.drl";
	private Person chencao;
	private Person xishi;
	private Person yyang;

	@Before
	public void setUp() throws Exception {
		instance = createStatefulRuleTemplate();
		chencao = new Person(1L, "chencao", "male");
		xishi = new Person(2L, "xishi", "female");
		yyang = new Person(3L, "yyang", "male");
	}

	private StatefulRuleTemplate createStatefulRuleTemplate() {
		InputStream ruleSource = getClass().getResourceAsStream(ruleDrl);
		StatefulRuleTemplate result = new StatefulRuleTemplate(new RuleServiceProviderImpl()).ruleSource(ruleSource);
		return result;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() throws Exception {
		instance.execute(new StatefulRuleCallback() {
			
			@Override
			public void doInRuleSession(StatefulRuleSession session) throws Exception {
				session.addObject(chencao);
				session.addObject(xishi);
				session.addObject(yyang);
				session.executeRules();
			}
		});

		// Validate
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());
		
	}
}
