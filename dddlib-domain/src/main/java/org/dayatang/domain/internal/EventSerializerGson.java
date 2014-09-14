package org.dayatang.domain.internal;

import com.google.gson.*;
import org.dayatang.domain.event.DomainEvent;
import org.dayatang.domain.event.EventSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by yyang on 14-9-14.
 */
public class EventSerializerGson implements EventSerializer {

    private Gson gson;

    public EventSerializerGson() {
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .serializeNulls().create();
    }

    /*
    public EventSerializerGson(boolean isCompact) {
        this(false, isCompact);
    }

    public EventSerializerGson(boolean isPretty, boolean isCompact) {
        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer());
        if (isPretty && isCompact) {
            gson = builder.setPrettyPrinting().create();
        } else if (isCompact) {
            gson = builder.create();
        } else {
            gson = builder.serializeNulls().create();
        }
    }
    */

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

    @Override
    public String serialize(DomainEvent domainEvent) {
        return gson.toJson(domainEvent);
    }

    @Override
    public <T extends DomainEvent> T deserialize(String eventBody, Class<T> domainEventClass) {
        return gson.fromJson(eventBody, domainEventClass);
    }
}
