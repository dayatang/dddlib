package org.openkoala.jbpm.infra;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * XML解析的类
 * @author lingen
 *
 */
public class XmlParseUtil {

	/**
	 * 把返回的XML值解析成List值的模式
	 * @param xml
	 * @return
	 */
	public static List<String> parseListXml(String xml){
		SAXReader reader = new SAXReader();
		Document document = null;
		List<String> values = new ArrayList<String>();
		try {
			document = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			for(Element element:elements){
				values.add(element.getTextTrim());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return values;
	}
	/**
	 * 将指定的参数解析成一个XML格式
	 * @param paramsMap
	 * @return
	 */
	public static String paramsToXml(Map<String,Object> paramsMap){
		if(paramsMap==null || paramsMap.size()==0)return "";
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("params");
		Set<String> keys = paramsMap.keySet();
		for(String key:keys){
			String value = paramsMap.get(key).toString();
			Element param = root.addElement(key);
			param.setText(value);
		}
		return document.asXML();
	}
	
	/**
	 * 将传入的XML格式，解析成为
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> xmlToPrams(String xml){
		Map<String,Object> params = new HashMap<String,Object>();
		if(xml==null || xml.equals(""))return params;
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			for(Element element:elements){
				String key = element.getName();
				String value = element.getTextTrim();
				Object val = value;
				if("int".equals(element.attribute("type"))){
					val = Integer.parseInt(value);
				}
				if("long".equals(element.attribute("type"))){
					val = Long.parseLong(value);
				}
				if("float".equals(element.attribute("type"))){
					val = Float.parseFloat(value);
				}
				if("double".equals(element.attribute("type"))){
					val = Double.parseDouble(value);
				}
				if("boolean".equals(element.attribute("type"))){
					val = Boolean.valueOf(value);
				}
				params.put(key, val);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return params;
	}
	
	public static void main(String args[]){
		String xml = "<params><creater>lingen</creater><isOpen>true</isOpen></params>";
		System.out.println(XmlParseUtil.xmlToPrams(xml));
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("creter", "lingen");
		params.put("you", "aaa");
		System.out.println(XmlParseUtil.paramsToXml(params));
	}
	
}