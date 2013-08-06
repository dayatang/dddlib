package org.openkoala.koala.createvo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.parse.XML2ObjectUtil;

/**
 * 模块定义工具类
 * 
 * @author lingen
 * 
 */
@SuppressWarnings("serial")
public class TypeDef implements Serializable {

	public List<Function> function;

	private TypeDef() {
	}

	public static TypeDef getInstance() {
		XML2ObjectUtil util = XML2ObjectUtil.getInstance();
		try {
			TypeDef def = (TypeDef) util.processParse("xml/typeDef.xml");
			return def;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Function> getFunction() {
		return function;
	}

	public void setFunction(List<Function> function) {
		this.function = function;
	}

	/**
	 * 传入层的名称，获取这个层可以使用到的功能列表
	 * 
	 * @param type
	 * @return
	 */
	public Map<String, String> getFunctions(String type) {
		Map<String, String> functionMap = new HashMap<String, String>();
		for (Function fun : function) {
			if (fun.getModules().contains(type)) {
				// key->功能名称，value->功能描述
				functionMap.put(fun.getName(), fun.getDescription());
			}
		}
		return functionMap;
	}

	public static void main(String args[]) {
		TypeDef def = TypeDef.getInstance();
		System.out.println(def.getFunctions("infra"));
	}
}
