package com.dayatang.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class ScriptService {
	
	private String scriptType = ScriptType.GROOVY;

	public ScriptService() {
	}

	public ScriptService(String scriptType) {
		this.scriptType = scriptType;
	}
	
	public ScriptEngine getEngine() {
		return getScriptEngineManager().getEngineByName(scriptType);
	}

	public ScriptEngineManager getScriptEngineManager() {
		return new ScriptEngineManager();
	}
	
	public ScriptEngineFactory getScriptEngineFactory() {
		return getEngine().getFactory();
	}
	
}
