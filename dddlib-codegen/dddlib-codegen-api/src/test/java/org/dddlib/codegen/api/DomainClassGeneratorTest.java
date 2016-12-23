package org.dddlib.codegen.api;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by yyang on 2016/12/23.
 */
public class DomainClassGeneratorTest {

    @Mock
    private DefinitionParser parser;

    @Mock
    private ClassGenerator generator;

    @Mock
    private ClassDefinition definition1;

    @Mock
    private ClassDefinition definition2;

    @Mock
    private JavaSourceFile sourceFile1;

    @Mock
    private JavaSourceFile sourceFile2;

    private Set<ClassDefinition> definitions;

    private Set<JavaSourceFile> sourceFiles;

    private DomainClassGenerator instance;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        instance = new DomainClassGenerator(parser, generator);

        definitions = Sets.newHashSet(definition1, definition2);


        sourceFiles = Sets.newHashSet(sourceFile1, sourceFile2);
    }

    @Test
    public void generateFromFile() throws Exception {
        String filename = getClass().getResource("/sales.yml").getFile();
        File file = new File(filename);

    }

    @Test
    public void generateFromFilename() throws Exception {

    }

    @Test
    public void generateFromClasspath() throws Exception {

    }

    @Test
    public void generateFromReader() throws Exception {

    }

}