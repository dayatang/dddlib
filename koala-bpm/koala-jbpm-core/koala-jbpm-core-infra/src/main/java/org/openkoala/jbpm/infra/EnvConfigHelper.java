package org.openkoala.jbpm.infra;

import java.io.InputStream;
import java.util.Properties;

public final class EnvConfigHelper extends Properties {

	private static final long serialVersionUID = -3102828151390080479L;

	private static EnvConfigHelper instance;

	public static EnvConfigHelper getInstance() {
		if (instance != null) {
			return instance;
		} else {
			makeInstance();
			return instance;
		}
	}

	public static synchronized void makeInstance() {
		if (instance == null) {
			instance = new EnvConfigHelper();
		}
	}

	private EnvConfigHelper() {
		InputStream is = getClass().getResourceAsStream("/META-INF/jbpmCore-config.properties");
		try {
			load(is);
		} catch (Exception ce) {
			System.out.println("读取配置文件失败！，请确定jbpmCore-config.properties文件存在");
		}
	}

}
