package org.openkoala.koala.maven;

import org.apache.maven.cli.MavenCli;
import org.openkoala.koala.exception.KoalaException;


public class MavenExcuter {
	
	/**
	 * 对指定目录的Maven项目进行clean install，检查项目是否能正常编译
	 * @param path
	 * @throws KoalaException 
	 */
	public static void runMaven(String path) throws KoalaException{
		MavenCli cli = new MavenCli();
		//-DskipTests:编译测试类但不执行 -Dmaven.test.skip=true 不编译也不执行测试类
		int result = cli.doMain(new String[]{"install","-DskipTests=true"},
		       path,
		        System.out, System.err);
		if(result!=0)throw new KoalaException("项目不能正常编译，请检查");
	}
	
	public static void main(String args[]) throws KoalaException{
		MavenExcuter.runMaven("E:/workspaces/runtime-EclipseApplication/demo/target/demo-WS");
	}
}
