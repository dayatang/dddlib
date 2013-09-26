package org.openkoala.koala.codechecker.checkstyle;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.codechecker.vo.CodeCheckerVO;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

public class KoalaCodeCheckerAuditListener implements AuditListener{

	private List<CodeCheckerVO> codeCheckerVos;
	
	
	@Override
	public void addError(AuditEvent evt) {
		CodeCheckerVO vo = new CodeCheckerVO(evt.getFileName(),evt.getLine(),evt.getColumn(),evt.getMessage());
		codeCheckerVos.add(vo);
	}

	@Override
	public void addException(AuditEvent arg0, Throwable arg1) {
		
	}

	@Override
	public void auditFinished(AuditEvent arg0) {
		System.out.println(codeCheckerVos);
	}

	@Override
	public void auditStarted(AuditEvent arg0) {
		codeCheckerVos = new ArrayList<CodeCheckerVO>();
	}

	@Override
	public void fileFinished(AuditEvent arg0) {
		
	}

	@Override
	public void fileStarted(AuditEvent arg0) {
		
	}

	public List<CodeCheckerVO> getCodeCheckerVos() {
		return codeCheckerVos;
	}

}
