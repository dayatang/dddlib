package org.dayatang.rule.time;

import org.dayatang.rule.StatelessRuleService;
import org.dayatang.rule.impl.StatelessRuleServiceJsr94;
import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FooTest {

	String ruleDrl = "/rule/Foo.drl";

	@Test
	public void item1() throws Exception {
        StatelessRuleService instance = StatelessRuleServiceJsr94.builder()
                .ruleServiceProvider(new RuleServiceProviderImpl())
                .ruleSource(getClass().getResourceAsStream(ruleDrl))
                .bulid();
		List<?> globalStatelessResults = instance.executeRules(createObjects());
		for (Object object : globalStatelessResults) {
			System.out.println(object);
		}
	}

	private List<Foo> createObjects() {
		List<Foo> objects = new ArrayList<Foo>();
		objects.add(createFoo(1L, "foo1", "foo1", new Date()));
		objects.add(createFoo(2L, "foo2", "foo2", new Date()));
		return objects;
	}

	private Foo createFoo(long id, String name, String result, Date startDate) {
		Foo foo = new Foo();
		foo.setId(id);
		foo.setName(name);
		foo.setResult(result);
		foo.setStartDate(startDate);
		return foo;
	}
}
