package com.dayatang.rule.impl;

import com.dayatang.rule.StatelessRuleService;
import com.dayatang.rule.examples.Person;
import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Before;
import org.junit.Test;

import javax.rules.StatelessRuleSession;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by yyang on 13-12-22.
 */
public class StatelessRuleServiceJsr94Test {

    private StatelessRuleServiceJsr94 instance;
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
    public void testExecuteRules() {
        instance = StatelessRuleServiceJsr94.builder()
                .ruleServiceProvider(new RuleServiceProviderImpl())
                .serviceProviderProperties(null)
                .executionSetProperties(null)
                .ruleSource(getClass().getResourceAsStream(ruleDrl))
                .sessionProperties(null)
                .bulid();

        // Execute rule
        List results = instance.executeRules(Arrays.asList(chencao, xishi, yyang));

        // Validate
        assertEquals(60, chencao.getRetireAge());
        assertEquals(55, xishi.getRetireAge());
        assertEquals(60, yyang.getRetireAge());

        //Release the resources
        instance.release();

    }

    @Test
    public void testExecuteRulesWithGlobal() {
        Map sessionProperties = new HashMap();
        Map globalMap = new HashMap();
        sessionProperties.put("map", globalMap);
        instance = StatelessRuleServiceJsr94.builder()
                .ruleServiceProvider(new RuleServiceProviderImpl())
                .serviceProviderProperties(null)
                .executionSetProperties(null)
                .ruleSource(getClass().getResourceAsStream("/rule/example.drl"))
                .sessionProperties(sessionProperties)
                .bulid();

        // Execute rule
        List results = instance.executeRules(new ArrayList());

        // Validate
        System.out.println(globalMap.get("cc"));

        //Release the resources
        instance.release();

    }

}
