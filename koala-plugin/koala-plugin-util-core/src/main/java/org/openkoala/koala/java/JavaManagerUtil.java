package org.openkoala.koala.java;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.exception.JavaDoException;
import org.openkoala.koala.exception.KoalaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 过滤搜索类的辅助类
 * 
 * @author lingen
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JavaManagerUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(JavaManagerUtil.class);

	/**
	 * 返回一个JAVA类中的所有Field值
	 * 
	 * @param javasrc
	 * @return
	 * @throws JavaDoException
	 */
	public static List<String> getJavaProperties(String javasrc)
			throws KoalaException {
		List<String> javaProperties = null;
		try {
			File file = new File(javasrc);
			CompilationUnit cu = JavaParser.parse(file);
			if (cu == null) {
				return Collections.EMPTY_LIST;
			}
			javaProperties = getJavaProperties(cu);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new JavaDoException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new JavaDoException(e.getMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return javaProperties;
	}

	/**
	 * 返回一个类中所有的Field的值
	 * 
	 * @param cu
	 * @return
	 */
	public static List<String> getJavaProperties(CompilationUnit cu) {
		List<String> javaProperties = new ArrayList<String>();

		List<FieldDeclaration> fieldDeclarations = getFieldDeclaration(cu);
		logger.info("获取到FieldDeclaration:" + fieldDeclarations);
		for (FieldDeclaration filed : fieldDeclarations) {
			logger.info("添加filed:" + filed);
			javaProperties.addAll(fieldDescription(filed));
		}
		return javaProperties;
	}

	

	public static List<FieldDeclaration> getFieldDeclaration(String javasrc) {
		CompilationUnit cu;
		List<FieldDeclaration> fieldDeclarations = new ArrayList<FieldDeclaration>();
		try {
			File file = new File(javasrc);
			cu = JavaParser.parse(file);
			new PropertiesVisitor(fieldDeclarations).visit(cu, null);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fieldDeclarations;
	}

	public static List<FieldDeclaration> getFieldDeclaration(CompilationUnit cu) {
		List<FieldDeclaration> fieldDeclarations = new ArrayList<FieldDeclaration>();
		new PropertiesVisitor(fieldDeclarations).visit(cu, null);
		return fieldDeclarations;
	}

	/**
	 * 返回一个类中的所有方法
	 * 
	 * @param javasrc
	 * @return
	 * @throws JavaDoException
	 */
	public static List<String> getJavaMethods(String javasrc)
			throws JavaDoException {
		List<String> javaMethods = null;
		try {
			File file = new File(javasrc);
			CompilationUnit cu = JavaParser.parse(file);
			javaMethods = getJavaMethods(cu);
		} catch (ParseException e) {
			throw new JavaDoException(e.getMessage());
		} catch (IOException e) {
			throw new JavaDoException(e.getMessage());
		}
		return javaMethods;
	}

	/**
	 * 返回一个类中的所有方法
	 * 
	 * @param cu
	 * @return
	 */
	public static List<String> getJavaMethods(CompilationUnit cu) {
		List<String> javaMethods = new ArrayList<String>();
		List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
		new MethodsVisitor(methods).visit(cu, null);
		for (MethodDeclaration method : methods) {
			javaMethods.add(methodDescription(method));
		}
		return javaMethods;
	}

	public static List<MethodDeclaration> getMethodDeclaration(
			CompilationUnit cu) {
		List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
		new MethodsVisitor(methods).visit(cu, null);
		return methods;
	}

	public static List<MethodDeclaration> getMethodDeclaration(String javasrc) {
		CompilationUnit cu = null;
		try {
			File file = new File(javasrc);
			cu = JavaParser.parse(file);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getMethodDeclaration(cu);
	}

	/**
	 * 传入一个类，判断这个类是
	 * 
	 * @param javasrac
	 * @return
	 * @throws JavaDoException
	 */
	public static boolean isInterface(String javasrc) throws JavaDoException {
		try {
			File file = new File(javasrc);
			CompilationUnit cu = JavaParser.parse(file);
			ClassOrInterfaceDeclaration classJava = getClassOrInterfaceDeclaration(cu);
			if (classJava.isInterface()) {
				return true;
			}
		} catch (ParseException e) {
			throw new JavaDoException(e.getMessage());
		} catch (IOException e) {
			throw new JavaDoException(e.getMessage());
		}
		return false;
	}

	/**
	 * 传入一个类，获取这个类实现的接口
	 * 
	 * @param javasrc
	 * @return
	 * @throws JavaDoException
	 */
	public static List<String> getInterfaces(String javasrc)
			throws JavaDoException {
		List<String> inters = new ArrayList<String>();
		try {
			File file = new File(javasrc);
			CompilationUnit cu = JavaParser.parse(file);
			ClassOrInterfaceDeclaration javaClass = getClassOrInterfaceDeclaration(cu);
			if (javaClass == null) {
				return Collections.EMPTY_LIST;
			}

			List<ClassOrInterfaceType> interList = javaClass.getImplements();
			if (interList == null) {
				return Collections.EMPTY_LIST;
			}
			List<ImportDeclaration> imports = cu.getImports();
			Map<String, String> importMap = new HashMap<String, String>();
			for (ImportDeclaration ipd : imports) {
				QualifiedNameExpr nameExpr = (QualifiedNameExpr) ipd.getName();
				importMap.put(nameExpr.getName(), nameExpr.getQualifier() + "."
						+ nameExpr.getName());
			}
			for (ClassOrInterfaceType inter : interList) {
				if (importMap.containsKey(inter.getName())) {
					inters.add(importMap.get(inter.getName()));
				} else {
					inters.add(inter.getName());
				}
			}
		} catch (ParseException e) {
			throw new JavaDoException(e.getMessage());
		} catch (IOException e) {
			throw new JavaDoException(e.getMessage());
		}
		return inters;
	}

	/**
	 * 返回一个cu中的主类的ClassOrInterfaceDeclaration
	 * 
	 * @param cu
	 * @return
	 */
	public static ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration(
			CompilationUnit cu) {
		List<TypeDeclaration> types = cu.getTypes();
		for (TypeDeclaration type : types) {
			if (type instanceof ClassOrInterfaceDeclaration) {
				ClassOrInterfaceDeclaration classType = (ClassOrInterfaceDeclaration) type;
				if ((classType.getModifiers() & ModifierSet.PUBLIC) > 0) {
					return classType;
				}
			}
		}
		return null;
	}

	private static class MethodsVisitor extends VoidVisitorAdapter {

		private List<MethodDeclaration> methods;

		public MethodsVisitor(List<MethodDeclaration> methods) {
			this.methods = methods;
		}

		@Override
		public void visit(MethodDeclaration method, Object arg) {
			this.methods.add(method);
		}
	}

	private static class PropertiesVisitor extends VoidVisitorAdapter {

		private List<FieldDeclaration> fieldDeclarations;

		public PropertiesVisitor(List<FieldDeclaration> fieldDeclarations) {
			this.fieldDeclarations = fieldDeclarations;
		}

		@Override
		public void visit(FieldDeclaration field, Object arg) {
			if (fieldDeclarations != null) {
				fieldDeclarations.add(field);
			}
		}
	}

	/**
	 * 查找一个类中，所包含的所有对象，方法中引用到的
	 * 
	 * @param cu
	 * @return
	 */
	public static List<String> getClassVO(CompilationUnit cu) {
		List<String> lists = new ArrayList<String>();
		List<MethodDeclaration> methods = JavaManagerUtil
				.getMethodDeclaration(cu);
		for (MethodDeclaration method : methods) {
			if (method.getType() instanceof ReferenceType) {
				ReferenceType type = (ReferenceType) method.getType();
				if (type.getType() instanceof ClassOrInterfaceType) {
					ClassOrInterfaceType classType = (ClassOrInterfaceType) type
							.getType();
					if (!lists.contains(classType.getName()))
						lists.add(classType.getName());
					if (classType.getTypeArgs() != null) {
						List<Type> args = classType.getTypeArgs();
						for (Type arg : args) {
							if (!lists.contains(arg.toString()))
								lists.add(arg.toString());
						}
					}
				}
			}

			List<Parameter> paramters = method.getParameters();
			if (paramters != null) {
				for (Parameter parameter : paramters) {
					if (!lists.contains(parameter.getType().toString().trim()))
						lists.add(parameter.getType().toString().trim());
				}
			}
		}
		return lists;
	}
	
	private static List<String> fieldDescription(FieldDeclaration filed) {
		List<String> fileds = new ArrayList<String>();
		for (VariableDeclarator vari : filed.getVariables()) {
			fileds.add(vari.getId().getName() + ":" + filed.getType());
		}
		return fileds;
	}
	
	public static String methodDescription(MethodDeclaration method) {
		StringBuffer methodString = new StringBuffer();
		methodString.append(method.getName() + "(");

		if (method.getParameters() != null) {
			List<Parameter> parameters = method.getParameters();
			for (int i=0;i<parameters.size();i++) {
				Parameter para = parameters.get(i);
				if(i!=parameters.size()-1){
					methodString.append(para.getType()+",");
				}else{
					methodString.append(para.getType());
				}
			}
		}
		methodString.append(")");
		methodString.append(":"+method.getType());
		return methodString.toString();
	}
	
	/**
     * 查询是否包含某个指定名称的Annotation
     * @param name
     * @return
     */
    public static boolean containsAnnotation(BodyDeclaration body,String name){
    	if(body.getAnnotations()==null){
    		return false;
    	}
		for(AnnotationExpr annotation:body.getAnnotations()){
			if(annotation.getName().getName().equals(name)){
				return true;
			}
		}
		return false;
	}
}
