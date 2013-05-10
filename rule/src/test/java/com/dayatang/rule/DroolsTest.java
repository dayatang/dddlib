package com.dayatang.rule;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.rule.examples.Person;

public class DroolsTest {

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
		// Execute rule
		StatelessKnowledgeSession session = createStatelessKnowledgeSession();
		session.execute(Arrays.asList(chencao, xishi, yyang));

		// Validate
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());

		// Release the resources
	}

	private StatelessKnowledgeSession createStatelessKnowledgeSession() {
		return createknowledgeBase().newStatelessKnowledgeSession();
	}

	@Test
	public void stateful() throws Exception {
		// Execute rule
		StatefulKnowledgeSession session = createStatefulKnowledgeSession();
		session.insert(chencao);
		session.insert(xishi);
		session.insert(yyang);
		session.fireAllRules();

		// Validate
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());

		// Release the resources
		session.dispose();
	}

	private StatefulKnowledgeSession createStatefulKnowledgeSession() {
		return createknowledgeBase().newStatefulKnowledgeSession();
	}

	private KnowledgeBase createknowledgeBase() {
		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase(); 
		knowledgeBase.addKnowledgePackages(createKnowledgeBuilder().getKnowledgePackages());
		return knowledgeBase;
	}

	private KnowledgeBuilder createKnowledgeBuilder() {
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newClassPathResource(ruleDrl, getClass()), ResourceType.DRL);
		if (knowledgeBuilder.hasErrors()) {
			System.err.println(knowledgeBuilder.getErrors().toString());
		}
		return knowledgeBuilder;
	}
}
