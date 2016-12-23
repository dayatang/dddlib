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
        return generateFrom(parser.parseFile(file));
    }

    public Set<JavaSourceFile> generateFromFile(File file) {
        return generateFrom(parser.parseFile(file));
    }

    public Set<JavaSourceFile> generateFromClasspath(String filePath) {
        return generateFrom(parser.parseClasspath(filePath));
    }

    public Set<JavaSourceFile> generateFromReader(Reader in) {
        return generateFrom(parser.parseReader(in));
    }

    private Set<JavaSourceFile> generateFrom(Set<ClassDefinition> definitions) {
        Set<JavaSourceFile> results = new HashSet<JavaSourceFile>();
        for (ClassDefinition definition : definitions) {
            results.add(generator.generate(definition));
        }
        return results;
    }
}
