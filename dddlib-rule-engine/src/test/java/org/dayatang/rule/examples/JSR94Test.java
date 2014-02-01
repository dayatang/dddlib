package org.dayatang.rule.examples;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.dayatang.rule.StatelessRuleService;
import org.dayatang.rule.impl.StatelessRuleServiceJsr94;
import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JSR94Test {

	private String ruleDrl = "/rule/Person.drl";

	@Test
	public void stateless() throws Exception {
		// Execute rule
		List<Person> objects = new ArrayList<Person>();
		objects.add(new Person(1L, "chencao"));

        StatelessRuleService instance = StatelessRuleServiceJsr94.builder()
                .ruleServiceProvider(new RuleServiceProviderImpl())
                .ruleSource(getClass().getResourceAsStream(ruleDrl))
                .bulid();

        // Execute rule
        List statelessResults = instance.executeRules(Arrays.asList(new Person(1L, "chencao")));

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

        StatelessRuleService instance = StatelessRuleServiceJsr94.builder()
                .ruleServiceProvider(new RuleServiceProviderImpl())
                .ruleSource(getClass().getResourceAsStream(ruleDrl))
                .sessionProperties(sessionProperties)
                .bulid();

		// Execute rule
		Person firstPerson = new Person(3L, "chencao");
        instance.executeRules(Arrays.asList(firstPerson));

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
