package org.openkoala.koala.deploy.webservice.util;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openkoala.koala.deploy.webservice.pojo.MediaType;
import org.openkoala.koala.deploy.webservice.pojo.ParamType;
import org.openkoala.koala.deploy.webservice.pojo.WebServiceType;

public class AutoRestRuleUtils {

	private static String[] useGetStrings = new String[]{"get", "query", "find"};
	private static String[] usePostStrings = new String[]{"save", "add", "append"};
	private static String[] usePutStrings = new String[]{"update", "change", "modify", "alter"};
	private static String[] useDeleteStrings = new String[]{"delete", "remove"};
	
	private static Set<String> wrapTypes = new HashSet<String>();
	static {
		wrapTypes.add("String");
		wrapTypes.add("Integer");
		wrapTypes.add("Long");
		wrapTypes.add("Character");
		wrapTypes.add("Float");
		wrapTypes.add("Double");
		wrapTypes.add("Byte");
		wrapTypes.add("Short");
		wrapTypes.add("Boolean");
	}
	
	private static Set<String> collectionTypes = new HashSet<String>();
	static {
		collectionTypes.add("List");
		collectionTypes.add("Set");
		collectionTypes.add("SortedSet");
		collectionTypes.add("NavigableSet");
		collectionTypes.add("Deque");
		collectionTypes.add("Queue");
		collectionTypes.add("BlockingQueue");
		collectionTypes.add("BlockingDeque");
		collectionTypes.add("BeanContextServices");
		collectionTypes.add("BeanContext");
		
		collectionTypes.add("AbstractCollection");
		collectionTypes.add("AbstractList");
		collectionTypes.add("AbstractQueue");
		collectionTypes.add("AbstractSequentialList");
		collectionTypes.add("AbstractSet");
		collectionTypes.add("ArrayBlockingQueue");
		collectionTypes.add("ArrayDeque");
		collectionTypes.add("ArrayList");
		collectionTypes.add("AttributeList");
		collectionTypes.add("BeanContextServicesSupport");
		collectionTypes.add("BeanContextSupport");
		collectionTypes.add("ConcurrentLinkedQueue");
		collectionTypes.add("ConcurrentSkipListSet");
		collectionTypes.add("CopyOnWriteArrayList");
		collectionTypes.add("CopyOnWriteArraySet");
		collectionTypes.add("DelayQueue");
		collectionTypes.add("EnumSet");
		collectionTypes.add("HashSet");
		collectionTypes.add("JobStateReasons");
		collectionTypes.add("LinkedBlockingDeque");
		collectionTypes.add("LinkedBlockingQueue");
		collectionTypes.add("LinkedHashSet");
		collectionTypes.add("LinkedList");
		collectionTypes.add("PriorityBlockingQueue");
		collectionTypes.add("PriorityQueue");
		collectionTypes.add("RoleList");
		collectionTypes.add("RoleUnresolvedList");
		collectionTypes.add("Stack");
		collectionTypes.add("SynchronousQueue");
		collectionTypes.add("TreeSet");
		collectionTypes.add("Vector");
	}
	
	/**
	 * 根据发布的方法名自动选择使用的HTTP方法
	 * @param methodName
	 * @return
	 */
	public static WebServiceType getHttpMethodByMethodName(String methodName) {
		for (String useGetString : useGetStrings) {
			if (methodName.startsWith(useGetString)) {
				return WebServiceType.GET;
			}
		}
		for (String usePostString : usePostStrings) {
			if (methodName.startsWith(usePostString)) {
				return WebServiceType.POST;
			}
		}
		for (String usePutString : usePutStrings) {
			if (methodName.startsWith(usePutString)) {
				return WebServiceType.PUT;
			}
		}
		for (String useDeleteString : useDeleteStrings) {
			if (methodName.startsWith(useDeleteString)) {
				return WebServiceType.DELETE;
			}
		}
		return WebServiceType.POST;
	}
	
	/**
	 * 根据HTTP方法自动选择默认使用的参数传输方式
	 * @param httpMehtod
	 * @return
	 */
	public static ParamType getDefaultParamType(WebServiceType httpMehtod, MethodDeclaration method) {
		List<Parameter> parameters = method.getParameters();
		if (parameters == null) {
			return null;
		}
		
		if (hasCollectionType(parameters)) {
			return ParamType.QUERY_PARAM;
		}
		if (httpMehtod != WebServiceType.DELETE && hasReferenceType(parameters)) {
			return ParamType.FORM_PARAM;
		}
		if (httpMehtod == WebServiceType.DELETE && hasReferenceType(parameters)) {
			return ParamType.QUERY_PARAM;
		}
		return ParamType.PATH_PARAM;
	}

	/**
	 * 根据所使用的http方法和参数类型，获得可选择的参数传输方式集
	 * @param httpMethod
	 * @param method
	 * @return
	 */
	public static List<ParamType> getParamTypes(WebServiceType httpMethod, MethodDeclaration method) {
		List<Parameter> parameters = method.getParameters();
		List<ParamType> results = new ArrayList<ParamType>();
		if (parameters == null) {
			return results;
		}
		
		if (hasCollectionType(parameters)) {
			results.add(ParamType.QUERY_PARAM);
			results.add(ParamType.MATRIX_PARAM);
			return results;
		}
		if (hasReferenceType(parameters)) {
			results.add(ParamType.QUERY_PARAM);
			results.add(ParamType.MATRIX_PARAM);
			if (httpMethod != WebServiceType.DELETE) {
				results.add(ParamType.FORM_PARAM);
			}
			return results;
		}
		
		for (ParamType paramType : ParamType.values()) {
			results.add(paramType);
		}
		if (httpMethod == WebServiceType.DELETE) {
			results.remove(ParamType.FORM_PARAM);
		}
		
		return results;
	}
	
	private static boolean hasReferenceType(List<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			if (isReferenceType(parameter)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isReferenceType(Parameter parameter) {
		Type type = parameter.getType();
		if (type instanceof ReferenceType) {
			String typeString = type.toString();
			if (!wrapTypes.contains(typeString) && !typeString.contains("[]")) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean hasCollectionType(List<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			Type type = parameter.getType();
			if (type instanceof ReferenceType) {
				String typeString = type.toString();
				if (typeString.contains("[]")) {
					return true;
				}
				if (collectionTypes.contains(typeString)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 根据参数的数据传输格式自动生成所使用的方法路径
	 * @param paramType
	 * @param method
	 * @return
	 */
	public static String generateUriPathByParameType(ParamType paramType, MethodDeclaration method) {
		String result = method.getName() + "/";
		List<Parameter> parameters = method.getParameters();
		if (parameters != null) {
			if (paramType == ParamType.PATH_PARAM) {
				for (Parameter parameter : parameters) {
					result = result + "{" + parameter.getId().getName() + "}/";
				}
			}
		}
		
		return result.substring(0, result.length() - 1);
	}

	/**
	 * 根据参数传输格式获取可以使用的数据传输格式集
	 * @param paramType
	 * @return
	 */
	public static List<MediaType> getConsumesMediaTypesByParamType(ParamType paramType) {
		List<MediaType> results = new ArrayList<MediaType>();
		if (paramType == null) {
			return results;
		}
		
		if (paramType == ParamType.PATH_PARAM) {
			results.add(MediaType.APPLICATION_XML);
			results.add(MediaType.APPLICATION_JSON);
			results.add(MediaType.TEXT_XML);
			results.add(MediaType.TEXT_HTML);
			return results;
		}
		if (paramType == ParamType.FORM_PARAM) {
			results.add(MediaType.APPLICATION_FORM_URLENCODED);
			results.add(MediaType.MULTIPART_FORM_DATA);
			return results;
		}
		if (paramType == ParamType.QUERY_PARAM) {
			results.add(MediaType.APPLICATION_XML);
			results.add(MediaType.APPLICATION_JSON);
			results.add(MediaType.TEXT_XML);
			results.add(MediaType.TEXT_HTML);
			return results;
		}
		if (paramType == ParamType.COOKIE_PARAM) {
			results.add(MediaType.APPLICATION_XML);
			results.add(MediaType.APPLICATION_JSON);
			results.add(MediaType.TEXT_XML);
			results.add(MediaType.TEXT_HTML);
			return results;
		}
		if (paramType == ParamType.HEADER_PARAM) {
			results.add(MediaType.APPLICATION_XML);
			results.add(MediaType.APPLICATION_JSON);
			results.add(MediaType.TEXT_XML);
			results.add(MediaType.TEXT_HTML);
			return results;
		}
		if (paramType == ParamType.MATRIX_PARAM) {
			results.add(MediaType.APPLICATION_XML);
			results.add(MediaType.APPLICATION_JSON);
			results.add(MediaType.TEXT_XML);
			results.add(MediaType.TEXT_HTML);
			return results;
		}
		
		return null;
	}
	
	/**
	 * 获取Produces可以使用的MediaType集
	 * @return
	 */
	public static List<MediaType> getProducesMediaTypes() {
		List<MediaType> results = new ArrayList<MediaType>();
		results.add(MediaType.APPLICATION_XML);
		results.add(MediaType.APPLICATION_JSON);
		results.add(MediaType.TEXT_XML);
		results.add(MediaType.TEXT_HTML);
		return results;
	}
	
}
