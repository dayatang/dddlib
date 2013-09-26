package org.openkoala.koala.codechecker.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 路径扫描辅助类，扫描一个目录下的所有JAVA文件，以进行检查
 * @author lingen
 *
 */
public class JavaFileScanUtil {

	public static List<File> scanDir(String dir){
		List<File> javaFiles = new ArrayList<File>();
		File fileDir = new File(dir);
		if(fileDir.isFile())javaFiles.add(fileDir);
		else{
			File[] files = fileDir.listFiles();
			for(File file:files){
				if(file.isFile() && file.getName().endsWith(".java")){
					javaFiles.add(file);
				}else if(file.isDirectory()){
					javaFiles.addAll(scanDir(file.getAbsolutePath()));
				}
			}
		}
		return javaFiles;
	}
	
}
