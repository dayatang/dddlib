package org.dddlib.codegen.engine;

import org.dddlib.codegen.api.DomainClassGenerator;
import org.dddlib.codegen.api.JavaSourceFile;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public class DefaultDomainClassGenerator implements DomainClassGenerator {
    @Override
    public Set<JavaSourceFile> generate(File file) {
        return null;
    }

    @Override
    public Set<JavaSourceFile> generate(InputStream in) {
        return null;
    }

    @Override
    public Set<JavaSourceFile> generate(Reader in) {
        return null;
    }
}
