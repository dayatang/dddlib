package org.openkoala.koala.parseImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openkoala.koala.annotation.ParseAnnotation;
import org.openkoala.koala.annotation.parse.Parse;
import org.openkoala.koala.annotation.parse.ParseListFunctionCreate;
import org.openkoala.koala.annotation.parse.ParseMappedFunctionCreate;
import org.openkoala.koala.annotation.parse.ParseObjectFunctionCreate;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.Project;
import org.openkoala.koala.widget.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 创建多模块项目的实现类
 * @author lingen.liu
 *
 */
public class ProjectCreateParse {

	private static final Logger logger = LoggerFactory.getLogger(ProjectCreateParse.class);
	private Map paramMap = new HashMap();
	
	private static List<String> baseType = new ArrayList<String>();
	
	static{
		baseType.add("boolean");
		baseType.add("byte");
		baseType.add("char");
		baseType.add("int");
		baseType.add("short");
		baseType.add("long");
		baseType.add("double");
		baseType.add("float");
	}

	/**
	 * 分析生成一个项目
	 * @param project
	 * @throws Exception
	 */
	public void parse(Project project) throws Exception {
		parseClass(project,null);
	}

	private void parseClass(Object obj,Object parent) throws Exception {
		if(obj==null)return;
		paramMap.put(obj, parent);
		// 遍历
		Class proClass = obj.getClass();
		// 遍历CLASS级别的
		Annotation[] annotations = proClass.getAnnotations();
		dealWithAnnotations(proClass.getAnnotations(), proClass.getSimpleName(),obj,null);
		Field[] fields = proClass.getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);
			String type = field.getType().getSimpleName().toLowerCase();
			Object filedVal = field.get(obj);
			if (type.equals("string")
					|| baseType.contains(type)) {
				String name = field.getName();
				dealWithAnnotations(field.getAnnotations(), name,obj,filedVal);
			} else if (field.getType().equals(List.class)) {
				String name = field.getName();
				dealWithAnnotations(field.getAnnotations(), name,obj,filedVal);
				
				List fieldObjList = (List)field.get(obj);
				if(fieldObjList==null)fieldObjList = new ArrayList();
				for(Object filedObj:fieldObjList){
					parseClass(filedObj,obj);
				}
			}else{
				Object filedObj = field.get(obj);
				parseClass(filedObj,obj);
			}
		}
	}

	private void dealWithAnnotations(Annotation[] annotations, String name,Object obj,Object filedVal)
			throws Exception {
		for (Annotation annotation : annotations) {
			ParseAnnotation parseAnnotation = (ParseAnnotation) annotation.annotationType().getAnnotation(ParseAnnotation.class);
			if (parseAnnotation != null) {
				
				Class parseType = parseAnnotation.type();
				Parse parse = (Parse)parseType.newInstance();
				parse.initParms(getParams(obj), name, filedVal);
				parse.process();
			}
		}
	}
	
	private List getParams(Object obj){
		List params = new ArrayList();
		params.add(obj);
		Object parent = paramMap.get(obj);
		while(parent!=null){
			params.add(parent);
			parent = paramMap.get(parent);
		}
		
		return params;
	}

	/**
	 * 分析生成一个新的子项目
	 * @param project
	 * @param module
	 * @throws Exception
	 */
	public void parse(Project project,Module module) throws Exception {
		parseClass(module,project);
		project.getModule().add(module);
	}
	
	/**
	 * 分析并更新一个WEB项目的securtiy组件
	 * @param project
	 * @param module
	 * @param security
	 * @throws KoalaException 
	 */
	public void parse(Project project,Module module,Security security) throws KoalaException{
		paramMap.put(project, null);
		paramMap.put(module, project);
		try {
			parseClass(security,module);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KoalaException(e.getMessage());
		}
	}
}
