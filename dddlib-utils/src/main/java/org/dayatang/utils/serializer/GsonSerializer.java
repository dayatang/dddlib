package org.dayatang.utils.serializer;

import com.google.gson.*;
import org.dayatang.utils.ObjectSerializer;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by yyang on 14-9-16.
 */
public class GsonSerializer implements ObjectSerializer {

    private Gson gson;

    public GsonSerializer() {
        this.gson = new GsonBuilder().create();
    }

    public GsonSerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String serialize(Object anObject) {
        return gson.toJson(anObject);
    }

    @Override
    public <T> T deserialize(String serializedString, Class<T> objectClass) {
        return gson.fromJson(serializedString, objectClass);
    }

}
