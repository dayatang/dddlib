package org.openkoala.koala.annotation.parse;

import java.util.List;

import org.openkoala.koala.action.XmlParse;
import org.openkoala.koala.action.XmlParseUtil;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.parse.XML2ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseObjectFunctionCreate extends AbstractXmlParse implements
		Parse {

	private static final Logger logger = LoggerFactory
			.getLogger(ParseObjectFunctionCreate.class);

	public String name;

	public List params;

	public void initParms(List params, String name, Object fieldVal) {
		this.params = params;
		this.name = name;
	}

	public void process() throws Exception {
		logger.info("解析类对象【" + name + "】");
		String xmlPath = "xml/ObjectFunctionCreate/" + name + ".xml";
		XmlParseUtil.parseXml(xmlPath, params);
		logger.info("类对象【" + name + "】解析成功");
	}

}
