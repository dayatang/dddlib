package org.openkoala.koala.maven;

import java.io.File;
import java.io.FileFilter;

/**
 * MAVEN文件的过滤器
 * @author lingen
 *
 */
public class MavenFileFilter implements FileFilter {

	private static MavenFileFilter mavenFileFilter = new MavenFileFilter();
	
	private MavenFileFilter(){
		
	}
	public static MavenFileFilter newInstance(){
		return mavenFileFilter;
	}
	
	public boolean accept(File pathname) {
		String fileName = pathname.getName();
		if(fileName.endsWith(".svn"))
		return false;
		if(fileName.equals("bin") || fileName.equals("target"))
		return false;
		if(fileName.equals(".project"))return false;
		if(fileName.equals(".settings"))return false;
		return true;
	}

}
