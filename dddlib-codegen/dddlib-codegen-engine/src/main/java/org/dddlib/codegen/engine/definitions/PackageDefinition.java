package org.dddlib.codegen.engine.definitions;

/**
 * Created by yyang on 2016/12/21.
 */
public class PackageDefinition {
    private String name;
    private String description;

    public PackageDefinition(String name) {
        this.name = name;
    }

    public PackageDefinition(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
