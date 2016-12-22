package org.dddlib.codegen.engine.parsers;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.dddlib.codegen.engine.DefinitionParser;
import org.dddlib.codegen.engine.ParsingException;
import org.dddlib.codegen.engine.definitions.PackageDefinition;

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
}
