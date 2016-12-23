package org.dddlib.codegen.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import org.dddlib.codegen.api.ClassDefinition;
import org.dddlib.codegen.api.ParsingException;
import org.dddlib.codegen.classdef.PackageDefinition;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public class JsonParser extends DefaultDefinitionParser {
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
    public Set<ClassDefinition> parseReader(Reader in) {
        final JsonNode node;
        try {
            node = objectMapper.readTree(jsonFactory.createParser(in));
            PackageDefinition packageDefinition = objectMapper.readValue(new TreeTraversingParser(node), PackageDefinition.class);
            return toClassDefinitions(packageDefinition);
        } catch (IOException e) {
            throw new ParsingException("Cannot parse reader!");
        }
    }

    @Override
    public Set<ClassDefinition> parseClasspath(String file) {
        return null;
    }


    @Override
    public boolean accept(String ext) {
        return "yml".equals(ext);
    }

    @Override
    public Set<ClassDefinition> parseFile(String file) {
        return null;
    }

    @Override
    public Set<ClassDefinition> parseFile(File file) {
        return null;
    }

    private Set<ClassDefinition> toClassDefinitions(PackageDefinition packageDefinition) {
        Set<ClassDefinition> results = new HashSet<ClassDefinition>();
        results.add(packageDefinition);
        results.addAll(packageDefinition.getClasses());
        return results;
    }
}
