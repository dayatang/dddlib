package org.openkoala.koala.monitor.toolkit.packagescan;

/**
 * Compiles simple wildcard patterns
 */
public class SimpleWildcardPatternFactory implements PatternFactory {
    public CompiledPattern compile(String pattern) {
        return new SimpleWildcardPattern(pattern);
    }
}
