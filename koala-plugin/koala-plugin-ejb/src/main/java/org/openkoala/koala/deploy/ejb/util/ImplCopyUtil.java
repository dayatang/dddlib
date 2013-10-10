package org.openkoala.koala.deploy.ejb.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.type.ClassOrInterfaceType;

import org.openkoala.koala.deploy.ejb.exception.ImplCopyException;
import org.openkoala.koala.deploy.ejb.exception.InterfaceCopyException;
import org.openkoala.koala.deploy.ejb.pojo.ImplObj;
import org.openkoala.koala.java.JavaManagerUtil;
import org.openkoala.koala.java.JavaSaver;
import org.openkoala.koala.pojo.MavenProject;

/**
 * IMPL实现层的修改
 * @author lingen
 *
 */
public class ImplCopyUtil {
	
	/**
	 * 修改一个IMPL实现，主要	包括加入EJB的注解及
	 * @param impl
	 * @throws ImplCopyException
	 */
	public static void updateImpl(String projectPath,ImplObj impl) throws ImplCopyException{
		try{
		  String implPath = projectPath+"/"+impl.getImplModuleName()+"-EJB/src/main/java/"+impl.getName();
		  File file = new File(implPath);
		  CompilationUnit cu = JavaParser.parse(file);
		  ClassOrInterfaceDeclaration coi = JavaManagerUtil.getClassOrInterfaceDeclaration(cu);
		  if(coi.isInterface())throw new InterfaceCopyException("指定的类不是接实现");
		  String interfaceName = impl.getInterfaceName().substring(impl.getInterfaceName().lastIndexOf("/")+1,impl.getInterfaceName().lastIndexOf(".java"));
		  MemberValuePair statelessMember = new MemberValuePair("name",new StringLiteralExpr(interfaceName));
		  List<MemberValuePair> members = new ArrayList<MemberValuePair>();
		  members.add(statelessMember);
		  
		  AnnotationExpr statlessAnnotationExpr = new NormalAnnotationExpr(new NameExpr("Stateless"),members);
		  
		  MemberValuePair interceptorsMember = new MemberValuePair("value",new ClassExpr(new ClassOrInterfaceType("org.openkoala.koala.util.SpringEJBIntercepter")));
		  List<MemberValuePair> interceptorsMembers = new ArrayList<MemberValuePair>();
		  interceptorsMembers.add(interceptorsMember);
		  AnnotationExpr interceptorsAnnotationExpr = new NormalAnnotationExpr(new NameExpr("Interceptors"),interceptorsMembers);
		  
		  if(JavaManagerUtil.containsAnnotation(coi,"Interceptors")==false)coi.getAnnotations().add(interceptorsAnnotationExpr);
		  if(JavaManagerUtil.containsAnnotation(coi,"Stateless")==false)coi.getAnnotations().add(statlessAnnotationExpr);
		  
		  cu.getImports().add(new ImportDeclaration(new NameExpr("javax.ejb.Stateless"),false,false));
		  cu.getImports().add(new ImportDeclaration(new NameExpr("javax.interceptor.Interceptors"),false,false));
		  cu.getImports().add(new ImportDeclaration(new NameExpr("org.openkoala.koala.util.SpringEJBIntercepter"),false,false));
		  
		  String interfaceQualifiedName = impl.getInterfaceName().substring(0,impl.getInterfaceName().lastIndexOf(".java"));
		  
		  
		  //加入local接口
		  if(impl.getLocalInterface()!=null && impl.getLocalInterface().getMethods()!=null){
		  String localClassName = (interfaceQualifiedName+"Local").replaceAll("/", "\\.");
		  String localSimpleName = localClassName.substring(localClassName.lastIndexOf(".")+1);
		  cu.getImports().add(new ImportDeclaration(new NameExpr(localClassName),false,false));
		  coi.getImplements().add(new ClassOrInterfaceType(localSimpleName));
		  }
		  
		  //加入remote接口
		  if(impl.getRemoteInterface()!=null && impl.getRemoteInterface().getMethods()!=null){
		  String remoteClassName = (interfaceQualifiedName+"Remote").replaceAll("/", "\\.");
		  String remoteSimpleName = remoteClassName.substring(remoteClassName.lastIndexOf(".")+1);
		  cu.getImports().add(new ImportDeclaration(new NameExpr(remoteClassName),false,false));
		  coi.getImplements().add(new ClassOrInterfaceType(remoteSimpleName));
		  }
		  
		  JavaSaver.saveToFile(implPath, cu);
		}catch(Exception e){
			throw new ImplCopyException(e.getMessage());
		}
	}
}
