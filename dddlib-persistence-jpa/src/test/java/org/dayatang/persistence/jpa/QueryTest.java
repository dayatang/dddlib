/**
 *
 */
package org.dayatang.persistence.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dayatang.test.domain.Dictionary;
import org.dayatang.test.domain.DictionaryCategory;
import org.dayatang.domain.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author yang
 */
public class QueryTest extends AbstractIntegrationTest {

    private CriteriaQuery instance;

    private DictionaryCategory gender;

    private DictionaryCategory education;

    private Dictionary male;

    private Dictionary female;

    private Dictionary undergraduate;

    private Criterions criterions = Criterions.singleton();

    @Before
    public void setUp() {
        super.setUp();
        instance = repository.createCriteriaQuery(Dictionary.class);
        gender = createCategory("gender", 1);
        education = createCategory("education", 2);
        male = createDictionary("01", "男", gender, 100, "01");
        female = createDictionary("02", "女", gender, 150, "01");
        undergraduate = createDictionary("01", "本科", education, 200, "05");
    }

    @Test
    public void testEq() {
        List<Dictionary> results = instance.eq("category", gender).list();
        assertTrue(results.contains(male));
        assertTrue(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testNotEq() {
        List<Dictionary> results = instance.notEq("category", gender).list();
        Dictionary dictionary = results.get(0);
        assertEquals(education, dictionary.getCategory());
    }

    @Test
    public void testGe() {
        List<Dictionary> results = instance.ge("sortOrder", 150).list();
        assertFalse(results.contains(male));
        assertTrue(results.contains(female));
        assertTrue(results.contains(undergraduate));
    }

    @Test
    public void testGt() {
        List<Dictionary> results = instance.gt("sortOrder", 150).list();
        assertFalse(results.contains(male));
        assertFalse(results.contains(female));
        assertTrue(results.contains(undergraduate));
    }

    @Test
    public void testLe() {
        List<Dictionary> results = instance.le("sortOrder", 150).list();
        assertTrue(results.contains(male));
        assertTrue(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testLt() {
        List<Dictionary> results = instance.lt("sortOrder", 150).list();
        assertTrue(results.contains(male));
        assertFalse(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testEqProp() {
        List<Dictionary> results = instance.eqProp("code", "parentCode").list();
        assertTrue(results.contains(male));
        assertFalse(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testNotEqProp() {
        List<Dictionary> results = instance.notEqProp("code", "parentCode").list();
        assertFalse(results.contains(male));
        assertTrue(results.contains(female));
        assertTrue(results.contains(undergraduate));
    }

    @Test
    public void testGtProp() {
        List<Dictionary> results = instance.gtProp("code", "parentCode").list();
        assertFalse(results.contains(male));
        assertTrue(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testGeProp() {
        List<Dictionary> results = instance.geProp("code", "parentCode").list();
        assertTrue(results.contains(male));
        assertTrue(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testLtProp() {
        List<Dictionary> results = instance.ltProp("code", "parentCode").list();
        assertFalse(results.contains(male));
        assertFalse(results.contains(female));
        assertTrue(results.contains(undergraduate));
    }

    @Test
    public void testLeProp() {
        List<Dictionary> results = instance.leProp("code", "parentCode").list();
        assertTrue(results.contains(male));
        assertFalse(results.contains(female));
        assertTrue(results.contains(undergraduate));
    }

    @Test
    public void testSizeEq() {
        instance = repository.createCriteriaQuery(DictionaryCategory.class).sizeEq("dictionaries", 2);
        List<DictionaryCategory> results = instance.list();
        assertTrue(results.contains(gender));
        assertFalse(results.contains(education));
    }

    @Test
    public void testSizeNotEq() {
        instance = repository.createCriteriaQuery(DictionaryCategory.class).sizeNotEq("dictionaries", 2);
        List<DictionaryCategory> results = instance.list();
        assertFalse(results.contains(gender));
        assertTrue(results.contains(education));
    }

    @Test
    public void testSizeGt() {
        instance = repository.createCriteriaQuery(DictionaryCategory.class).sizeGt("dictionaries", 1);
        List<DictionaryCategory> results = instance.list();
        assertTrue(results.contains(gender));
        assertFalse(results.contains(education));
    }

    @Test
    public void testSizeGe() {
        instance = repository.createCriteriaQuery(DictionaryCategory.class).sizeGe("dictionaries", 2);
        List<DictionaryCategory> results = instance.list();
        assertTrue(results.contains(gender));
        assertFalse(results.contains(education));
    }

    @Test
    public void testSizeLt() {
        instance = repository.createCriteriaQuery(DictionaryCategory.class).sizeLt("dictionaries", 2);
        List<DictionaryCategory> results = instance.list();
        assertFalse(results.contains(gender));
        assertTrue(results.contains(education));
    }

    @Test
    public void testSizeLe() {
        instance = repository.createCriteriaQuery(DictionaryCategory.class).sizeLe("dictionaries", 2);
        List<DictionaryCategory> results = instance.list();
        assertTrue(results.contains(gender));
        assertTrue(results.contains(education));
    }

    @Test
    public void testIsEmpty() {
        DictionaryCategory empty = createCategory("a", 3);
        instance = repository.createCriteriaQuery(DictionaryCategory.class);
        List<DictionaryCategory> results = instance.isEmpty("dictionaries").list();
        assertTrue(results.contains(empty));
        assertFalse(results.contains(gender));
        assertFalse(results.contains(education));
    }

    @Test
    public void testNotEmpty() {
        DictionaryCategory empty = createCategory("a", 3);
        instance = repository.createCriteriaQuery(DictionaryCategory.class);
        List<DictionaryCategory> results = instance.notEmpty("dictionaries").list();
        assertFalse(results.contains(empty));
        assertTrue(results.contains(gender));
        assertTrue(results.contains(education));
    }

    @Test
    public void testContainsText() {
        List<Dictionary> results = instance.containsText("text", "科").list();
        assertTrue(results.contains(undergraduate));
        assertFalse(results.contains(male));
        assertFalse(results.contains(female));
    }

    @Test
    public void testStartsWithText() {
        List<Dictionary> results = instance.startsWithText("text", "本").list();
        assertTrue(results.contains(undergraduate));

        instance = repository.createCriteriaQuery(Dictionary.class).startsWithText("text", "科");
        results = instance.list();
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testInEntity() {
        Set<DictionaryCategory> params = new HashSet<DictionaryCategory>();
        params.add(education);
        params.add(gender);
        List<Dictionary> results = instance.in("category", params).list();
        assertTrue(results.contains(male));
        assertTrue(results.contains(female));
        assertTrue(results.contains(undergraduate));
    }

    @Test
    public void testInString() {
        Set<String> params = new HashSet<String>();
        params.add("男");
        params.add("女");
        List<Dictionary> results = instance.in("text", params).list();
        assertTrue(results.contains(male));
        assertTrue(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testInNull() {
        Collection<Object> value = null;
        List<Dictionary> results = instance.in("id", value).list();
        assertTrue(results.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInEmpty() {
        List<Dictionary> results = instance.in("id", Collections.EMPTY_LIST).list();
        assertTrue(results.isEmpty());
    }

    @Test
    public void testNotInEntity() {
        Set<Long> params = new HashSet<Long>();
        params.add(male.getId());
        params.add(female.getId());
        List<Dictionary> results = instance.notIn("id", params).list();
        assertFalse(results.contains(male));
        assertFalse(results.contains(female));
        assertTrue(results.contains(undergraduate));
    }

    @Test
    public void testNotInString() {
        Set<String> params = new HashSet<String>();
        params.add("男");
        params.add("女");
        List<Dictionary> results = instance.notIn("text", params).list();
        assertFalse(results.contains(male));
        assertFalse(results.contains(female));
        assertTrue(results.contains(undergraduate));
    }

    @Test
    public void testNotInNull() {
        Collection<Object> value = null;
        List<Dictionary> results = instance.notIn("id", value).list();
        assertFalse(results.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNotInEmpty() {
        List<Dictionary> results = instance.notIn("id", Collections.EMPTY_LIST).list();
        assertFalse(results.isEmpty());
    }

    @Test
    public void testIsNull() {
        List<Dictionary> results = instance.isNull("description").list();
        assertTrue(results.contains(male));
        assertTrue(results.contains(female));
        assertTrue(results.contains(undergraduate));
    }

    @Test
    public void testNotNull() {
        List<Dictionary> results = instance.notNull("text").list();
        assertTrue(results.contains(male));
        assertTrue(results.contains(female));
        assertTrue(results.contains(undergraduate));
    }

    @Test
    public void testBetween() {
        List<Dictionary> results = instance.between("parentCode", "01", "02").list();
        assertTrue(results.contains(male));
        assertTrue(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testAnd() {
        List<Dictionary> results = instance.and(criterions.eq("code", "01"), criterions.eq("category", gender)).list();
        assertTrue(results.contains(male));
        assertFalse(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testOr() {
        List<Dictionary> results = instance.or(criterions.eq("text", "男"), criterions.eq("sortOrder", 150)).list();
        assertTrue(results.contains(male));
        assertTrue(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testNot() {
        List<Dictionary> results = instance.not(criterions.eq("code", "01")).list();
        assertFalse(results.contains(male));
        assertTrue(results.contains(female));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testFindPaging() {
        createDictionary("08", "xyz", education, 150, "01");
        createDictionary("09", "xyy", education, 160, "02");
        List<Dictionary> results = instance.setFirstResult(1).setMaxResults(2).list();
        assertEquals(2, results.size());
    }

    @Test
    public void testFindOrder() {
        instance.asc("sortOrder");
        List<Dictionary> results = instance.asc("sortOrder").list();
        assertTrue(results.indexOf(male) < results.indexOf(female));
        assertTrue(results.indexOf(female) < results.indexOf(undergraduate));

        instance = repository.createCriteriaQuery(Dictionary.class).desc("sortOrder");
        results = instance.list();
        assertTrue(results.indexOf(male) > results.indexOf(female));
        assertTrue(results.indexOf(female) > results.indexOf(undergraduate));
    }

    //@Test
    public void testAlias() {
        List<Dictionary> results = repository.find(instance.eq("category.name", education));
        Dictionary graduate = Dictionary.get(4L);
        assertTrue(results.contains(graduate));
        Dictionary doctor = Dictionary.get(46L);
        assertFalse(results.contains(doctor));
    }

    private DictionaryCategory createCategory(String name, int sortOrder) {
        DictionaryCategory category = new DictionaryCategory();
        category.setName(name);
        category.setSortOrder(sortOrder);
        entityManager.persist(category);
        return category;
    }

    private Dictionary createDictionary(String code, String text, DictionaryCategory category, int sortOrder,
                                        String parentCode) {
        Dictionary dictionary = new Dictionary(code, text, category);
        dictionary.setSortOrder(sortOrder);
        dictionary.setParentCode(parentCode);
        entityManager.persist(dictionary);
        return dictionary;
    }
}
