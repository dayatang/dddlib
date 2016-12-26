package org.dddlib.codegen.parser;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.dddlib.codegen.classdef.ClassDefinition;
import org.dddlib.codegen.api.ParsingException;
import org.dddlib.codegen.classdef.PackageDefinition;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public class XmlParser extends AbstractDefinitionParser {

    private XmlFactory xmlFactory;

    private XmlMapper xmlMapper;

    public XmlParser() {
        xmlFactory = new XmlFactory();
        xmlMapper = new XmlMapper(xmlFactory, new JacksonXmlModule());
    }

    @Override
    public Set<ClassDefinition> parseReader(Reader in) {
        try {
            XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(in);
            PackageDefinition packageDefinition = xmlMapper.readValue(reader, PackageDefinition.class);
            return toClassDefinitions(packageDefinition);
        } catch (XMLStreamException e) {
            throw new ParsingException("Cannot parse reader!");
        } catch (IOException e) {
            throw new ParsingException("Cannot parse reader!");
        }
    }

    @Override
    public Set<ClassDefinition> parseClasspath(String file) {
        return null;
    }


    @Override
    public Set<ClassDefinition> parseFile(String file) {
        return null;
    }

    @Override
    public Set<ClassDefinition> parseFile(File file) {
        return null;
    }

    @Override
    public boolean accept(String ext) {
        return "xml".equals(ext);
    }
}
