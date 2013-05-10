package com.dayatang.rule;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.rules.StatelessRuleSession;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.rule.examples.Person;

public class StatelessRuleTemplateTest {
	
	private StatelessRuleTemplate instance;
	private String ruleDrl = "/rule/Gender.drl";
	private Person chencao;
	private Person xishi;
	private Person yyang;

	@Before
	public void setUp() throws Exception {
		instance = createStatelessRuleTemplate();
		chencao = new Person(1L, "chencao", "male");
		xishi = new Person(2L, "xishi", "female");
		yyang = new Person(3L, "yyang", "male");
	}

	private StatelessRuleTemplate createStatelessRuleTemplate() {
		InputStream ruleSource = getClass().getResourceAsStream(ruleDrl);
		return new StatelessRuleTemplate(new RuleServiceProviderImpl()).ruleSource(ruleSource);
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testExecuteCallback() throws Exception {
		final List facts = Arrays.asList(chencao, xishi, yyang);
		List results = instance.execute(new StatelessRuleCallback() {
			
			@Override
			public List doInRuleSession(StatelessRuleSession session) throws Exception {
				return session.executeRules(facts);
			}
		});

		// Validate
		assertTrue(results.containsAll(facts));
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testExecuteFacts() throws Exception {
		final List facts = Arrays.asList(chencao, xishi, yyang);
		List results = instance.execute(facts);

		// Validate
		assertTrue(results.containsAll(facts));
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testExecuteManyTimes() throws Exception {
		List results = instance.execute(new StatelessRuleCallback() {
			
			@Override
			public List doInRuleSession(StatelessRuleSession session) throws Exception {
				List ret = new ArrayList();
				ret.addAll(session.executeRules(Arrays.asList(chencao, xishi)));
				ret.addAll(session.executeRules(Collections.singletonList(yyang)));
				return ret;
			}
		});

		// Validate
		assertTrue(results.containsAll(Arrays.asList(chencao, xishi, yyang)));
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());
	}

}
