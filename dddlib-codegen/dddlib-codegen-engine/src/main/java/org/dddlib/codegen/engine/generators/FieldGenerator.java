package org.dddlib.codegen.engine.generators;

import org.dddlib.codegen.api.JavaSourceFile;
import org.dddlib.codegen.engine.definitions.FieldDefinition;

/**
 * Created by yyang on 2016/12/21.
 */
public abstract class FieldGenerator {

    private JavaSourceFile file;
    private FieldDefinition field;

    public FieldGenerator(JavaSourceFile file, FieldDefinition field) {
        this.file = file;
        this.field = field;
    }

    public void generateField() {

    }

    public void generateAccessors() {

    }
}
