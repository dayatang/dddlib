package org.dayatang.utils.serializer;

import com.google.gson.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by yyang on 14/11/9.
 */
public class GsonObjectSerializerBuilder {
    private GsonBuilder builder;

    public GsonObjectSerializerBuilder() {
        builder = new GsonBuilder();
    }

    public GsonObjectSerializerBuilder(GsonBuilder builder) {
        this.builder = builder;
    }

    public GsonObjectSerializerBuilder serializeNulls() {
        return new GsonObjectSerializerBuilder(builder.serializeNulls());
    }

    public GsonObjectSerializerBuilder excludeFieldsNamed(String... fieldNames) {
        return new GsonObjectSerializerBuilder(builder
                .addSerializationExclusionStrategy(new FieldNameExclusionStrategy(fieldNames)));
    }

    public GsonObjectSerializerBuilder excludeFieldsWithModifiers(int... modifiers) {
        return new GsonObjectSerializerBuilder(builder
                .excludeFieldsWithModifiers(modifiers));
    }

    public GsonObjectSerializerBuilder addSerializationExclusionStrategy(ExclusionStrategy strategy) {
        return new GsonObjectSerializerBuilder(builder
                .addSerializationExclusionStrategy(strategy));
    }

    public GsonObjectSerializerBuilder addDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        return new GsonObjectSerializerBuilder(builder
                .addDeserializationExclusionStrategy(strategy));
    }

    public GsonObjectSerializerBuilder registerTypeAdapter(Type type, Object typeAdapter) {
        return new GsonObjectSerializerBuilder(builder.registerTypeAdapter(type, typeAdapter));
    }

    public GsonObjectSerializerBuilder prettyPrinting() {
        return new GsonObjectSerializerBuilder(builder.setPrettyPrinting());
    }

    public GsonObjectSerializerBuilder dateFormat(String pattern) {
        return new GsonObjectSerializerBuilder(builder.setDateFormat(pattern));
    }

    public GsonObjectSerializer build() {
        return new GsonObjectSerializer(builder.create());
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
