package com.dayatang.script;


import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScriptServiceTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void displayEngines() {
		ScriptEngineManager manager = new ScriptEngineManager();
		for (ScriptEngineFactory factory : manager.getEngineFactories()) {
			System.out.println("Engine name: " + factory.getEngineName());
			System.out.println("Engine version: " + factory.getEngineVersion());
			System.out.println("Language name: " + factory.getLanguageName());
			System.out.println("Language version: " + factory.getLanguageVersion());
			
			System.out.println("\n\rLanguage Extensions: ");
			for (String extension : factory.getExtensions()) {
				System.out.println(extension);
			}
			
			System.out.println("\n\rMIME types: ");
			for (String mimeType : factory.getMimeTypes()) {
				System.out.println(mimeType);
			}
			
			System.out.println("\n\rFactory names: ");
			for (String name : factory.getNames()) {
				System.out.println(name);
			}
			System.out.println("");
			System.out.println("");
		}
	}
	
	@Test
	public void testConstructorWithoutArgs() {
		ScriptService service = new ScriptService();
		String engineName = service.getEngine().getFactory().getEngineName();
		assertEquals("Groovy Scripting Engine", engineName);
	}
	
	@Test
	public void testConstructorWithArgs() {
		ScriptService service = new ScriptService(ScriptType.GROOVY);
		String engineName = service.getEngine().getFactory().getEngineName();
		assertEquals("Groovy Scripting Engine", engineName);

		service = new ScriptService(ScriptType.JS);
		engineName = service.getEngine().getFactory().getEngineName();
		assertEquals("Mozilla Rhino", engineName);
	}

	
	@Test
	public void testEvalStringGroovy() throws ScriptException {
		ScriptEngine engine = new ScriptService().getEngine();
		String script = "System.out.println(\"Hello!!!!\");";
		engine.eval(script);
	}
	
	@Test
	public void testEvalStringJavascript() throws ScriptException {
		ScriptEngine engine = new ScriptService(ScriptType.JS).getEngine();
		//String script = "print(\"Hello!!!!\");";
		String script = "eval(\"3 + 2\");";
		System.out.println(engine.eval(script));
	}
	
	
	@Test
	public void testEvalReader() throws ScriptException, FileNotFoundException, MalformedURLException, URISyntaxException {
		ScriptEngine engine = new ScriptService().getEngine();
		InputStream in = getClass().getResourceAsStream("/test.groovy");
		engine.eval(new InputStreamReader(in));
	}
	
	
}
