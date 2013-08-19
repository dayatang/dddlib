package org.openkoala.koala.codechecker;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.codechecker.checkstyle.CheckStyleCodeCheckerProcess;
import org.openkoala.koala.codechecker.pmd.PMDCodeCheckerProcess;
import org.openkoala.koala.codechecker.vo.CodeCheckerVO;

/**
 * JAVA类的代码检查
 * 
 * @author lingen
 * 
 */
public class JavaCodeChecker {

	/**
	 * 传入一个目录，扫描这个目录下的所有JAVA文件并进行代码检查
	 * @param filePath
	 * @return
	 */
	public static List<CodeCheckerVO> checkCode(String filePath) {
		List<CodeCheckerVO> results = new ArrayList<CodeCheckerVO>();
		CodeCheckerProcess checkstyle = new CheckStyleCodeCheckerProcess();
		results.addAll(checkstyle.process(filePath));
		CodeCheckerProcess pmd = new PMDCodeCheckerProcess();
		results.addAll(pmd.process(filePath));
		return results;
	}
}
