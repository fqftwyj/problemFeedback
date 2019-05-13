package com.yuanwang.common.utils.excelexport;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtil {
	/**
	 * 获得excel的cell值
	 * @param cell
	 * @return
	 */
	public static Object getCellString(Cell cell){
		Object cellValue ="";
		if(null!=cell){
		int	type=cell.getCellType();
		switch (type) {
			case Cell.CELL_TYPE_STRING:
				cellValue=cell.getStringCellValue();				
				break;
			case Cell.CELL_TYPE_NUMERIC:
				
				if(HSSFDateUtil.isCellDateFormatted(cell)){
					Date d = cell.getDateCellValue();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					cellValue=format.format(d);
				}else {
					DecimalFormat decimalFormat = new DecimalFormat("########");
					cellValue=decimalFormat.format(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_BLANK:
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue=cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_ERROR:
				break;
			case Cell.CELL_TYPE_FORMULA:
				cellValue=cell.getNumericCellValue();		
				break;
			default:
				break;
			}
		}
		return cellValue;
	}
}
