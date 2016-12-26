package org.dddlib.codegen.parser;

import org.dddlib.codegen.classdef.ClassDefinition;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by yyang on 2016/12/23.
 */
public class YmlParserTest {

    private YmlParser instance;

    @Before
    public void setUp() throws Exception {
        instance = new YmlParser();
    }

    @Test
    public void parseReader() throws Exception {

    }

    @Test
    public void parseClasspath() throws Exception {
        Set<ClassDefinition> classDefinitions = instance.parseClasspath("/products.yml");

    }

    @Test
    public void parseFile() throws Exception {

    }

    @Test
    public void parseFileName() throws Exception {

    }

    @Test
    public void accept() throws Exception {
        assertThat(instance.accept("yml")).isTrue();
        assertThat(instance.accept("json")).isFalse();
    }

}