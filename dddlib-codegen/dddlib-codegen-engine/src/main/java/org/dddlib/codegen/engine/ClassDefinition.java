package org.dddlib.codegen.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *领域类定义
 * Created by yyang on 2016/12/21.
 */
public abstract class ClassDefinition {
    private String packageName;
    private String className;
    private boolean isAbstract = false;
    private Set<String> pkProps = new HashSet<String>();
    private Set<String> uniqueProps = new HashSet<String>();
    private List<FieldDefinition> props = new ArrayList<FieldDefinition>();
}
