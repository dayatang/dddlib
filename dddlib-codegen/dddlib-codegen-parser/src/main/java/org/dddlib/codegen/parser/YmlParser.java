package org.dddlib.codegen.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.dddlib.codegen.classdef.ClassDefinition;
import org.dddlib.codegen.api.ParsingException;
import org.dddlib.codegen.classdef.PackageDefinition;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public class YmlParser extends AbstractDefinitionParser {

    private YAMLFactory yamlFactory;
    private ObjectMapper objectMapper;

    public YmlParser() {
        yamlFactory = new YAMLFactory();
        objectMapper = new ObjectMapper(yamlFactory);
    }

    public YmlParser(YAMLFactory yamlFactory, ObjectMapper objectMapper) {
        this.yamlFactory = yamlFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    public Set<ClassDefinition> parseReader(Reader in) {
        final JsonNode node;
        try {
            node = objectMapper.readTree(yamlFactory.createParser(in));
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
    public Set<ClassDefinition> parseFile(String file) {
        return null;
    }

    @Override
    public Set<ClassDefinition> parseFile(File file) {
        return null;
    }

    @Override
    public boolean accept(String ext) {
        return "yml".equals(ext);
    }
}
