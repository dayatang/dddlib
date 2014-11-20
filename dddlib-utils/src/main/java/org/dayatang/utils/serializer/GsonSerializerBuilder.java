package org.dayatang.utils.serializer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yyang on 14/11/9.
 */
public class GsonSerializerBuilder {
    private GsonBuilder builder;

    public GsonSerializerBuilder() {
        builder = new GsonBuilder();
    }

    public GsonSerializerBuilder(GsonBuilder builder) {
        this.builder = builder;
    }

    public GsonSerializerBuilder serializeNulls() {
        return new GsonSerializerBuilder(builder.serializeNulls());
    }

    public GsonSerializerBuilder excludeFieldsNamed(String... fieldNames) {
        return new GsonSerializerBuilder(builder
                .addSerializationExclusionStrategy(new FieldNameExclusionStrategy(fieldNames)));
    }

    public GsonSerializerBuilder excludeFieldsWithModifiers(int... modifiers) {
        return new GsonSerializerBuilder(builder
                .excludeFieldsWithModifiers(modifiers));
    }

    public GsonSerializerBuilder addSerializationExclusionStrategy(ExclusionStrategy strategy) {
        return new GsonSerializerBuilder(builder
                .addSerializationExclusionStrategy(strategy));
    }

    public GsonSerializerBuilder addDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        return new GsonSerializerBuilder(builder
                .addDeserializationExclusionStrategy(strategy));
    }

    public GsonSerializerBuilder registerTypeAdapter(Type type, Object typeAdapter) {
        return new GsonSerializerBuilder(builder.registerTypeAdapter(type, typeAdapter));
    }

    public GsonSerializerBuilder prettyPrinting() {
        return new GsonSerializerBuilder(builder.setPrettyPrinting());
    }

    public GsonSerializerBuilder dateFormat(String pattern) {
        return new GsonSerializerBuilder(builder.setDateFormat(pattern));
    }

    public GsonSerializer build() {
        return new GsonSerializer(builder.create());
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
