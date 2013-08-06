package org.openkoala.koala.codechecker.pmd;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.RulesetsFactoryUtils;
import net.sourceforge.pmd.benchmark.Benchmark;
import net.sourceforge.pmd.benchmark.Benchmarker;
import net.sourceforge.pmd.cli.PMDCommandLineInterface;
import net.sourceforge.pmd.cli.PMDParameters;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageFilenameFilter;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.LanguageVersionDiscoverer;
import net.sourceforge.pmd.renderers.Renderer;
import net.sourceforge.pmd.util.FileUtil;
import net.sourceforge.pmd.util.IOUtil;
import net.sourceforge.pmd.util.datasource.DataSource;

import org.openkoala.koala.codechecker.CodeCheckerProcess;
import org.openkoala.koala.codechecker.vo.CodeCheckerVO;

public class PMDCodeCheckerProcess implements CodeCheckerProcess {

	private KoalaCodeCheckerReportListener renderer;


	public PMDCodeCheckerProcess(){
		renderer = new KoalaCodeCheckerReportListener();
	}
	
	@Override
	public List<CodeCheckerVO> process(String filePath) {
		
	       
			try {
				 String[] args = new String[]{"-d",filePath,"-f","text","-R","xml/basic.xml","-version","1.5","-language","java"};

			    	PMDParameters params = PMDCommandLineInterface.extractParameters(new PMDParameters(), args, "pmd");
				    PMDConfiguration configuration = PMDParameters.transformParametersIntoConfiguration(params);
				    
				    RuleSetFactory ruleSetFactory = RulesetsFactoryUtils
							.getRulesetFactory(configuration);
				   
				    long startLoadRules = System.nanoTime();
				    RuleSets ruleSets = null;
				    try{
					ruleSets = RulesetsFactoryUtils.getRuleSets(
							configuration.getRuleSets(), ruleSetFactory, startLoadRules);
				    }catch(Exception e){
				    	return Collections.EMPTY_LIST;
				    }

					Set<Language> languages = getApplicableLanguages(configuration, ruleSets);
					List<DataSource> files = getApplicableFiles(configuration, languages);
			
					List<Renderer> renderers = new LinkedList<Renderer>();
					renderers.add(renderer);

					renderer.setWriter(IOUtil.createWriter(configuration.getReportFile()));
				renderer.start();
				RuleContext ctx = new RuleContext();
				PMD.processFiles(configuration, ruleSetFactory, files, ctx, renderers);
				renderer.end();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return renderer.getCodeCheckerVos();
	}
	
	public static List<DataSource> getApplicableFiles(
			PMDConfiguration configuration, Set<Language> languages) {
		long startFiles = System.nanoTime();
		LanguageFilenameFilter fileSelector = new LanguageFilenameFilter(languages);
		List<DataSource> files = FileUtil.collectFiles(
				configuration.getInputPaths(), fileSelector);
		long endFiles = System.nanoTime();
		Benchmarker.mark(Benchmark.CollectFiles, endFiles - startFiles, 0);
		return files;
	}
	
	private static Set<Language> getApplicableLanguages(PMDConfiguration configuration, RuleSets ruleSets) {
		Set<Language> languages = new HashSet<Language>();
		LanguageVersionDiscoverer discoverer = configuration.getLanguageVersionDiscoverer();
		
		for (Rule rule : ruleSets.getAllRules()) {
			Language language = rule.getLanguage();
			if (languages.contains(language))
				continue;
			LanguageVersion version = discoverer.getDefaultLanguageVersion(language);
			if (RuleSet.applies(rule, version)) {
				languages.add(language);
			}
		}
		return languages;
	}

}
