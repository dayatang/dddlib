package org.openkoala.koala.codechecker.checkstyle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.codechecker.CodeCheckerProcess;
import org.openkoala.koala.codechecker.util.JavaFileScanUtil;
import org.openkoala.koala.codechecker.vo.CodeCheckerVO;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class CheckStyleCodeCheckerProcess implements CodeCheckerProcess {

	@Override
	public List<CodeCheckerVO> process(String filePath) {
		KoalaCodeCheckerAuditListener listener = new KoalaCodeCheckerAuditListener();
		Checker checker;
		try {
			checker = new Checker();

			checker.setModuleClassLoader(Thread.currentThread()
					.getContextClassLoader());
			checker.configure(ConfigurationLoader.loadConfiguration(
					"/xml/sun_checks.xml", null));
			List<File> files = new ArrayList<File>();
			checker.addListener(listener);
			files.addAll(JavaFileScanUtil.scanDir(filePath));
			checker.process(files);
		} catch (CheckstyleException e) {
			e.printStackTrace();
		}
		return listener.getCodeCheckerVos();
	}
	
	public static void main(String args[]){
		String path = "F:/Project/foss-project/src/plugin/KoalaCodeChecker/src/org/openkoala/koala/codechecker/checkstyle/CheckStyleCodeCheckerProcess.java";
		CheckStyleCodeCheckerProcess check = new CheckStyleCodeCheckerProcess();
		check.process(path);
		try{
			
		}finally{
			
		}
	}

}
