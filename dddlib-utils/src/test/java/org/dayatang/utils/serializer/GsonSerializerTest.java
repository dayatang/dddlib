package org.dayatang.utils.serializer;

import com.google.common.collect.Sets;
import com.google.gson.*;
import org.dayatang.utils.DateUtils;
import org.dayatang.utils.support.Dictionary;
import org.dayatang.utils.support.DictionaryCategory;
import org.dayatang.utils.support.DomainEventSub;
import org.dayatang.utils.support.DomainEventSubDto;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

/**
 * Created by yyang on 14-9-14.
 */
public class GsonSerializerTest {

    private GsonSerializerBuilder builder;

    private GsonSerializer instance;

    private DictionaryCategory category;

    private Dictionary dictionary1;

    private Dictionary dictionary2;

    private DomainEventSub event;

    @Before
    public void setUp() throws Exception {
        builder = new GsonSerializerBuilder().dateFormat("yyyy-MM-dd");
        instance = builder.build();
        Date occurredOn = DateUtils.date(2002, 4, 11);
        event = new DomainEventSub(occurredOn, 1);
        event.setId("anId");
        event.setProp1("abc");
        event.setTransientField("transient");
        event.setLastModified(DateUtils.date(2002, 12, 11));
    }

    @Test
    public void notSerializeNulls() throws Exception {
        String result = "{\"prop1\":\"abc\",\"lastModified\":\"2002-12-11\",\"id\":\"anId\",\"occurredOn\":\"2002-04-11\",\"version\":1}";
        assertThat(instance.serialize(event), is(result));
    }

    @Test
    public void serializeNulls() {
        instance = builder.serializeNulls().build();
        String result = "{\"prop1\":\"abc\",\"prop2\":null,\"lastModified\":\"2002-12-11\",\"id\":\"anId\",\"occurredOn\":\"2002-04-11\",\"version\":1}";
        assertThat(instance.serialize(event), is(result));
    }

    @Test
    public void prettyPrinting() {
        instance = builder.prettyPrinting().build();
        String lineSeparator = System.getProperty("line.separator");
        String result = String.format(
                "{\n  \"prop1\": \"abc\",\n  \"lastModified\": \"2002-12-11\",\n  \"id\": \"anId\",\n  \"occurredOn\": \"2002-04-11\",\n  \"version\": 1\n}",
            lineSeparator, lineSeparator, lineSeparator, lineSeparator, lineSeparator);
        assertThat(instance.serialize(event), is(result));

    }

    @Test
    public void excludeFieldsNamed() throws Exception {
        String result = "{\"prop1\":\"abc\",\"lastModified\":\"2002-12-11\",\"id\":\"anId\",\"version\":1}";
        instance = builder.excludeFieldsNamed("occurredOn").build();
        assertThat(instance.serialize(event), is(result));
    }

    @Test
    public void excludeFieldsWithModifiers() {
        instance = builder.excludeFieldsWithModifiers(Modifier.PROTECTED, Modifier.TRANSIENT).build();
        String result = "{\"prop1\":\"abc\",\"id\":\"anId\",\"occurredOn\":\"2002-04-11\",\"version\":1}";
        assertThat(instance.serialize(event), is(result));
    }

    @Test
    public void deserialize() throws Exception {
        String eventBody = "{\"prop1\":\"abc\",\"prop2\":null,\"lastModified\":\"2002-12-11\",\"id\":\"anId\",\"occurredOn\":\"2002-04-11\",\"version\":1}";
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

    @Test
    public void registerTypeAdapter() {
        builder = builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date source, Type typeOfSource, JsonSerializationContext context) {
                return new JsonPrimitive(source.getTime());
            }
        });
        instance = builder.build();
        String result = "{\"prop1\":\"abc\",\"lastModified\":1039536000000,\"id\":\"anId\",\"occurredOn\":1018454400000,\"version\":1}";
        assertThat(instance.serialize(event), is(result));
    }

    @Test
    public void addSerializationExclusionStrategy() {
        builder = builder.addSerializationExclusionStrategy(new FieldNameExclusionStrategy("occurredOn"));
        String result = "{\"prop1\":\"abc\",\"lastModified\":\"2002-12-11\",\"id\":\"anId\",\"version\":1}";
        instance = builder.build();
        assertThat(instance.serialize(event), is(result));
    }

    @Test
    public void addDeserializationExclusionStrategy() {
        builder = builder.addDeserializationExclusionStrategy(new FieldNameExclusionStrategy("version"));
        instance = builder.build();
        String eventBody = "{\"prop1\":\"abc\",\"lastModified\":\"2002-12-11\",\"id\":\"anId\",\"occurredOn\":\"2002-04-11\",\"version\":100}";
        DomainEventSub result = instance.deserialize(eventBody, DomainEventSub.class);
        assertThat(result.getOccurredOn(), is(DateUtils.date(2002, 4, 11)));
        assertThat(result.getLastModified(), is(DateUtils.date(2002, 12, 11)));
        assertThat(result.getProp1(), is("abc"));
        assertNull(result.getProp2());
        assertThat(result.getId(), is("anId"));
        assertThat(result.getVersion(), is(not(100)));
    }

    @Test
    public void beanCopy() {
        DomainEventSubDto dto = new DomainEventSubDto();
        dto.setId("anId");
        dto.setTransientField("transient");
        dto.setOccurredOn("2002-04-11");
        dto.setProp1("abc");
        dto.setVersion("234");
        DomainEventSub event = instance.deserialize(instance.serialize(dto), DomainEventSub.class);
        assertThat(event.getId(), is("anId"));
        assertThat(event.getOccurredOn(), is(DateUtils.date(2002, 4, 11)));
        assertThat(event.getTransientField(), nullValue());
        assertThat(event.getProp1(), is("abc"));
        assertThat(event.getProp2(), nullValue());
        assertThat(event.getVersion(), is(234));
    }


    private static class FieldNameExclusionStrategy implements ExclusionStrategy {

        private List<String> fieldNames;

        private FieldNameExclusionStrategy(String... fieldNames) {
            this.fieldNames = Arrays.asList(fieldNames);
        }

        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldNames.contains(fieldAttributes.getName());
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }
}

