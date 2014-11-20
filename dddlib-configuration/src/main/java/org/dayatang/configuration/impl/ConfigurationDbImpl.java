package org.dayatang.configuration.impl;

import org.dayatang.configuration.WritableConfiguration;
import org.dayatang.utils.Assert;
import org.dayatang.utils.Slf4JLogger;

import javax.sql.DataSource;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * <P>ConfigurationDbImpl为读取/回写配置信息的工具类，并将配置信息写入数据库， 具体配置大致采用
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
public class ConfigurationDbImpl extends AbstractConfiguration implements WritableConfiguration {
	
	private static final Slf4JLogger LOGGER = Slf4JLogger.getLogger(ConfigurationDbImpl.class);
	
	private ConfigurationDbUtils dbUtils;
	private static final String DEFAULT_TABLE_NAME = "SYS_CONFIG";
	private static final String DEFAULT_KEY_COLUMN = "KEY_COLUMN";
	private static final String DEFAULT_VALUE_COLUMN = "VALUE_COLUMN";
	
	
	public ConfigurationDbImpl(DataSource dataSource) {
		this(dataSource, DEFAULT_TABLE_NAME, DEFAULT_KEY_COLUMN, DEFAULT_VALUE_COLUMN);
	}

	public ConfigurationDbImpl(DataSource dataSource, String tableName, String keyColumn, String valueColumn) {
		Assert.notNull(dataSource, "Data source is null!");
		Assert.notEmpty(tableName, "Table name is blank!");
		Assert.notEmpty(tableName, "Key column is blank!");
		Assert.notEmpty(tableName, "Value column is blank!");
		dbUtils = new ConfigurationDbUtils(dataSource, tableName, keyColumn, valueColumn);
	}

	@Override
	public Properties getProperties() {
		Properties results = new Properties();
		for (Map.Entry<String, String> each : getHashtable().entrySet()) {
			results.put(each.getKey(), each.getValue());
		}
		return results;
	}

	//从数据库中取得配置项，更新当前内存中的配置值。
	@Override
	public void load() {
		hTable = dbUtils.load();
		LOGGER.debug("Configuration info loaded from database at {}", new Date());
	}

	/* (non-Javadoc)
	 * @see org.dayatang.configuration.WritableConfiguration#save()
	 */
	@Override
	public void save() {
		dbUtils.save(hTable);
	}
}