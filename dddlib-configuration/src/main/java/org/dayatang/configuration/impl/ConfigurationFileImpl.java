package org.dayatang.configuration.impl;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.configuration.ConfigurationException;
import org.dayatang.configuration.WritableConfiguration;
import org.dayatang.utils.Assert;
import org.dayatang.utils.Slf4JLogger;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 * <P>ConfigurationFileImpl为读取/回写配置文件的工具类，一个实例大概对应了一个物理配置文件，可以使用
 * getXxx("aa.conf")，getXxx("/a/b","xx.conf") 获得/a/b/xx.conf配置文件。 具体配置大致采用
 * ConfigurationFileImpl.getXxx(key)的方式读取。</P>
 * <P>每个配置项用key --> value 的方式组织，推荐采用点分字符串的方式编制key部分。 usePrefix()激活
 * 配置项前缀功能，你可以通过usePrefix("xxx.xxx")设置某个具体实例的前缀。</P>
 * <P>前缀的作用在于减少复杂性，如果我们在配置文件里有org.dayatang.smbserverhost=smbserverhost.com
 * 这一项，并且不更改默认前缀的话，getXxx("smbserverhost")和get("org.dayatang.smbserverhost")
 * 将会返回同样的结果。</P>
 * <P>配置文件的格式符合标准的java属性文件格式，采用UTF8的编码方式，支持中文，不需native2ascii。</P>
 * <P>注意：为了避免日期格式的转换等复杂问题，日期是转化为long类型的数据保存的（采用date.getTime()方法）。</P>
 * 
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public class ConfigurationFileImpl extends AbstractConfiguration implements WritableConfiguration {
	private static final Slf4JLogger LOGGER = Slf4JLogger.getLogger(ConfigurationFileImpl.class);
	private PropertiesFileUtils pfu = new PropertiesFileUtils("utf-8");
	private File file;

	public static ConfigurationFileImpl fromFile(final File file) {
		return new ConfigurationFileImpl(file);
	}

	public ConfigurationFileImpl(final String pathname) {
		this(new File(pathname));
	}

	public ConfigurationFileImpl(final String dirPath, final String fileName) {
		this(new File(dirPath, fileName));
	}


	public ConfigurationFileImpl(final File file) {
		if (file == null) {
			throw new ConfigurationException("File " + file.getName() + " is null!");
		}
		if (!file.exists()) {
			throw new ConfigurationException("File " + file.getName() + " not found!");
		}
		if (!file.canRead()) {
			throw new ConfigurationException("File " + file.getName() + " is unreadable!");
		}
		this.file = file;
		load();
	}

	@Override
	public void load() {
		hTable = new Hashtable<String, String>();
		Properties props = new Properties();
		InputStream in = null;
		try {
			System.out.println(file.getName());
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
	 * @see org.dayatang.configuration.WritableConfiguration#save()
	 */
	@Override
	public void save() {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), PropertiesFileUtils.ISO_8859_1));
			store(getProperties(), out, "Config file for " + file);
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