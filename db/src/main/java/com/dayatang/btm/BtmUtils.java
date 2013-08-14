package com.dayatang.btm;

import java.net.URISyntaxException;
import java.net.URL;

import javax.transaction.UserTransaction;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;

/**
 * 通过开源JTA实现库BTM创建数据源并绑定到JNDI.
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class BtmUtils {


	private static final String DATASOURCE_CONF_FILE = "/datasources.properties";
	
	/**
	 * 从类路径读入数据源配置文件
	 * @param resourceFile
	 * @return
	 */
	public static BtmUtils readConfigurationFromClasspath(String resourceFile) {
		return new BtmUtils(getAbsolutePathOfClasspathResource(resourceFile));
	}
	
	private static String getAbsolutePathOfClasspathResource(String resourceFile) {
		URL url = BtmUtils.class.getResource(resourceFile);
		if (url == null) {
			throw new RuntimeException("File " + resourceFile + " does not exist!");
		}
		try {
			return url.toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException("File " + resourceFile + " does not exist!");
		}
	}

	/**
	 * 从文件系统读入数据源配置文件
	 * @param confFile
	 * @return
	 */
	public static BtmUtils readConfigurationFromFile(String confFile) {
		return new BtmUtils(confFile);
	}

	
	private String confFile;
	
	public BtmUtils() {
		this(getAbsolutePathOfClasspathResource(DATASOURCE_CONF_FILE));
	}

	private BtmUtils(String confFile) {
		this.confFile = confFile;
		System.out.println("----------------------------"); 
		System.out.println("LogPart1Filename:" + TransactionManagerServices.getConfiguration().getLogPart1Filename());
	}
	
	/**
	 * 根据数据源配置设置JNDI和事务管理器
	 * @throws Exception
	 */
	public void setupDataSource() {
        TransactionManagerServices.getConfiguration().setResourceConfigurationFilename(confFile);
        TransactionManagerServices.getTransactionManager();
	}

	/**
	 * 关闭BTM服务并释放资源
	 * @throws Exception
	 */
    public void closeDataSource() {
    	TransactionManagerServices.getTransactionManager().shutdown();
    	TransactionManagerServices.getConfiguration().shutdown();
    }

	public UserTransaction getTransaction() {
		return TransactionManagerServices.getTransactionManager();
	}

	public BitronixTransactionManager getTransactionManager() {
		return TransactionManagerServices.getTransactionManager();
	}
}
