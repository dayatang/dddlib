package org.openkoala.koala.deploy.ejb.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;

import org.openkoala.koala.deploy.ejb.exception.InterfaceCopyException;
import org.openkoala.koala.deploy.ejb.pojo.LocalInterface;
import org.openkoala.koala.deploy.ejb.pojo.RemoteInterface;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.java.JavaManagerUtil;
import org.openkoala.koala.java.JavaSaver;

/**
 * 接口生成辅助类，分析一个接口，生成它的远程及本地接口
 * @author lingen
 *
 */
public class InterfaceCopyUtil {

	/**
	 * 传入一个JAVA接口源文件，生成它的本地接口，按默认配置生成
	 * @param javasrc
	 * @throws KoalaException 
	 */
	public static void localInterfaceCreate(String javasrc) throws KoalaException{
		localInterfaceCreate(javasrc,null);
	}
	
	/**
	 * 将application本身打包成EJB，是供WEB内部调用
	 * @param javasrc
	 */
	public static void selfInterface(String javasrc){
	    CompilationUnit cu = null;
        try {
        	File file = new File(javasrc);
            cu = JavaParser.parse(file);
            ClassOrInterfaceDeclaration coi = JavaManagerUtil.getClassOrInterfaceDeclaration(cu);
            if(coi.isInterface()==false)throw new InterfaceCopyException("指定的类不是接口");
            
            
            coi.setName(coi.getName());
            cu.getImports().add(new ImportDeclaration(new NameExpr("javax.ejb.Remote"),false,false));
            AnnotationExpr expr = new NormalAnnotationExpr(new NameExpr("Remote"),null);
			List<AnnotationExpr> annotations = new ArrayList<AnnotationExpr>();
			if(coi.getAnnotations()!=null){
				annotations.addAll(coi.getAnnotations());
			}
			annotations.add(expr);
			coi.setAnnotations(annotations);
            JavaSaver.saveToFile(javasrc, cu);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}
	
	/**
	 * 传入一个JAVA接口源文件，生成它的本地接口，按照指定的配置生成
	 * @param javasrc
	 * @param local
	 * @throws KoalaException 
	 */
	public static void localInterfaceCreate(String javasrc,LocalInterface local) throws KoalaException{
		if(local!=null && local.getMethods()==null)return;
		try {
			File file = new File(javasrc);
			CompilationUnit cu = JavaParser.parse(file);
			ClassOrInterfaceDeclaration coi = JavaManagerUtil.getClassOrInterfaceDeclaration(cu);
			if(coi.isInterface()==false)throw new InterfaceCopyException("指定的类不是接口");
			//修改名称
			coi.setName(coi.getName()+"Local");
			AnnotationExpr expr = new NormalAnnotationExpr(new NameExpr("Local"),null);
			List<AnnotationExpr> annotations = new ArrayList<AnnotationExpr>();
		    if(coi.getAnnotations()!=null){
		    	annotations.addAll(coi.getAnnotations());
		    }
			annotations.add(expr);
			coi.setAnnotations(annotations);
			cu.getImports().add(new ImportDeclaration(new NameExpr("javax.ejb.Local"),false,false));
			
			if(local!=null){
				//过滤方法
				List<String> methods = JavaManagerUtil.getJavaMethods(cu);
				for(String method:methods){
					if(!local.getMethods().contains(method)){
						removeMethod(coi,method);
					}
				}
			}
			
			File srcFile = new File(javasrc);
			String newPath = srcFile.getParent()+"/"+coi.getName()+".java";
			JavaSaver.saveToFile(newPath, cu);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new InterfaceCopyException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new InterfaceCopyException(e.getMessage());
		}
	}

	/**
	 * 传入一个JAVA接口源文件，生成它的远程接口，按照默认的配置生成
	 * @param javasrc
	 * @throws KoalaException 
	 */
	public static void remoteIntrefaceCreate(String javasrc) throws KoalaException{
		remoteIntrefaceCreate(javasrc,null);
	}
	
	/**
	 * 传入一个JAVA接口源文件，生成它的远程接口，按照指定的配置生成
	 * @param javasrc
	 * @param remote
	 * @throws KoalaException 
	 */
	public static void remoteIntrefaceCreate(String javasrc,RemoteInterface remote) throws KoalaException{
		if(remote!=null && remote.getMethods()==null)return;
		try {
			File file = new File(javasrc);
			CompilationUnit cu = JavaParser.parse(file);
			ClassOrInterfaceDeclaration coi = JavaManagerUtil.getClassOrInterfaceDeclaration(cu);
			if(coi.isInterface()==false)throw new InterfaceCopyException("指定的类不是接口");
			//修改名称
			coi.setName(coi.getName()+"Remote");
			cu.getImports().add(new ImportDeclaration(new NameExpr("javax.ejb.Remote"),false,false));
			AnnotationExpr expr = new NormalAnnotationExpr(new NameExpr("Remote"),null);
			List<AnnotationExpr> annotations = new ArrayList<AnnotationExpr>();
		    if(coi.getAnnotations()!=null){
		    	annotations.addAll(coi.getAnnotations());
		    }
			annotations.add(expr);
			coi.setAnnotations(annotations);
			if(remote!=null && remote.getMethods()==null){
				return;
			}
			//过滤方法
			else {
				//过滤方法
				List<String> methods = JavaManagerUtil.getJavaMethods(cu);
				for(String method:methods){
					if(!remote.getMethods().contains(method)){
						removeMethod(coi,method);
					}
				}
			}
			File srcFile = new File(javasrc);
			String newPath = srcFile.getParent()+"/"+coi.getName()+".java";
			JavaSaver.saveToFile(newPath, cu);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new InterfaceCopyException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new InterfaceCopyException(e.getMessage());
		}
	}
	
	 public static void removeMethod(ClassOrInterfaceDeclaration coi,String method) {
	    	BodyDeclaration remove = null;
			for (BodyDeclaration type : coi.getMembers()) {
				if(type instanceof MethodDeclaration){
					MethodDeclaration me = (MethodDeclaration)type;
					if(JavaManagerUtil.methodDescription(me).equals(method)){
						remove = type;
						break;
					}
				}
			}
			if(remove!=null)coi.getMembers().remove(remove);
	}
	 
}
