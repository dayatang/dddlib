package org.openkoala.koala.action;

import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.parse.XML2ObjectUtil;

/**
 * 通用XML解析
 * @author lingen
 *
 */
public class XmlParseUtil {

	
	/**
	 * 解析XML定义，传入XML定义及参数，将XML中的定义进行解析并生成，用于新增或更新项目生成文件
	 * @param xmlFile  XML文件所在的位置
	 * @param params   参数,list类型
	 */
	public static void parseXml(String xmlFile,List paramLists){
		VelocityContext context = VelocityUtil.getVelocityContext(paramLists);
		parseXmlAction(xmlFile,context);
	}
	
	
	
	/**
	 * 解析XML定义，传入XML定义及参数，将XML中的定义进行解析并生成，用于新增或更新项目生成文件
	 * @param xmlFile  XML文件所在的位置
	 * @param params   参数,Map类型
	 */
	public static void parseXml(String xmlFile,Map<String,Object> paramMaps){
		VelocityContext context = VelocityUtil.getVelocityContext(paramMaps);
		parseXmlAction(xmlFile,context);
	}
	
	
	/**
	 * 传入一个XML文件,传入参数,对XML进行解析
	 * @param xmlFile
	 * @param properties
	 * @throws KoalaException
	 */
	 private static  void parseXmlAction(String xmlFile,VelocityContext context) throws KoalaException{
		try {
			XmlParse parse = (XmlParse) XML2ObjectUtil.getInstance().processParse(xmlFile);
			parse.parse(context);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KoalaException(e.getMessage());
		}
	}
}
