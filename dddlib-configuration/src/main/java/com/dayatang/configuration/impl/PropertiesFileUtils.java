package com.dayatang.configuration.impl;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Properties;

import com.dayatang.configuration.ConfigurationException;

/**
 * 解决Java Properties文件的编码问题，避免iso8859-1到utf8的相互转义
 * 
 * @author jzhai
 */
public class PropertiesFileUtils {
	public static final String ISO_8859_1 = "iso8859-1";
	private String encoding = "UTF-8";

	public PropertiesFileUtils(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 将Properties文件，其中含有原生的UTF8等编码字符，转换为编码正确的Hashtable
	 */
	public Hashtable<String, String> rectifyProperties(Properties p) {
		if (p == null) {
			return null;
		}
		Hashtable<String, String> ret = new Hashtable<String, String>();
		for (Entry<Object, Object> e : p.entrySet()) {
			String key = (String) e.getKey();
			String value = (String) e.getValue();
			ret.put(key, StringPropertyReplacer.replaceProperties(rectifyStr(value)));
		}
		return ret;
	}

	/**
	 * 将编码正确的Hashtable, 转换为待写入Properties文件的对象
	 */
	public Properties unRectifyProperties(Hashtable<String, String> h) {
		if (h == null) {
			return null;
		}
		Properties ret = new Properties();
		for (Entry<String, String> e : h.entrySet()) {
			String key = e.getKey();
			String value = e.getValue();
			ret.put(key, unRectifyStr(value));
		}
		return ret;
	}

	/**
	 * 修正Properities中原生字符串为正确的编码
	 */
	public String rectifyStr(String raw) {
		if (raw == null) {
			return raw;
		}
		String ret = raw;
		try {
			byte[] bytes = raw.getBytes(ISO_8859_1);
			ret = new String(bytes, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationException("Unsupport Encoding:" + encoding, e);
		}
		return ret;
	}

	/**
	 * 还原正确的字符串编码为ISO编码
	 */
	public String unRectifyStr(String validStr) {
		if (validStr == null) {
			return validStr;
		}
		String ret = validStr;
		try {
			byte[] bytes = validStr.getBytes(encoding);
			ret = new String(bytes, ISO_8859_1);
		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationException("Unsupport Encoding:" + encoding, e);
		}
		return ret;
	}
}