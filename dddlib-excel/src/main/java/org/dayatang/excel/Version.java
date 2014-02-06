package org.dayatang.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Date;

public enum Version {
	XLS {
		@Override
		public Date getDate(Double value, boolean isDate1904) {
			return value == null ? null : HSSFDateUtil.getJavaDate(value, isDate1904);
		}
	},
	XLSX {
		@Override
		public Date getDate(Double value, boolean isDate1904) {
			return value == null ? null : DateUtil.getJavaDate(value, isDate1904);
		}
	};

	public static Version of(String filename) {
		if (filename.toLowerCase().endsWith("xls")) {
			return XLS;
		}
		if (filename.toLowerCase().endsWith("xlsx")) {
			return XLSX;
		}
		throw new IllegalStateException("Unsupported file type!");
	}

	public abstract Date getDate(Double value, boolean isDate1904);
}
