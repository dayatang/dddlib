package org.dayatang.configuration;

import org.dayatang.utils.Assert;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 数据库工厂，用于隐藏配置类的具体实现
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class ConfigurationFactory {

    private static ConfigurationFactory instance = getInstance();

    private static ConfigurationFactory getInstance() {
		Iterator<ConfigurationFactory> iterator = ServiceLoader.load(ConfigurationFactory.class).iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		throw new IllegalStateException("ConfigurationFactory implement class not found!");
    }

    public static ConfigurationFactory singleton() {
        return instance;
    }

	/**
	 * 从JDBC数据源中读取配置
	 * @param dataSource 数据源
	 * @return 可写的配置
	 */
	public WritableConfiguration fromDatabase(DataSource dataSource) {
		return instance.fromDatabase(dataSource);
	}

	/**
	 * 从JDBC数据源中读取配置
	 * @param dataSource 数据源
	 * @param tableName 存放配置信息的数据表名
	 * @param keyColumn 存放配置键的列名
	 * @param valueColumn 存放配置值的列名
	 * @return 可写的配置
	 */
	public WritableConfiguration fromDatabase(DataSource dataSource, String tableName, String keyColumn, String valueColumn) {
		return instance.fromDatabase(dataSource, tableName, keyColumn, valueColumn);
	}

	/**
	 * 从类路径文件中读取配置
	 * @param fileName 类路径资源文件名
	 * @return 可写的配置
	 */
	public Configuration fromClasspath(String fileName) {
		return instance.fromClasspath(fileName);
	}

	/**
	 * 从磁盘文件中读取配置
	 * @param fileName 磁盘文件名
	 * @return 可写的配置
	 */
	public WritableConfiguration fromFileSystem(String fileName) {
		return instance.fromFileSystem(fileName);
	}

	/**
	 * 从磁盘文件中读取配置
	 * @param dirPath 文件所属的目录
	 * @param fileName 文件名
	 * @return 可写的配置
	 */
	public WritableConfiguration fromFileSystem(String dirPath, String fileName) {
		return instance.fromFileSystem(dirPath, fileName);
	}

	/**
	 * 从磁盘文件中读取配置
	 * @param file 包含配置信息的文件
	 * @return 可写的配置
	 */
	public WritableConfiguration fromFileSystem(File file) {
		return instance.fromFileSystem(file);
	}

	/**
	 * 从远程url读取配置
	 * @param url 包含配置信息的url
	 * @return 只读的配置
	 */
	public Configuration fromUrl(String url) {
		return instance.fromUrl(url);
	}

	/**
	 * 从远程url读取配置
	 * @param url 包含配置信息的url
	 * @return 只读的配置
	 */
	public Configuration fromUrl(URL url) {
		return instance.fromUrl(url);
	}

	/**
	 * 从输入流中读取配置信息
	 * @param in 输入流
	 * @return 只读的配置
	 */
	public Configuration fromInputStream(InputStream in) {
		return  instance.fromInputStream(in);
	}
	
}
