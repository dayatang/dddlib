package org.openkoala.koala.java;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openkoala.koala.exception.KoalaException;

import japa.parser.ast.CompilationUnit;

/**
 * 将修改后的类保存起来
 * @author lingen
 *
 */
public class JavaSaver {

	/**
	 * 将修改后的JAVA类cu保存到指定的文件中去
	 * @param javaPath
	 * @param cu
	 * @throws KoalaException 
	 */
	public static void saveToFile(String path,CompilationUnit cu) throws KoalaException{
		try {
			FileUtils.write(new File(path), cu.toString(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			throw new KoalaException("修改后的类写入失败");
		}
	}
}
