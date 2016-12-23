package org.dddlib.codegen.parser;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.dddlib.codegen.api.DefinitionParser;
import org.dddlib.codegen.api.ParsingException;
import org.dddlib.codegen.parser.definitions.PackageDefinition;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by yyang on 2016/12/21.
 */
public class XmlParser implements DefinitionParser {

    private XmlFactory xmlFactory;

    private XmlMapper xmlMapper;

    public XmlParser() {
        xmlFactory = new XmlFactory();
        xmlMapper = new XmlMapper(xmlFactory, new JacksonXmlModule());
    }

    @Override
    public PackageDefinition parse(Reader in) {
        try {
            XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(in);
            return xmlMapper.readValue(reader, PackageDefinition.class);
        } catch (XMLStreamException e) {
            throw new ParsingException("Cannot parse reader!");
        } catch (IOException e) {
            throw new ParsingException("Cannot parse reader!");
        }
    }

    @Override
    public PackageDefinition parseClasspath(String file) {
        return null;
    }

    @Override
    public PackageDefinition parseFile(String file) {
        return null;
    }
}
