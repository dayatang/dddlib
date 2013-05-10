package com.dayatang.rule.examples;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Test;

import com.dayatang.rule.StatelessRuleService;
import com.dayatang.rule.impl.StatelessRuleServiceJsr94;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JSR94Test {

	private String ruleDrl = "/rule/Person.drl";

	@Test
	public void stateless() throws Exception {
		// Execute rule
		List<Person> objects = new ArrayList<Person>();
		objects.add(new Person(1L, "chencao"));
		StatelessRuleService ruleService = new StatelessRuleServiceJsr94(new RuleServiceProviderImpl());
		List statelessResults = ruleService.executeRules(getClass().getResourceAsStream(ruleDrl), null, null, objects);

		// Validate
		assertEquals(1, statelessResults.size());

		Person p = (Person) statelessResults.get(0);
		assertEquals(100, p.getId().longValue());
	}

	@Test
	public void globalAndFunction() throws Exception {

		// Prepare global parameter
		List globalList = new ArrayList();
		Map sessionProperties = new HashMap();
		sessionProperties.put("list", globalList);
		StatelessRuleService ruleService = new StatelessRuleServiceJsr94(new RuleServiceProviderImpl());

		// Execute rule
		Person firstPerson = new Person(3L, "chencao");
		ruleService.executeRules(getClass().getResourceAsStream(ruleDrl), null, sessionProperties, Collections.singletonList(firstPerson));

		// FirstPerson hasn't been changed
		assertEquals(300, firstPerson.getId().longValue());

		// Validate global
		List global = (List) sessionProperties.get("list");
		assertEquals(2, global.size());
		Person p1 = (Person) global.get(0);
		assertEquals(300, p1.getId().longValue());
		Person p2 = (Person) global.get(1);
		assertEquals(400, p2.getId().longValue());
		assertEquals("pengmei", p2.getName());

	}

}
