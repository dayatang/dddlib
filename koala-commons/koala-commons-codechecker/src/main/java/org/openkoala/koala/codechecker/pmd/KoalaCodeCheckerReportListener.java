package org.openkoala.koala.codechecker.pmd;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openkoala.koala.codechecker.vo.CodeCheckerVO;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.ReportListener;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.renderers.AbstractIncrementingRenderer;
import net.sourceforge.pmd.stat.Metric;

public class KoalaCodeCheckerReportListener extends
		AbstractIncrementingRenderer {

	private List<CodeCheckerVO> codeCheckerVos;

	public KoalaCodeCheckerReportListener() {
		super(NAME, "Text format.");
		codeCheckerVos = new ArrayList<CodeCheckerVO>();
	}

	public static final String NAME = "KoalaRender";

	public String defaultFileExtension() {
		return "KoalaRender";
	}


	@Override
	public void start() throws IOException {
		codeCheckerVos = new ArrayList<CodeCheckerVO>();
	}

	@Override
	public void renderFileViolations(Iterator<RuleViolation> violations)
			throws IOException {
		while (violations.hasNext()) {
			RuleViolation rv = violations.next();
			codeCheckerVos.add(new CodeCheckerVO(rv.getFilename(),rv.getBeginLine(),rv.getBeginColumn(),rv.getDescription()));
		}
	}

	@Override
	public void end() throws IOException {
	}


	public List<CodeCheckerVO> getCodeCheckerVos() {
		return codeCheckerVos;
	}
	
	

}
