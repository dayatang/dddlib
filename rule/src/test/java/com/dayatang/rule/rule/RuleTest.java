package com.dayatang.rule.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.rule.StatelessRuleService;
import com.dayatang.rule.impl.StatelessRuleServiceJsr94;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RuleTest {

	protected StatelessRuleService ruleService;

	@Before
	public void setup() throws Exception {
		ruleService = new StatelessRuleServiceJsr94(new RuleServiceProviderImpl());
	}

	@Test
	public void stateless() throws Exception {
		StatelessRuleService ruleService = new StatelessRuleServiceJsr94(new RuleServiceProviderImpl());
		Map map = new HashMap();
		Map globalMap = new HashMap();
		map.put("map", globalMap);
		ruleService.executeRules(RuleTest.class.getResourceAsStream("/rule/example.drl"), null, map, new ArrayList());
		System.out.println(globalMap.get("cc"));
	}
	
}
