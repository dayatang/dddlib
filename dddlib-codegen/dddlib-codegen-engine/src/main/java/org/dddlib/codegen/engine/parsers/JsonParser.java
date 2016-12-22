package org.dddlib.codegen.engine.parsers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import org.dddlib.codegen.engine.DefinitionParser;
import org.dddlib.codegen.engine.ParsingException;
import org.dddlib.codegen.engine.definitions.ClassDefinition;
import org.dddlib.codegen.engine.definitions.PackageDefinition;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public class JsonParser implements DefinitionParser {
    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;

    public JsonParser() {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper();
    }

    public JsonParser(JsonFactory jsonFactory, ObjectMapper objectMapper) {
        this.jsonFactory = jsonFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    public PackageDefinition parse(Reader in) {
        final JsonNode node;
        try {
            node = objectMapper.readTree(jsonFactory.createParser(in));
            return objectMapper.readValue(new TreeTraversingParser(node), PackageDefinition.class);
        } catch (IOException e) {
            throw new ParsingException("Cannot parse reader!");
        }
    }
}
