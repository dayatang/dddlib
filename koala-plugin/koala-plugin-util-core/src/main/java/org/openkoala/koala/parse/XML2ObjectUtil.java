package org.openkoala.koala.parse;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.action.xml.DocumentUtil;

/**
 * 将XML文件解析成为对象
 * 
 * @author lingen.liu
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class XML2ObjectUtil {

	// value标签
	private static final String VALUE_ELEMENT = "value";

	// name属性
	private static final String NAME_ATTR = "name";

	// property标签
	private static final String PROPERTY_ELEMENT = "property";
	
	//ELEMENT标签
	private static final String DOM4J_ELEMENT = "ELEMENT";

	private static XML2ObjectUtil xmlW2ObjectUtil = new XML2ObjectUtil();
	
	private static Map<String,String> classes;

	static{
		classes = PackageScanUtil.scan();
	}
	
	private XML2ObjectUtil() {
	}


	public static XML2ObjectUtil getInstance() {
		return new XML2ObjectUtil();
	}

	/**
	 * 解析，将XML最终解析成为一个对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object processParse(String xmlPath) throws Exception {
		
//		InputStream in = getInputStreamFromClassLoader(xmlPath.substring(xmlPath.indexOf("xml"));
//		InputStreamReader sr = new InputStreamReader(in,"UTF-8"); 
		Document document = DocumentUtil.readDocument(xmlPath);
		Element root = document.getRootElement();
		String objName = root.getName().substring(0, 1).toUpperCase() + root.getName().substring(1);
		String className = classes.get(objName);
		Class rootClass = Class.forName(className);
		return objectParse(rootClass,root,null);
	}
	
	/**
	 * 从当前类加载器中获取输入流
	 * @param xmlPath
	 * @return
	 */
	private InputStream getInputStreamFromClassLoader(String xmlPath) {
		return this.getClass().getClassLoader().getResourceAsStream(xmlPath);
	}

	/**
	 * 将一个ELEMENT对象解析成为OBJECT对象
	 * 
	 * @param classObject
	 * @param element
	 */
	private Object objectParse(Class classObject, Element root, Object parent)
			throws Exception {
		Constructor<?> constructor = classObject.getDeclaredConstructor(); // 获取类中不带参数的构造方法
		constructor.setAccessible(true); // 将private的构造方法设置成可以访问的
		Object obj = constructor.newInstance();
		
		List<Element> elements = root.elements();
		for (Element element : elements) {
			if (PROPERTY_ELEMENT.equals(element.getName().toLowerCase())) {
				String value = element.getText().trim();

				String property = element.attributeValue(NAME_ATTR);
				Field field = getField(classObject,property);
				field.setAccessible(true);// 压制Java对访问修饰符的检查

				// 处理属性是集合的情况
				if (element.elements(VALUE_ELEMENT).size() > 0) {
					List<Element> valueElements = element.elements(VALUE_ELEMENT);
					List<String> valueList = new ArrayList<String>();
					for (Element valueElement : valueElements) {
						valueList.add(valueElement.getText());
					}
					field.set(obj, valueList);
				} else {
					setValue(obj, field, value);
				}

			} else {
				String property = element.getName();
				String propertyObj = property.substring(0, 1).toUpperCase() + property.substring(1);
				String className = classes.get(propertyObj);
				Class classPorperty = Class.forName(className);
				Object classObj = objectParse(classPorperty, element, obj);
				
				property  = property.substring(0,1).toLowerCase() + property.substring(1);
				Field field = null;
			    field = getField(classObject,property);
				if(field==null){
					property = property+ "s";
					propertyObj = propertyObj + "s";
					field = getField(classObject,property);
				}
				if (!field.getType().equals(List.class)) {
					// 如果是单个元素
					
					field.setAccessible(true);
					field.set(obj, classObj);
				} else {
					// 如果有多个元素，则肯定存储为一个LIST
					Method getMethod = getMethod(classObject,"get" + propertyObj);
					List values = (List) getMethod.invoke(obj);
					if(values == null) {
						values = new ArrayList();
					}
					values.add(classObj);
					Method setMethod = getMethod(classObject,"set" + propertyObj, List.class);
					setMethod.invoke(obj, values);
				}
			}
		}
		return obj;
	}

	/**
	 * 使用反射，将值设置到对应的Field上
	 * 
	 * @param obj
	 * @param field
	 * @param value
	 * @throws Exception
	 */
	private void setValue(Object obj, Field field, Object value)
			throws Exception {
		String type = field.getType().getSimpleName();
		String val = (String) value;
		if (type.equals("String")) {
			field.set(obj, val);
		} else if (type.equals("Boolean") || type.equals("boolean")) {
			field.set(obj, new Boolean(val));
		} else {
			throw new Exception("UnSupported Field Type");
		}
	}
	
	private Field getField(Class classObject,String property) throws SecurityException, NoSuchFieldException{
		Field field =  null;
		try{field = classObject.getDeclaredField(property);}catch(NoSuchFieldException e){}
		while(field==null && classObject.getSuperclass()!=null){
			classObject = classObject.getSuperclass();
			try{field = classObject.getDeclaredField(property);}catch(NoSuchFieldException e){}
		}
		return field;
	}
	
	private Method getMethod(Class classObject,String methodName, Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException{
		Method method = null;
		try{method = classObject.getDeclaredMethod(methodName,parameterTypes);}catch(NoSuchMethodException e){}
		 while(method==null && classObject.getSuperclass()!=null){
			 classObject = classObject.getSuperclass();
			 try{method = classObject.getDeclaredMethod(methodName,parameterTypes);}catch(NoSuchMethodException e){}
		 }
		return method;
	}
	
}
