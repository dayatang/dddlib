package org.dayatang.domain;

import junit.framework.TestCase;
import org.dayatang.domain.internal.criterion.*;

import java.util.Arrays;

/**
 * Created by yyang on 15/8/10.
 */
public class CriteriaTest extends TestCase {

    public void testEq() throws Exception {
        assertEquals(Criteria.eq("disabled", false), new EqCriterion("disabled", false));
    }

    public void testNotEq() throws Exception {
        assertEquals(Criteria.notEq("disabled", false), new NotEqCriterion("disabled", false));
    }

    public void testGe() throws Exception {
        assertEquals(Criteria.ge("disabled", false), new GeCriterion("disabled", false));
    }

    public void testGt() throws Exception {
        assertEquals(Criteria.gt("disabled", false), new GtCriterion("disabled", false));
    }

    public void testLe() throws Exception {
        assertEquals(Criteria.le("disabled", false), new LeCriterion("disabled", false));
    }

    public void testLt() throws Exception {
        assertEquals(Criteria.lt("disabled", false), new LtCriterion("disabled", false));
    }

    public void testEqProp() throws Exception {
        assertEquals(Criteria.eqProp("prop1", "prop2"), new EqPropCriterion("prop1", "prop2"));
    }

    public void testNotEqProp() throws Exception {
        assertEquals(Criteria.notEqProp("prop1", "prop2"), new NotEqPropCriterion("prop1", "prop2"));
    }

    public void testGtProp() throws Exception {
        assertEquals(Criteria.gtProp("prop1", "prop2"), new GtPropCriterion("prop1", "prop2"));
    }

    public void testGeProp() throws Exception {
        assertEquals(Criteria.geProp("prop1", "prop2"), new GePropCriterion("prop1", "prop2"));
    }

    public void testLtProp() throws Exception {
        assertEquals(Criteria.ltProp("prop1", "prop2"), new LtPropCriterion("prop1", "prop2"));
    }

    public void testLeProp() throws Exception {
        assertEquals(Criteria.leProp("prop1", "prop2"), new LePropCriterion("prop1", "prop2"));
    }

    public void testSizeEq() throws Exception {
        assertEquals(Criteria.sizeEq("children", 3), new SizeEqCriterion("children", 3));
    }

    public void testSizeNotEq() throws Exception {
        assertEquals(Criteria.sizeNotEq("children", 3), new SizeNotEqCriterion("children", 3));
    }

    public void testSizeGt() throws Exception {
        assertEquals(Criteria.sizeGt("children", 3), new SizeGtCriterion("children", 3));
    }

    public void testSizeGe() throws Exception {
        assertEquals(Criteria.sizeGe("children", 3), new SizeGeCriterion("children", 3));
    }

    public void testSizeLt() throws Exception {
        assertEquals(Criteria.sizeLt("children", 3), new SizeLtCriterion("children", 3));
    }

    public void testSizeLe() throws Exception {
        assertEquals(Criteria.sizeLe("children", 3), new SizeLeCriterion("children", 3));
    }

    public void testContainsText() throws Exception {
        assertEquals(Criteria.containsText("name", "zhang"), new ContainsTextCriterion("name", "zhang"));
    }

    public void testStartsWithText() throws Exception {
        assertEquals(Criteria.startsWithText("name", "zhang"), new StartsWithTextCriterion("name", "zhang"));
    }

    public void testInCollection() throws Exception {
        assertEquals(Criteria.in("status", Arrays.asList("a", "b")), new InCriterion("status", Arrays.asList("a", "b")));
    }

    public void testInArray() throws Exception {
        assertEquals(Criteria.in("status", new String[] {"a", "b"}), new InCriterion("status", new String[] {"a", "b"}));
    }

    public void testNotInCollection() throws Exception {
        assertEquals(Criteria.notIn("status", Arrays.asList("a", "b")), new NotInCriterion("status", Arrays.asList("a", "b")));
    }

    public void testNotInArray() throws Exception {
        assertEquals(Criteria.notIn("status", new String[]{"a", "b"}), new NotInCriterion("status", new String[] {"a", "b"}));
    }

    public void testBetween() throws Exception {
        assertEquals(Criteria.between("age", 20, 30), new BetweenCriterion("age", 20, 30));
    }

    public void testIsNull() throws Exception {
        assertEquals(Criteria.isNull("created"), new IsNullCriterion("created"));
    }

    public void testNotNull() throws Exception {
        assertEquals(Criteria.notNull("created"), new NotNullCriterion("created"));
    }

    public void testIsEmpty() throws Exception {
        assertEquals(Criteria.isEmpty("children"), new IsEmptyCriterion("children"));
    }

    public void testNotEmpty() throws Exception {
        assertEquals(Criteria.notEmpty("children"), new NotEmptyCriterion("children"));
    }

    public void testIsTrue() throws Exception {
        assertEquals(Criteria.isTrue("disabled"), new EqCriterion("disabled", true));
    }

    public void testIsFalse() throws Exception {
        assertEquals(Criteria.isFalse("disabled"), new EqCriterion("disabled", false));
    }

    public void testIsBlank() throws Exception {
        assertEquals(Criteria.isBlank("name"), new IsNullCriterion("name").or(new EqCriterion("name", "")));
    }

    public void testNotBlank() throws Exception {
        assertEquals(Criteria.notBlank("name"), new NotNullCriterion("name").and(new NotEqCriterion("name", "")));
    }

    public void testNot() throws Exception {
        assertEquals(Criteria.not(Criteria.eq("disabled", false)), new NotCriterion(new EqCriterion("disabled", false)));
    }

    public void testAnd() throws Exception {
        assertEquals(Criteria.and(Criteria.eq("disabled", false), Criteria.eq("name", "zhang")),
                new AndCriterion(new EqCriterion("disabled", false), new EqCriterion("name", "zhang")));
    }

    public void testOr() throws Exception {
        assertEquals(Criteria.or(Criteria.eq("disabled", false), Criteria.eq("name", "zhang")),
                new OrCriterion(new EqCriterion("disabled", false), new EqCriterion("name", "zhang")));
    }

    public void testEmpty() throws Exception {
        assertEquals(Criteria.empty(), EmptyCriterion.singleton());
    }
}