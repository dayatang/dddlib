package org.openkoala.koala.action;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.parse.XML2ObjectUtil;

/**
 * 通用XML解析
 * @author lingen
 *
 */
public class XmlParseUtil {

	/**
	 * 传入一个XML文件,传入参数,对XML进行解析
	 * @param xmlFile
	 * @param properties
	 * @throws KoalaException
	 */
	public static  void parseXmlAction(String xmlFile,VelocityContext context) throws KoalaException{
		try {
			XmlParse parse = (XmlParse) XML2ObjectUtil.getInstance().processParse(xmlFile);
			parse.parse(context);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KoalaException(e.getMessage());
		}
	}
}
