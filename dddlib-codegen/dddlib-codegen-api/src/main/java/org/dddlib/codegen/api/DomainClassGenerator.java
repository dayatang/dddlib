package org.dddlib.codegen.api;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 领域类生成器接口。从各种文件或输入流中读取领域类定义生成Java类源文件。
 * Created by yyang on 2016/12/21.
 */
public final class DomainClassGenerator {

    private DefinitionParser parser;

    private ClassGenerator generator;

    public DomainClassGenerator(DefinitionParser parser, ClassGenerator generator) {
        this.parser = parser;
        this.generator = generator;
    }

    public Set<JavaSourceFile> generateFromFile(String file) {
        return generateFromFile(new File(file));
    }

    public Set<JavaSourceFile> generateFromFile(File file) {
        FileReader in = null;
        try {
            in = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new ParsingException("File '" + file + "' not found!");
        }
        return generateFromReader(in);
    }

    public Set<JavaSourceFile> generateFromClasspath(String filePath) {
        Reader in = new InputStreamReader(getClass().getResourceAsStream(filePath));
        return generateFromReader(in);
    }

    public Set<JavaSourceFile> generateFromReader(Reader in) {
        Set<JavaSourceFile> results = new HashSet<JavaSourceFile>();
        for (ClassDefinition definition : parser.parseReader(in)) {
            results.add(generator.generate(definition));
        }
        return results;
    }
}
