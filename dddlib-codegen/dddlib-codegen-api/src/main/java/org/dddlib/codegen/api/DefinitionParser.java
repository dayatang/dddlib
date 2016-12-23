package org.dddlib.codegen.api;

import java.io.File;
import java.io.Reader;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public interface DefinitionParser {
    Set<ClassDefinition> parseFile(String file);
    Set<ClassDefinition> parseFile(File file);
    Set<ClassDefinition> parseClasspath(String filePath);
    Set<ClassDefinition> parseReader(Reader in);
}
