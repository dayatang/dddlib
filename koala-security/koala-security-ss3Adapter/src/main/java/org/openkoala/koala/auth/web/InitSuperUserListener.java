package org.openkoala.koala.auth.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 初始化超级管理员
 * @author zhuyuanbiao
 * @date 2014年1月21日 下午5:19:49
 *
 */
public class InitSuperUserListener implements ServletContextListener {
	
	private static Logger logger = LoggerFactory.getLogger(InitSuperUserListener.class);
	
	private Properties properties;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("初始化监听器启动...");
		
		initProperties();
		
		initSuperUser(sce);
	}

	private void initProperties() {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("/META-INF/props/user-config.properties");
		properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			logger.error("读取配置文件失败：{}", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("关闭IO失败：{}", e);
			}
		}
	}

	private WebApplicationContext getWebApplicationContext(ServletContextEvent sce) {
		return WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
	}
	
	private UserApplication getUserApplication(ServletContextEvent sce) {
		return getWebApplicationContext(sce).getBean(UserApplication.class);
	}
	
	private PasswordEncoder getPasswordEncoder(ServletContextEvent sce) {
		return getWebApplicationContext(sce).getBean(PasswordEncoder.class);
	}

	private void initSuperUser(ServletContextEvent sce) {
		if (getUserApplication(sce).findByUserAccount(getUserAccount()) != null) {
			logger.info("超级管理员【{}】已经存在.", getUserAccount());
			return;
		}
		
		UserVO userVO = new UserVO();
		userVO.setUserAccount(getUserAccount());
		userVO.setUserPassword(getPasswordEncoder(sce).encode(getPassword()));
		userVO.setEmail(getEmail());
		userVO.setSuper(true);
		userVO.setUserDesc(getUserDesc());
		userVO.setName(getRealName());
		userVO.setValid(true);
		
		try {
			getUserApplication(sce).saveUser(userVO);
			logger.info("初始化超级管理员成功：{}", getUserAccount());
		} catch (Exception e) {
			logger.error("初始化超级管理员失败：{}", e);
		}
	}

	private String getRealName() {
		return properties.getProperty("name");
	}

	private String getUserDesc() {
		return properties.getProperty("desc");
	}

	private String getEmail() {
		return properties.getProperty("email");
	}

	private String getPassword() {
		return properties.getProperty("password");
	}

	private String getUserAccount() {
		return properties.getProperty("useraccount");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
