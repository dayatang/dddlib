package org.dayatang.utils;

import com.google.common.collect.Sets;
import org.dayatang.utils.serializer.GsonObjectSerializer;
import org.dayatang.utils.support.Dictionary;
import org.dayatang.utils.support.DictionaryCategory;
import org.dayatang.utils.support.DomainEventSub;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

/**
 * Created by yyang on 14-9-14.
 */
public class GsonObjectSerializerTest {

    private GsonObjectSerializer instance;

    private DictionaryCategory category;

    private Dictionary dictionary1;

    private Dictionary dictionary2;

    private DomainEventSub event;

    @Before
    public void setUp() throws Exception {
        instance = new GsonObjectSerializer();
        Date occurredOn = DateUtils.date(2002, 4, 11);
        event = new DomainEventSub(occurredOn, 1);
        event.setId("anId");
        event.setProp1("abc");
    }

    @Test
    public void testSerialize() throws Exception {
        String result = "{\"prop1\":\"abc\",\"prop2\":null,\"id\":\"anId\",\"occurredOn\":\"1018454400000\",\"version\":1}";
        assertThat(instance.serialize(event), is(result));
    }

    @Test
    public void testDeserialize() throws Exception {
        String eventBody = "{\"prop1\":\"abc\",\"prop2\":null,\"id\":\"anId\",\"occurredOn\":\"1018454400000\",\"version\":1}";
        DomainEventSub result = instance.deserialize(eventBody, DomainEventSub.class);
        assertThat(result.getOccurredOn(), is(event.getOccurredOn()));
        assertThat(result.getProp1(), is(event.getProp1()));
        assertThat(result.getVersion(), is(event.getVersion()));
        assertNull(result.getProp2());
    }


    @Test
    public void test() {
        DictionaryCategory category = new DictionaryCategory();
        category.setName("a category");
        category.setSortOrder(1);
        category.setId(3L);
        Dictionary dictionary1 = new Dictionary("01", "男", category);
        Dictionary dictionary2 = new Dictionary("01", "男", category);
        category.setDictionaries(Sets.newHashSet(dictionary1, dictionary2));
        System.out.println(instance.serialize(category));
    }

}
