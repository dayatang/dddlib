package org.dddlib.codegen.engine.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.dddlib.codegen.engine.DefinitionParser;
import org.dddlib.codegen.engine.ParsingException;
import org.dddlib.codegen.engine.definitions.ClassDefinition;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public class YmlParser implements DefinitionParser {

    private YAMLFactory yamlFactory = new YAMLFactory();
    private ObjectMapper objectMapper;

    @Override
    public Set<ClassDefinition> parse(Reader in) {
        final JsonNode node;
        try {
            node = objectMapper.readTree(yamlFactory.createParser(in));
        } catch (IOException e) {
            throw new ParsingException("Cannot parse reader!");
        }
        return null;
    }
}
