package org.dayatang.configuration.impl;

import org.dayatang.configuration.Configuration;
import org.dayatang.configuration.ConfigurationFactory;
import org.dayatang.configuration.WritableConfiguration;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;

/**
 * Created by yyang on 14-2-5.
 */
public class ConfigurationFactoryImpl extends ConfigurationFactory {

    /**
     * 从JDBC数据源中读取配置
     * @param dataSource 数据源
     * @return 可写的配置
     */
    public WritableConfiguration fromDatabase(DataSource dataSource) {
        return new ConfigurationDbImpl(dataSource);
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
        return new ConfigurationDbImpl(dataSource, tableName, keyColumn, valueColumn);
    }

    /**
     * 从类路径文件中读取配置
     * @param fileName 类路径资源文件名
     * @return 可写的配置
     */
    public WritableConfiguration fromClasspath(String fileName) {
        return ConfigurationFileImpl.fromClasspath(fileName);
    }

    /**
     * 从磁盘文件中读取配置
     * @param fileName 磁盘文件名
     * @return 可写的配置
     */
    public WritableConfiguration fromFileSystem(String fileName) {
        return ConfigurationFileImpl.fromFileSystem(fileName);
    }

    /**
     * 从磁盘文件中读取配置
     * @param dirPath 文件所属的目录
     * @param fileName 文件名
     * @return 可写的配置
     */
    public WritableConfiguration fromFileSystem(String dirPath, String fileName) {
        return ConfigurationFileImpl.fromFileSystem(dirPath, fileName);
    }

    /**
     * 从磁盘文件中读取配置
     * @param file 包含配置信息的文件
     * @return 可写的配置
     */
    public WritableConfiguration fromFileSystem(File file) {
        return ConfigurationFileImpl.fromFile(file);
    }

    /**
     * 从远程url读取配置
     * @param url 包含配置信息的url
     * @return 只读的配置
     */
    public Configuration fromUrl(String url) {
        return ConfigurationUrlImpl.fromUrl(url);
    }

    /**
     * 从远程url读取配置
     * @param url 包含配置信息的url
     * @return 只读的配置
     */
    public Configuration fromUrl(URL url) {
        return ConfigurationUrlImpl.fromUrl(url);
    }
}
