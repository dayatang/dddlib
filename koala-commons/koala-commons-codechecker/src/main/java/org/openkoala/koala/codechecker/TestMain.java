package org.openkoala.koala.codechecker;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.codechecker.checkstyle.CheckStyleCodeCheckerProcess;
import org.openkoala.koala.codechecker.pmd.PMDCodeCheckerProcess;
import org.openkoala.koala.codechecker.vo.CodeCheckerVO;

public class TestMain {
	
	public static void main(String args[]){
		String path = "F:\\Project\\foss-project\\src\\JBPM\\jbpm-ws-client";
		List<CodeCheckerVO> results = new ArrayList<CodeCheckerVO>();
		
		CodeCheckerProcess checkstyle = new CheckStyleCodeCheckerProcess();
//		results.addAll(checkstyle.process(path));
		
		
		CodeCheckerProcess pmd = new PMDCodeCheckerProcess();
		results.addAll(pmd.process(path));
		for(CodeCheckerVO result:results){
			System.out.println(result.getFilename()+":"+result.getLine()+":"+result.getMessage());
		}

	}
}
