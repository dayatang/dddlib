package org.dddlib.codegen.engine;

import org.dddlib.codegen.api.DomainClassGenerator;
import org.dddlib.codegen.api.JavaSourceFile;
import org.dddlib.codegen.engine.definitions.ClassDefinition;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public class DefaultDomainClassGenerator implements DomainClassGenerator {

    private DefinitionParser parser;

    private ClassGenerator generator;

    public DefaultDomainClassGenerator(DefinitionParser parser, ClassGenerator generator) {
        this.parser = parser;
        this.generator = generator;
    }

    @Override
    public Set<JavaSourceFile> generate(File file) {
        FileReader in = null;
        try {
            in = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new ParsingException("File '" + file + "' not found!");
        }
        return generate(in);
    }

    @Override
    public Set<JavaSourceFile> generate(String filePath) {
        FileReader in = null;
        try {
            in = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new ParsingException("File '" + filePath + "' not found!");
        }
        return generate(in);
    }

    @Override
    public Set<JavaSourceFile> generate(Reader in) {
        Set<JavaSourceFile> results = new HashSet<JavaSourceFile>();
        for (ClassDefinition definition : parser.parse(in)) {
            results.add(generator.generate(definition));
        }
        return results;
    }
}
