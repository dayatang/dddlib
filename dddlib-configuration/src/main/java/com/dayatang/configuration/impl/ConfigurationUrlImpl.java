package com.dayatang.configuration.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import com.dayatang.configuration.ConfigurationException;
import com.dayatang.utils.Slf4jLogger;

/**
 * <P>ConfigurationUrlImpl为读取远程配置文件的工具类，一个实例大概对应了一个远程配置文件，具体配置大致采用
 * ConfigurationUrlImpl.getXxx(key)的方式读取。</P>
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
public class ConfigurationUrlImpl extends AbstractConfiguration {
	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(ConfigurationUrlImpl.class);
	private PropertiesFileUtils pfu = new PropertiesFileUtils("utf-8");
	private URL url;
	
	public static ConfigurationUrlImpl fromUrl(final String url) {
		try {
			return new ConfigurationUrlImpl(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new IllegalStateException("URL " + url + " is malformed!" , e);
		}
	}
	
	public static ConfigurationUrlImpl fromUrl(final URL url) {
		return new ConfigurationUrlImpl(url);
	}

	private ConfigurationUrlImpl(final URL url) {
		this.url = url;
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
			in = url.openStream();
			props.load(in);
			hTable = pfu.rectifyProperties(props);
			LOGGER.debug("Load configuration from {} at {}", url, new Date());
		} catch (IOException e) {
			throw new ConfigurationException("Cannot load config file: " + url, e);
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + url + "}";
	}
}