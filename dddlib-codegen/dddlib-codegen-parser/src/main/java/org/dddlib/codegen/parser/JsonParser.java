package org.dddlib.codegen.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import org.dddlib.codegen.api.ClassDefinition;
import org.dddlib.codegen.api.DefinitionParser;
import org.dddlib.codegen.api.ParsingException;

import java.io.File;
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

    @Override
    public PackageDefinition parseClasspath(String file) {
        return null;
    }

    @Override
    public Set<ClassDefinition> parseReader(Reader in) {
        return null;
    }

    @Override
    public PackageDefinition parseFile(String file) {
        return null;
    }

    @Override
    public Set<ClassDefinition> parseFile(File file) {
        return null;
    }
}
