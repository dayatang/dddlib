/*
 * Copyright (c) Koala 2012-2014 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.koala.commons;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-6-5 上午10:51:13  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class KoalaDateUtils {

	public static final String HHMM = "HHmm";
	public static final String DDMMMYY = "ddMMMyy";
	public static final String DD_MMMYYYY_HHMM = "ddMMMyyyy HHmm";
	public static final String DD_MMMYYYY_HH_MM = "ddMMMyyyy HH:mm";
	public static final String DD_MMMYYYY_HH_MM_SS = "ddMMMyyyy HH:mm:ss";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY2MM2DD = "yyyy/MM/dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String HH_MM = "HH:mm";
	public static final String YYYY_MM_DD2HH_mm = "yyyy-MM-dd-HH-mm";
	public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";

	/**
	 * 解析日期<br>
	 * 支持格式：<br>
	 * generate by: vakin jiang at 2012-3-1
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseDate(String dateStr) {
		SimpleDateFormat format = null;
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}

		String _dateStr = dateStr.trim();
		try {
			if (_dateStr.matches("\\d{1,2}[A-Z]{3}")) {
				_dateStr = _dateStr
						+ (Calendar.getInstance().get(Calendar.YEAR) - 2000);
			}
			// 01OCT12
			if (_dateStr.matches("\\d{1,2}[A-Z]{3}\\d{2}")) {
				format = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
			} else if (_dateStr.matches("\\d{1,2}[A-Z]{3}\\d{4}.*")) {// 01OCT2012
				// ,01OCT2012
				// 1224,01OCT2012
				// 12:24
				_dateStr = _dateStr.replaceAll("[^0-9A-Z]", "")
						.concat("000000").substring(0, 15);
				format = new SimpleDateFormat("ddMMMyyyyHHmmss", Locale.ENGLISH);
			} else {
				StringBuffer sb = new StringBuffer(_dateStr);
				String[] tempArr = _dateStr.split("\\s+");
				tempArr = tempArr[0].split("-|\\/");
				if (tempArr.length == 3) {
					if (tempArr[1].length() == 1) {
						sb.insert(5, "0");
					}
					if (tempArr[2].length() == 1) {
						sb.insert(8, "0");
					}
				}
				_dateStr = sb.append("000000").toString().replaceAll("[^0-9]",
						"").substring(0, 14);
				if (_dateStr.matches("\\d{14}")) {
					format = new SimpleDateFormat("yyyyMMddHHmmss");
				}
			}

			Date date = format.parse(_dateStr);
			return date;
		} catch (Exception e) {
			throw new RuntimeException("无法解析日期字符[" + dateStr + "]");
		}
	}

	/**
	 * 解析日期字符串转化成日期格式<br>
	 * generate by: vakin jiang at 2012-3-1
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String dateStr, String pattern) {
		try {
			SimpleDateFormat format = null;
			if (StringUtils.isBlank(dateStr)) {
				return null;
			}

			if (StringUtils.isNotBlank(pattern)) {
				format = new SimpleDateFormat(pattern);
				return format.parse(dateStr);
			}
			return parseDate(dateStr);
		} catch (Exception e) {
			throw new RuntimeException("无法解析日期字符[" + dateStr + "]");
		}
	}

	/**
	 * 获取一天开始时间<br>
	 * generate by: vakin jiang at 2011-12-23
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayBegin(Date date) {
		String format = DateFormatUtils.format(date, YYYY_MM_DD);
		return parseDate(format.concat(" 00:00:00"));
	}

	/**
	 * 获取一天结束时间<br>
	 * generate by: vakin jiang at 2011-12-23
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		String format = DateFormatUtils.format(date, YYYY_MM_DD);
		return parseDate(format.concat(" 23:59:59"));
	}

	/**
	 * 时间戳格式转换为日期（年月日）格式<br>
	 * generate by: vakin jiang at 2011-12-23
	 * 
	 * @param date
	 * @return
	 */
	public static Date timestamp2Date(Date date) {
		return formatDate(date, YYYY_MM_DD);
	}

	/**
	 * 时间戳格式转换为日期（年月日）格式<br>
	 * generate by: vakin jiang at 2011-12-23
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate2YYYY_MM_DD(Date date) {
		return format(date, YYYY_MM_DD);
	}
	
	 /**
     * 格式化日期格式为：ddMMMyy<br>
     * generate by: vakin jiang
     *                    at 2012-10-17
     * @param dateStr
     * @return
     */
    public static String format2ddMMMyy(Date date){
        SimpleDateFormat format = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        return format.format(date).toUpperCase();
    }

	 /**
     * 格式化日期格式为：ddMMMyy<br>
     * generate by: vakin jiang
     *                    at 2012-10-17
     * @param dateStr
     * @return
     */
    public static String format2ddMMMyy(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        return format.format(parseDate(dateStr)).toUpperCase();
    }

	/**
	 * 格式化日期字符串<br>
	 * generate by: vakin jiang at 2012-3-7
	 * 
	 * @param dateStr
	 * @param patterns
	 * @return
	 */
	public static String formatDateStr(String dateStr, String... patterns) {
		String pattern = YYYY_MM_DD_HH_MM_SS;
		if (patterns != null && patterns.length > 0
				&& StringUtils.isNotBlank(patterns[0])) {
			pattern = patterns[0];
		}
		return DateFormatUtils.format(parseDate(dateStr), pattern);
	}

	/**
	 * 格式化日期为日期字符串<br>
	 * generate by: vakin jiang at 2012-3-7
	 * 
	 * @param orig
	 * @param patterns
	 * @return
	 */
	public static String format(Date date, String... patterns) {
		if (date == null)
			return "";
		String pattern = YYYY_MM_DD_HH_MM_SS;
		if (patterns != null && patterns.length > 0
				&& StringUtils.isNotBlank(patterns[0])) {
			pattern = patterns[0];
		}
		return DateFormatUtils.format(date, pattern);
	}

	public static String format2YYYY_MM_DD(Date date) {
		return format(date, YYYY_MM_DD);
	}

	/**
	 * 格式化日期为指定格式<br>
	 * generate by: vakin jiang at 2012-3-7
	 * 
	 * @param orig
	 * @param patterns
	 * @return
	 */
	public static Date formatDate(Date orig, String... patterns) {
		String pattern = YYYY_MM_DD_HH_MM_SS;
		if (patterns != null && patterns.length > 0
				&& StringUtils.isNotBlank(patterns[0])) {
			pattern = patterns[0];
		}
		return parseDate(DateFormatUtils.format(orig, pattern));
	}

	/**
	 * 当前国际时间<br>
	 * generate by: vakin jiang at 2012-6-4
	 * 
	 * @return
	 */
	public static Date currentGMTTime() {
		return DateUtils.addHours(Calendar.getInstance(Locale.CHINA).getTime(), -8);
	}

}
