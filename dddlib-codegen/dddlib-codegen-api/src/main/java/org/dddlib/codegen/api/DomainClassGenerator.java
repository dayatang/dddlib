package org.dddlib.codegen.api;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 领域类生成器接口。从各种文件或输入流中读取领域类定义生成Java类源文件。
 * Created by yyang on 2016/12/21.
 */
public final class DomainClassGenerator {

    private Set<DefinitionParser> parsers;

    private ClassGenerator generator;

    public DomainClassGenerator(Set<DefinitionParser> parsers, ClassGenerator generator) {
        this.parsers = parsers;
        this.generator = generator;
    }

    public Set<JavaSourceFile> generateFromFile(String file) {
        String ext = getExtOfFile(file);
        return generateFrom(parserOf(ext).parseFile(file));
    }

    public Set<JavaSourceFile> generateFromFile(File file) {
        String ext = getExtOfFile(file.getName());
        return generateFrom(parserOf(ext).parseFile(file));
    }

    public Set<JavaSourceFile> generateFromClasspath(String filePath) {
        String ext = getExtOfFile(filePath);
        return generateFrom(parserOf(ext).parseClasspath(filePath));
    }

    public Set<JavaSourceFile> generateFromReader(Reader in, String type) {
        return generateFrom(parserOf(type).parseReader(in));
    }

    private Set<JavaSourceFile> generateFrom(Set<ClassDefinition> definitions) {
        Set<JavaSourceFile> results = new HashSet<JavaSourceFile>();
        for (ClassDefinition definition : definitions) {
            results.add(generator.generate(definition));
        }
        return results;
    }

    private DefinitionParser parserOf(String ext) {
        for (DefinitionParser parser : parsers) {
            if (parser.accept(ext)) {
                return parser;
            }
        }
        throw new UnsupportedFileFormatException("没有解析器支持文件类型." + ext);
    }

    private String getExtOfFile(String filename) {
        int pos = filename.lastIndexOf(".");
        if (pos < 0) {
            return "";
        }
        return filename.substring(pos + 1);
    }
}
