package org.dayatang.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class WorkbookFactory {
	
	private WorkbookFactory() {
		super();
	}

	public static Workbook createWorkbook(File excelFile) {
		return createWorkbook(excelFile, Version.of(excelFile.getName()));
	}

	public static Workbook createWorkbook(File excelFile, Version version) {
		try {
			return createWorkbook(new FileInputStream(excelFile), version);
		} catch (FileNotFoundException e) {
			throw new ExcelException("File " + excelFile.getPath() + " not exists.", e);
		}
	}
	
	public static Workbook createWorkbook(InputStream in, Version version) {
			try {
				if (version == Version.XLSX) {
					return new XSSFWorkbook(in);
				} else {
					return new HSSFWorkbook(in);
				}
			} catch (IOException e) {
				throw new ExcelException(e);
			}
	}
}
