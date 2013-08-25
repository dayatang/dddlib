package com.dayatang.configuration.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.configuration.ConfigurationException;
import com.dayatang.configuration.WritableConfiguration;
import com.dayatang.utils.Assert;
import com.dayatang.utils.Slf4jLogger;

/**
 * <P>ConfigurationFileImpl为读取/回写配置文件的工具类，一个实例大概对应了一个物理配置文件，可以使用
 * getXxx("aa.conf")，getXxx("/a/b","xx.conf") 获得/a/b/xx.conf配置文件。 具体配置大致采用
 * ConfigurationFileImpl.getXxx(key)的方式读取。</P>
 * <P>每个配置项用key --> value 的方式组织，推荐采用点分字符串的方式编制key部分。 usePrefix()激活
 * 配置项前缀功能，你可以通过usePrefix("xxx.xxx")设置某个具体实例的前缀。</P>
 * <P>前缀的作用在于减少复杂性，如果我们在配置文件里有com.dayatang.smbserverhost=smbserverhost.com
 * 这一项，并且不更改默认前缀的话，getXxx("smbserverhost")和get("com.dayatang.smbserverhost")
 * 将会返回同样的结果。</P>
 * <P>配置文件的格式符合标准的java属性文件格式，采用UTF8的编码方式，支持中文，不需native2ascii。</P>
 * <P>注意：为了避免日期格式的转换等复杂问题，日期是转化为long类型的数据保存的（采用date.getTime()方法）。</P>
 * 
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public class ConfigurationFileImpl extends AbstractConfiguration implements WritableConfiguration {
	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(ConfigurationFileImpl.class);
	private PropertiesFileUtils pfu = new PropertiesFileUtils("utf-8");
	private File file;
	/**
	 * 从类路径读入配置文件
	 * @param fileName
	 * @return
	 */
	public static ConfigurationFileImpl fromClasspath(final String fileName) {
		Assert.notBlank(fileName, String.format("File name %s is empty!", fileName));
		URL url = ConfigurationFileImpl.class.getResource(fileName);
		Assert.notNull(url, String.format("File {} not found!", fileName));
		File file = new File(url.getFile());
		return new ConfigurationFileImpl(file);
	}
	
	/**
	 * 从文件系统读入配置文件
	 * @param pathname
	 * @return
	 */
	public static ConfigurationFileImpl fromFileSystem(final String pathname) {
		Assert.notBlank(pathname, String.format("File name %s is empty!", pathname));
		return fromFileSystem(new File(pathname));
	}
	
	/**
	 * 从文件系统读入配置文件
	 * @param dirPath
	 * @param fileName
	 * @return
	 */
	public static ConfigurationFileImpl fromFileSystem(final String dirPath, final String fileName) {
		Assert.notBlank(dirPath, String.format("Directory %s is empty!", dirPath));
		Assert.notBlank(fileName, String.format("File name %s is empty!", fileName));
		return fromFileSystem(new File(dirPath, fileName));
	}
	
	public static ConfigurationFileImpl fromFileSystem(final File file) {
		if (!file.exists()) {
			throw new ConfigurationException("File " + file.getName() + " not found!");
		}
		if (!file.canRead()) {
			throw new ConfigurationException("File " + file.getName() + " is unreadable!");
		}
		return new ConfigurationFileImpl(file);
	}

	private ConfigurationFileImpl(final File file) {
		this.file = file;
		if (!file.exists()) {
			throw new IllegalArgumentException(String.format("File $s not exists!", file.getAbsolutePath()));
		}
		if (!file.canRead()) {
			throw new IllegalStateException("File " + file.getName() + " is unreadable!");
		}
		load();
	}

	@Override
	public Properties getProperties() {
		return pfu.unRectifyProperties(getHashtable());
	}

	@Override
	public void load() {
		hTable = new Hashtable<String, String>();
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			props.load(in);
			hTable = pfu.rectifyProperties(props);
			LOGGER.debug("Load configuration from {} at {}", file.getAbsolutePath(), new Date());
		} catch (IOException e) {
			throw new ConfigurationException("Cannot load config file: " + file, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new ConfigurationException("Cannot close input stream.", e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.dayatang.configuration.WritableConfiguration#save()
	 */
	@Override
	public void save() {
		BufferedWriter out = null;
		try {
			Properties props = pfu.unRectifyProperties(getHashtable());
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), PropertiesFileUtils.ISO_8859_1));
			store(props, out, "Config file for " + file);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new ConfigurationException("Cannot close input stream.", e);
				}
			}
		}
	}

	private void store(Properties props, BufferedWriter out, String comments) throws IOException {
		if (StringUtils.isNotEmpty(comments)) {
			out.append("#" + comments);
			out.newLine();
		}
		out.write("#" + new Date().toString());
		out.newLine();
		synchronized (this) {
			for (Map.Entry<Object, Object> each : props.entrySet()) {
				String key = convertString((String) each.getKey(), true);
				String value = convertString((String) each.getValue(), false);
				out.write(key + "=" + value);
				out.newLine();
			}
			out.flush();
		}
		LOGGER.debug("Save configuration to {} at {}", file.getAbsolutePath(), new Date());
	}

	private String convertString(String theString, boolean escapeSpace) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuilder outBuffer = new StringBuilder(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			// Handle common case first, selecting largest block that
			// avoids the specials below
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch (aChar) {
			case ' ':
				if (x == 0 || escapeSpace) {
					outBuffer.append('\\');
				}
				outBuffer.append(' ');
				break;
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			case '=': // Fall through
			case ':': // Fall through
			case '#': // Fall through
			case '!':
				outBuffer.append('\\');
				outBuffer.append(aChar);
				break;
			default:
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + file + "}";
	}
}