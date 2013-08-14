package com.dayatang.utils;


/**
 * 通用日志接口
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface Logger {
	
	public void debug(String msg, Object... args);
	
	public void debug(String msg, Throwable t);
	
	public void info(String msg, Object... args);
	
	public void info(String msg, Throwable t);
	
	public void trace(String format, Object... args);
	
	public void trace(String msg, Throwable t);
	
	public void warn(String format, Object... args);
	
	public void warn(String msg, Throwable t);
	
	public void error(String format, Object... args);
	
	public void error(String msg, Throwable t);
	
}
