package org.dayatang.utils.serializer;

import com.google.gson.*;
import org.dayatang.utils.ObjectSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by yyang on 14-9-16.
 */
public class GsonObjectSerializer implements ObjectSerializer {

    private Gson gson;

    public GsonObjectSerializer() {
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .serializeNulls().create();
    }

    @Override
    public String serialize(Object anObject) {
        return gson.toJson(anObject);
    }

    @Override
    public <T> T deserialize(String serializedString, Class<T> objectClass) {
        return gson.fromJson(serializedString, objectClass);
    }

    private class DateSerializer implements JsonSerializer<Date> {
        public JsonElement serialize(Date source, Type typeOfSource, JsonSerializationContext context) {
            return new JsonPrimitive(Long.toString(source.getTime()));
        }
    }

    private class DateDeserializer implements JsonDeserializer<Date> {
        public Date deserialize(JsonElement json, Type typeOfTarget, JsonDeserializationContext context) throws JsonParseException {
            long time = Long.parseLong(json.getAsJsonPrimitive().getAsString());
            return new Date(time);
        }
    }

}
