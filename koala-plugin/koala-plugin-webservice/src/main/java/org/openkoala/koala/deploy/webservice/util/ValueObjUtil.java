package org.openkoala.koala.deploy.webservice.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;

import org.openkoala.koala.deploy.webservice.pojo.ValueObj;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.java.JavaManagerUtil;
import org.openkoala.koala.java.JavaSaver;

/**
 * 对ValueObj进行指定的修改，进行XML化
 * @author lingen
 *
 */
public class ValueObjUtil {

	/**
	 * 传入一个valueObj对象，对其进行WS的XML注解化配置
	 * @param valueObj
	 * @throws KoalaException 
	 */
	public static void updateValueObj(ValueObj valueObj) throws KoalaException{
		String javasrc = valueObj.getQualifiedName();
		CompilationUnit cu;
		try {
			cu = JavaParser.parse(javasrc);
			ClassOrInterfaceDeclaration coi = JavaManagerUtil.getClassOrInterfaceDeclaration(cu);
			
			List<FieldDeclaration> fieldDeclarations = JavaManagerUtil.getFieldDeclaration(cu);
			
			AnnotationExpr xmlRootElement = new NormalAnnotationExpr(new NameExpr("XmlRootElement"),null);
			
			AnnotationExpr xmlElement = new NormalAnnotationExpr(new NameExpr("XmlElement"),null);
			
			coi.getAnnotations().add(xmlRootElement);
			List<String> properties = new ArrayList<String>();
			for(FieldDeclaration filed:fieldDeclarations){
				List<VariableDeclarator> vars = filed.getVariables();
				for(VariableDeclarator var:vars){

					properties.add("get"+var.getId().getName().substring(0,1).toUpperCase()+var.getId().getName().substring(1));
				}
				
			}
			
			List<MethodDeclaration> methods = JavaManagerUtil.getMethodDeclaration(cu);
			for(MethodDeclaration method:methods){
				if(properties.contains(method.getName()))method.getAnnotations().add(xmlElement);
			}
			
			//import javax.xml.bind.annotation.XmlElement;
			//import javax.xml.bind.annotation.XmlRootElement;
			 cu.getImports().add(new ImportDeclaration(new NameExpr("javax.xml.bind.annotation.XmlElement"),false,false));
			 cu.getImports().add(new ImportDeclaration(new NameExpr("javax.xml.bind.annotation.XmlRootElement"),false,false));
			JavaSaver.saveToFile(javasrc, cu);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
