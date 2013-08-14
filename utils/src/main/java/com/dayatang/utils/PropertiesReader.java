package com.dayatang.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 从类路径资源、磁盘文件和输入流中读取属性配置。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class PropertiesReader {

	private PropertiesReader() {
	}

	public static Properties readPropertiesFromClasspath(String propertiesFile) {
		InputStream in = PropertiesReader.class.getResourceAsStream(propertiesFile);
		return readPropertiesFromInputStream(new BufferedInputStream(in));
	}

	public static Properties readPropertiesFromFile(String propertiesFile) {
		try {
			InputStream in = new FileInputStream(new File(propertiesFile));
			return readPropertiesFromInputStream(new BufferedInputStream(in));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File " + propertiesFile + " not found.", e);
		}
	}
	
	public static Properties readPropertiesFromInputStream(InputStream in) {
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load properties.", e);
		}
		return properties;
	}

}
