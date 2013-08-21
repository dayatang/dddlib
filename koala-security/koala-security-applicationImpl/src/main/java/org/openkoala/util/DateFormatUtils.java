package org.openkoala.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * DateFormat工具类（解决多线程访问出现问题）
 * @author zyb
 * @since 2013-5-7 上午10:27:30
 */
public final class DateFormatUtils {

	// 将DateFormat实例放到ThreadLocal中
	private static final ThreadLocal<Formater> FORMATER_LOCAL = new ThreadLocal<Formater>();
	
	private static DateFormatUtils dateFormatUtils;
	
	private static final Logger LOGGER = Logger.getLogger("DateFormatUtils");
	
	private DateFormatUtils() {
		
	}
	
	private class Formater {
		
		private DateFormat dateFormat;
		
		// 默认转换模式
		private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss"; 
		
		Formater() {
			dateFormat = new SimpleDateFormat(DEFAULT_PATTERN);
		}
		
		Formater(String pattern) {
			dateFormat = new SimpleDateFormat(pattern);
		}
		
		public DateFormat getDateFormat() {
			return dateFormat;
		}
		
	}
	
	/**
	 * 将日期转成字符串
	 * @param date
	 * @return
	 */
	public static String format(Date date, String pattern) {
		set(pattern);
		return FORMATER_LOCAL.get().getDateFormat().format(date);
	}
	
	/**
	 * 将字符串转成日期
	 * @param source
	 * @return
	 */
	public static Date parse(String source, String pattern) {
		set(pattern);
		try {
			return FORMATER_LOCAL.get().getDateFormat().parse(source);
		} catch (ParseException e) {
			LOGGER.info(e.getMessage());
		}
		return null;
	}
	
	public static Date parse(String source) {
		return parse(source, null);
	}
	
	public static String format(Date date) {
		return format(date, null);
	}
	
	private static synchronized DateFormatUtils getInstance() {
		if (dateFormatUtils == null) {
			dateFormatUtils = new DateFormatUtils();
		}
		return dateFormatUtils;
	}
	
	/**
	 * 将Formater实例放入到ThreadLocal中
	 * @param pattern
	 */
	private static void set(String pattern) {
		if (pattern != null && !"".equals(pattern)) {
			FORMATER_LOCAL.set(getInstance().new Formater(pattern));
		} else {
			FORMATER_LOCAL.set(getInstance().new Formater());
		}
	}
	
}