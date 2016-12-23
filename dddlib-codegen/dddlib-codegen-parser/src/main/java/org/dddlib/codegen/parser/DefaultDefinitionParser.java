package org.dddlib.codegen.parser;

import org.dddlib.codegen.api.ClassDefinition;
import org.dddlib.codegen.api.DefinitionParser;

import java.io.File;
import java.io.Reader;
import java.util.Set;

/**
 * Created by yyang on 2016/12/23.
 */
public abstract class DefaultDefinitionParser implements DefinitionParser {
    @Override
    public Set<ClassDefinition> parseFile(String file) {
        return null;
    }

    @Override
    public Set<ClassDefinition> parseFile(File file) {
        return null;
    }

    @Override
    public Set<ClassDefinition> parseClasspath(String filePath) {
        return null;
    }

    @Override
    public Set<ClassDefinition> parseReader(Reader in) {
        return null;
    }
}
