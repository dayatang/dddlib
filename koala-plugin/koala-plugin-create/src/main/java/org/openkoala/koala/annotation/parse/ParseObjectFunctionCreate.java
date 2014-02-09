package org.openkoala.koala.annotation.parse;

import java.util.List;

import org.openkoala.koala.action.XmlParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @description 解析Object对象
 *  
 * @date：      2013-8-27   
 * 
 * @version    Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author     lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class ParseObjectFunctionCreate implements
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
