package org.openkoala.koala.deploy.webservice.util;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openkoala.koala.deploy.webservice.pojo.InterfaceObj;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.java.JavaManagerUtil;
import org.openkoala.koala.java.JavaSaver;

public class ImplObjUtil {

	public static void updateImplObj(InterfaceObj interfaceObj)
			throws KoalaException {
		List<String> selectedMethods = interfaceObj.getSelectedMethods();

		if (selectedMethods == null || selectedMethods.isEmpty()) {
			return;
		}

		String javasrc = interfaceObj.getQualifiedImplName();
		CompilationUnit compilationUnit;
		try {
			File file = new File(javasrc);
			compilationUnit = JavaParser.parse(file);
			ClassOrInterfaceDeclaration coi = JavaManagerUtil
					.getClassOrInterfaceDeclaration(compilationUnit);

			compilationUnit.getImports().add(new ImportDeclaration(new NameExpr("javax.jws.WebMethod"), false, false));
			compilationUnit.getImports().add(new ImportDeclaration(new NameExpr("javax.jws.WebService"), false, false));

			AnnotationExpr webServiceAnnotation = new SingleMemberAnnotationExpr(new NameExpr("WebService"), 
					new NameExpr("endpointInterface = \"" + getInterfaceClassFullNameBy(interfaceObj.getName()) + "\""));

			AnnotationExpr webMethodAnnotation = new SingleMemberAnnotationExpr(new NameExpr("WebMethod"),
					new NameExpr("exclude = true"));

			if (JavaManagerUtil.containsAnnotation(coi, "WebService") == false) {
				coi.getAnnotations().add(webServiceAnnotation);
			}

			List<MethodDeclaration> methods = JavaManagerUtil
					.getMethodDeclaration(compilationUnit);
			for (MethodDeclaration method : methods) {
				if (selectedMethods.contains(JavaManagerUtil.methodDescription(method))) {
					continue;
				}

				if (JavaManagerUtil.containsAnnotation(coi, "WebMethod") == false) {
					method.getAnnotations().add(webMethodAnnotation);
				}
			}

			JavaSaver.saveToFile(javasrc, compilationUnit);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getInterfaceClassFullNameBy(String pathName) {
		String result = "";
		result = pathName.replace("/", ".");
		return result.replace(".java", "");
	}
	
}
