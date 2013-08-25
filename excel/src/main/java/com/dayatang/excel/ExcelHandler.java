package com.dayatang.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel读写类
 * 
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 * 
 */
public class ExcelHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelHandler.class);
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private Workbook workbook;
	private Version version;

	public ExcelHandler() {
		workbook = new HSSFWorkbook();
		workbook.createSheet();
		this.version = Version.XLS;
	}

	public ExcelHandler(File excelFile) {
		workbook = WorkbookFactory.createWorkbook(excelFile);
		this.version = Version.of(excelFile.getName());
	}

	public ExcelHandler(File excelFile, Version version) {
		workbook = WorkbookFactory.createWorkbook(excelFile, version);
		this.version = version;
	}

	public ExcelHandler(InputStream excelStream, Version version) {
		workbook = WorkbookFactory.createWorkbook(excelStream, version);
		this.version = version;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	/**
	 * 从指定的工作表范围读取数据
	 * 
	 * @param excelRange
	 * @return
	 */
	public ExcelRangeData readRange(ExcelRange excelRange) {
		Sheet sheet = getSheet(excelRange);
		List<Object[]> data = new ArrayList<Object[]>();
		int lastRow = excelRange.getRowTo() < 0 ? getLastRowNum(sheet, excelRange) : excelRange.getRowTo();
		if (lastRow < excelRange.getRowFrom()) { // 没有数据
			return new ExcelRangeData(data, version, isDate1904());
		}
		for (int rowIndex = excelRange.getRowFrom(); rowIndex <= lastRow; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			Object[] rowData = new Object[excelRange.getColumnTo() - excelRange.getColumnFrom() + 1];
			for (int columnIndex = excelRange.getColumnFrom(); columnIndex <= excelRange.getColumnTo(); columnIndex++) {
				Cell cell = row.getCell(columnIndex, Row.CREATE_NULL_AS_BLANK);
				rowData[columnIndex - excelRange.getColumnFrom()] = getCellValue(cell);
			}
			data.add(rowData);
		}
		return new ExcelRangeData(data, version, isDate1904());
	}

	private Sheet getSheet(ExcelRange excelRange) {
		int sheetIndex = excelRange.getSheetIndex();
		String sheetName = excelRange.getSheetName();
		if (workbook.getNumberOfSheets() == 0) {
			return workbook.createSheet("sheet1");
		}
		return sheetIndex < 0 ? getSheet(sheetName) : getSheet(sheetIndex);
	}

	private Sheet getSheet(ExcelCell excelCell) {
		int sheetIndex = excelCell.getSheetIndex();
		String sheetName = excelCell.getSheetName();
		if (workbook.getNumberOfSheets() == 0) {
			return workbook.createSheet("sheet1");
		}
		return sheetIndex < 0 ? getSheet(sheetName) : getSheet(sheetIndex);
	}

	private Sheet getSheet(int sheetIndex) {
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		if (sheet == null) {
			throw new IllegalArgumentException("Sheet index (" + sheetIndex + ") is out of range (0.." + (workbook.getNumberOfSheets() - 1) + ")");
		}
		return sheet;
	}

	private Sheet getSheet(String sheetName) {
		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			throw new IllegalArgumentException("Sheet name (" + sheetName + ") does not exists.)");
		}
		return sheet;
	}

	private int getLastRowNum(Sheet sheet, ExcelRange excelRange) {
		int lastRowNum = sheet.getLastRowNum();
		for (int row = excelRange.getRowFrom(); row <= lastRowNum; row++) {
			boolean isBlankRow = true;
			for (int column = excelRange.getColumnFrom(); column <= excelRange.getColumnTo(); column++) {
				if (sheet.getRow(row) == null) {
					return row - 1;
				}
				Object cellValue = getCellValue(sheet.getRow(row).getCell(column));
				if (cellValue != null && StringUtils.isNotBlank(cellValue.toString())) { // 本行非空行，检验下一行
					isBlankRow = false;
					break;
				}
			}
			if (isBlankRow) { // 代码进入此处说明整行为空行，
				return row - 1;
			}
		}
		return lastRowNum;
	}

	/**
	 * 从工作表的指定单元格中读取字符串数据
	 * @param excelCell
	 * @return
	 */
	public String readString(ExcelCell excelCell) {
		return ExcelUtils.getString(readCell(excelCell));
	}

	/**
	 * 从工作表的指定单元格中读取整数数据
	 * @param excelCell
	 * @return
	 */
	public Integer readInt(ExcelCell excelCell) {
		return ExcelUtils.getInt(readCell(excelCell));
	}

	/**
	 * 从工作表的指定单元格中读取长整数数据
	 * @param excelCell
	 * @return
	 */
	public Long readLong(ExcelCell excelCell) {
		return ExcelUtils.getLong(readCell(excelCell));
	}

	/**
	 * 从工作表的指定单元格中读取Double数据
	 * @param excelCell
	 * @return
	 */
	public Double readDouble(ExcelCell excelCell) {
		return ExcelUtils.getDouble(readCell(excelCell));
	}

	/**
	 * 从工作表的指定单元格中读取布尔数据
	 * @param excelCell
	 * @return
	 */
	public Boolean readBoolean(ExcelCell excelCell) {
		return ExcelUtils.getBoolean(readCell(excelCell));
	}

	/**
	 * 从工作表的指定单元格中读取日期数据
	 * @param excelCell
	 * @return
	 */
	public Date readDate(ExcelCell excelCell) {
		return ExcelUtils.getDate(readCell(excelCell), version, isDate1904());
	}

	private Object readCell(ExcelCell excelCell) {
		return getCellValue(getSheet(excelCell).getRow(excelCell.getRowIndex()).getCell(excelCell.getColumnIndex()));
	}

	/**
	 * 检测Excel工作簿是否采用1904日期系统
	 */
	private boolean isDate1904() {
		Sheet sheet = workbook.createSheet();
		int sheetIndex = workbook.getSheetIndex(sheet);
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue(0.0);
		boolean is1994 = isDate1904(cell);
		workbook.removeSheetAt(sheetIndex);
		return is1994;
	}

	/**
	 * throws an exception for non-numeric cells
	 */
	private static boolean isDate1904(Cell cell) {
		double value = cell.getNumericCellValue();
		Date date = cell.getDateCellValue();
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		long year1900 = cal.get(Calendar.YEAR) - 1900;
		long yearEst1900 = Math.round(value / (365.25));
		return year1900 > yearEst1900;
	}

	private Object getCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
				LOGGER.error("Cell content is error. Sheet: {}, row: {}, column: {}",
						new Object[] { cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex() });
				return null;
			}
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return null;
			}
			if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				return cell.getBooleanCellValue();
			}
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return cell.getNumericCellValue();
			}
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			}
		} catch (IllegalStateException e) {
			LOGGER.error(e.getLocalizedMessage());
			LOGGER.error("Read cell error. Sheet: {}, row: {}, column: {}",
					new Object[] { cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex() });
			throw new ExcelException(e);
		}
		return null;
	}

	/**
	 * 以指定ExcelCell代表的单元格为左上角，将一批数据写入工作表
	 * 
	 * @param topLeftCell
	 * @param data
	 */
	public void writeRange(ExcelCell topLeftCell, List<Object[]> data) {
		writeRange(getSheet(topLeftCell), topLeftCell.getRowIndex(), topLeftCell.getColumnIndex(), data);
	}

	private void writeRange(Sheet sheet, int rowFrom, int colFrom, List<Object[]> data) {
		int rowIndex = rowFrom;
		for (Object[] dataRow : data) {
			Row row = sheet.createRow(rowIndex);
			int columnIndex = colFrom;
			for (Object cellValue : dataRow) {
				Cell cell = row.createCell(columnIndex);
				setCellValue(cell, cellValue);
				columnIndex++;
			}
			rowIndex++;
		}
	}

	/**
	 * 将数据写入指定的Excel单元格中。
	 * 
	 * @param excelCell
	 * @param value
	 */
	public void writeCell(ExcelCell excelCell, Object value) {
		Row row = getSheet(excelCell).createRow(excelCell.getRowIndex());
		Cell cell = row.createCell(excelCell.getColumnIndex());
		setCellValue(cell, value);
	}

	private void setCellValue(Cell cell, Object data) {
		if (data == null) {
			cell.setCellValue("");
			return;
		}
		if (data instanceof Date) {
			cell.setCellValue((Date) data);
			cell.setCellStyle(getDateStyle(DATE_FORMAT, cell.getRow().getSheet().getWorkbook()));
			return;
		}
		if (data instanceof Boolean) {
			cell.setCellValue(((Boolean) data).booleanValue());
			return;
		}
		if (data instanceof Number) {
			cell.setCellValue(((Number) data).doubleValue());
			return;
		}
		cell.setCellValue(data.toString());
	}

	private CellStyle getDateStyle(String dateFormat, Workbook workbook) {
		DataFormat format = workbook.createDataFormat();
		CellStyle result = workbook.createCellStyle();
		result.setDataFormat(format.getFormat(dateFormat));
		return result;
	}

	public void outputTo(OutputStream out) {
		try {
			workbook.write(out);
		} catch (IOException e) {
			throw new ExcelException(e);
		}
	}

	public void outputTo(File file) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			workbook.write(out);
		} catch (IOException e) {
			throw new ExcelException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public int getSheetCount() {
		return workbook.getNumberOfSheets();
	}
}
