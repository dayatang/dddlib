package org.dddlib.codegen.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.dddlib.codegen.api.DefinitionParser;
import org.dddlib.codegen.api.ParsingException;
import org.dddlib.codegen.parser.definitions.PackageDefinition;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by yyang on 2016/12/21.
 */
public class YmlParser implements DefinitionParser {

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
    public PackageDefinition parse(Reader in) {
        final JsonNode node;
        try {
            node = objectMapper.readTree(yamlFactory.createParser(in));
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
    public PackageDefinition parseFile(String file) {
        return null;
    }
}
