package org.openkoala.koala.annotation.parse;

import java.util.List;

import org.openkoala.koala.action.XmlParse;
import org.openkoala.koala.action.XmlParseUtil;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.parse.XML2ObjectUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class ParseMappedFunctionCreate  implements Parse {

//	private static final Logger logger = LoggerFactory.getLogger(ParseMappedFunctionCreate.class);
	// 参数值，每个Parse都会有的
	private List params;

	private String name;

	public void initParms(List params, String name, Object fieldVal) {
		this.params = params;
		this.name = name;
	}

	public void process() throws Exception {
		String xmlPath = "xml/MappedFunctionCreate/"+name+".xml";
		XmlParseUtil.parseXml(xmlPath, params);
	}

}
