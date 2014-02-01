package com.dayatang.utils;


/**
 * 通用日志接口
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface Logger {
	
	 void debug(String msg, Object... args);
	
	 void debug(String msg, Throwable t);
	
	 void info(String msg, Object... args);
	
	 void info(String msg, Throwable t);
	
	 void trace(String format, Object... args);
	
	 void trace(String msg, Throwable t);
	
	 void warn(String format, Object... args);
	
	 void warn(String msg, Throwable t);
	
	 void error(String format, Object... args);
	
	 void error(String msg, Throwable t);
	
}
