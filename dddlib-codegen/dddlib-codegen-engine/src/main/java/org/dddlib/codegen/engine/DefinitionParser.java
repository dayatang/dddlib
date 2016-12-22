package org.dddlib.codegen.engine;

import org.dddlib.codegen.engine.definitions.ClassDefinition;
import org.dddlib.codegen.engine.definitions.PackageDefinition;

import java.io.Reader;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public interface DefinitionParser {
    PackageDefinition parse(Reader in);
}
