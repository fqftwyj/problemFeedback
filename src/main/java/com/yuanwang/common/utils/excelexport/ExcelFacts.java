/**
 *
 */
package com.yuanwang.common.utils.excelexport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;


public class ExcelFacts {
	private final String[] cellNames;
	private final String[] orderedAttrs;

	private final List<? extends Object> dataList;
	private final String sheetName;
	private final String strHeader;
	private final Map<String, String> header;
	private final Map<String, IChange> replaceStrategyMap;
	private final List<String> appendContent;
	private final short[] columnWidths;

	private List<List<? extends Object>> splitList = new ArrayList<List<? extends Object>>(
			10);

	private Workbook wb = null;

	public static class Builder {
		// Required parameters
		private final String[] cellNames;
		private final String[] orderedAttrs;

		// Optional parameters - initialized to default values
		private List<? extends Object> dataList = null;
		private String sheetName = null;
		private String strHeader = null;
		private Map<String, String> header = null;
		private Map<String, IChange> replaceStrategyMap = null;
		private List<String> appendContent = null;
		private short[] columnWidths = null;

		public Builder(String[] cellNames, String[] orderedAttrs) {
			this.cellNames = cellNames;
			this.orderedAttrs = orderedAttrs;
		}

		public Builder dataList(List<? extends Object> dataList) {
			this.dataList = dataList;
			return this;
		}

		public Builder sheetName(String sheetName) {
			this.sheetName = sheetName;
			return this;
		}

		public Builder header(String header) {
			this.strHeader = header;
			return this;
		}

		public Builder header(Map<String, String> header) {
			this.header = header;
			return this;
		}

		public Builder replaceStrategyMap(
				Map<String, IChange> replaceStrategyMap) {
			this.replaceStrategyMap = replaceStrategyMap;
			return this;
		}

		public Builder appendContent(List<String> content){
			this.appendContent = content;
			return this;
		}

		public Builder columnWidths(short[] columnWidths) {
			this.columnWidths = columnWidths;
			return this;
		}

		public ExcelFacts build() {
			return new ExcelFacts(this);
		}
	}

	private ExcelFacts(Builder builder) {
		dataList = builder.dataList;
		cellNames = builder.cellNames;
		sheetName = builder.sheetName;
		strHeader = builder.strHeader;
		header = builder.header;
		orderedAttrs = builder.orderedAttrs;
		replaceStrategyMap = builder.replaceStrategyMap;
		appendContent = builder.appendContent;
		columnWidths = builder.columnWidths;
	}

	public void addData(List<? extends Object> smallList) {
		this.splitList.add(smallList);
	}

	private List<List<? extends Object>> getSplitList() {
		List<List<? extends Object>> allList = null;
		if (this.splitList.isEmpty()) {
			allList = ExcelService.splitList(this.dataList);
		} else { // 调用了addData的时候，this.splitList有值，外部调用者已经分隔好，
			// 不再调用splitList方法
			allList = new ArrayList<List<? extends Object>>();
			allList.add(this.dataList);
			allList.addAll(this.splitList);
		}
		return allList;
	}

	private HttpServletResponse getResponse(String type,HttpServletRequest request,HttpServletResponse response) {
		response.reset();
		response.setCharacterEncoding("UTF-8");
		if (".zip".equals(type)) {
			response.setContentType("application/x-zip-compressed");
		} else {
			response.setContentType("application/vnd.ms-excel");
		}
		response.setHeader("Content-Disposition", "attachment;filename="
				+ ExcelService.encodeFileName(sheetName,request) + type);
		return response;
	}

	public void exportXLS(HttpServletRequest req,HttpServletResponse resp) {
		List<List<? extends Object>> splitList = getSplitList();
		HttpServletResponse response = getResponse(".xls",req,resp);
		try {
			OutputStream os = response.getOutputStream();
			int i = 1;
			wb = new HSSFWorkbook();
			if (splitList.size()==0) {
				this.createExcel(splitList,0);
			}else {
				for (List<? extends Object> smallList : splitList) {
					this.createExcel(smallList, i++);
				}
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			wb.write(baos);

			os.write(baos.toByteArray());
			baos.flush();
			baos.close();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exportZIP(HttpServletRequest req,HttpServletResponse resp) {
		List<List<? extends Object>> splitList = getSplitList();
		HttpServletResponse response = getResponse(".zip",req,resp);
		try {
			OutputStream os = response.getOutputStream();
			ZipArchiveOutputStream zout = new ZipArchiveOutputStream(os);
			if (splitList.isEmpty()) {
				zout.close();
				os.close();
				return;
			}
			zout.setEncoding("GBK");

			try {
				int length = splitList.size();
				if(length == 1) {
					wb = new HSSFWorkbook();
					this.createExcel(splitList.get(0), 0);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					wb.write(baos);
					baos.flush();
					ZipArchiveEntry entry = new ZipArchiveEntry(sheetName + ".xls");
					zout.putArchiveEntry(entry);
					zout.write(baos.toByteArray());
					baos.close();
				} else {
					int i = 1;
					for (List<? extends Object> smallList : splitList) {
						wb = new HSSFWorkbook();
						this.createExcel(smallList, i);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						wb.write(baos);
						baos.flush();
						ZipArchiveEntry entry = new ZipArchiveEntry(sheetName
								+ (i++) + ".xls");
						zout.putArchiveEntry(entry);
						zout.write(baos.toByteArray());
						baos.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				zout.closeArchiveEntry();
				zout.close();
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * zip压缩加密码
	 * @param password
	 */
	public void exportZIP(String password,HttpServletRequest req,HttpServletResponse resp){
		List<List<? extends Object>> splitList = getSplitList();
		HttpServletResponse response = getResponse(".zip",req,resp);
		try {
			OutputStream os = response.getOutputStream();
			if (splitList.isEmpty()) {
				//没有导出数据时处理异常
				ZipArchiveOutputStream zout = new ZipArchiveOutputStream(os);
				zout.close();
				os.close();
				return;
			}
			ZipOutputStream outputStream=new ZipOutputStream(os,new ZipModel());
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); //压缩方式 
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); //压缩级别
			parameters.setSourceExternalStream(true);//支持流输入
			if(!isEmpty(password)){
				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);//加密方式
				parameters.setPassword(password);//设置加密密码
			}
			try {
				int length = splitList.size();
				if(length == 1) {
					wb = new HSSFWorkbook();
					this.createExcel(splitList.get(0), 0);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					wb.write(baos);
					baos.flush();
					try {
						parameters.setFileNameInZip(sheetName+".xls");//设置压缩文件内文件类型
						outputStream.putNextEntry(null, parameters);
					} catch (ZipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					outputStream.write(baos.toByteArray());
					try {//清除下标
						outputStream.closeEntry();
					} catch (ZipException e) {
						e.printStackTrace();
					}
					baos.close();

				} else {
					int i = 1;
					for (List<? extends Object> smallList : splitList) {
						wb = new HSSFWorkbook();
						this.createExcel(smallList, i);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						wb.write(baos);
						baos.flush();
						try {
							parameters.setFileNameInZip(sheetName+(i++)+".xls");
							outputStream.putNextEntry(null, parameters);
						} catch (ZipException e) {
							e.printStackTrace();
						}
						outputStream.write(baos.toByteArray());
						try {//清除下标
							outputStream.closeEntry();
						} catch (ZipException e) {
							e.printStackTrace();
						}
						baos.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(outputStream!=null){
					outputStream.flush();
					try {
						outputStream.finish();
					} catch (ZipException e) {
						e.printStackTrace();
					}
					outputStream.close();
					os.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void createExcel(List<? extends Object> dataList, int splitIndex) {
		Sheet sheet = wb.createSheet();

		if(splitIndex == 0) {
			wb.setSheetName(wb.getNumberOfSheets() - 1,
					getSafeSheetName(sheetName));
		} else {
			wb.setSheetName(wb.getNumberOfSheets() - 1,
					getSafeSheetName(sheetName, splitIndex));
		}

		if ( null != columnWidths) {
			for (int i=0; i<columnWidths.length; i++) {
				sheet.setColumnWidth(i,columnWidths[i]* 256);
			}
		}
		int index = setTitle(cellNames, sheet);

		Object[] rowValues = new Object[this.orderedAttrs.length];
		Field[] fields = null;
		boolean isMap = false;
		for (int i = 0, size = dataList.size(); i < size; i++) {
			Object dataObj = dataList.get(i);
			if (i == 0) {
				if(dataObj instanceof Map) {
					isMap = true;
				}
				if(!isMap) {
					fields = dataObj.getClass().getDeclaredFields();
					AccessibleObject.setAccessible(fields, true);
				}
			}
			Map<String, Object> fieldValues;
			if(isMap){
				fieldValues = ExcelService.getFieldValues((Map<String,Object>)dataObj,this.replaceStrategyMap);
			} else {
				fieldValues = ExcelService.getFieldValues(
						fields, dataObj, this.replaceStrategyMap);

			}
			Row row = sheet.createRow(index++);
			for (int j = 0; j < this.orderedAttrs.length; j++) {
				rowValues[j] = fieldValues.get(this.orderedAttrs[j]);
				if (rowValues[j] == null) {
					rowValues[j] = "";
				}

				String rowValueStr = rowValues[j].toString();
				if(rowValueStr.matches("[1-9][0-9]*|0")){
					Cell cell = row.createCell(j);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(rowValueStr.toString());
				}else{
					row.createCell(j).setCellValue(rowValueStr);
				}
			}

		}
		if(null == this.appendContent || this.appendContent.isEmpty()) {
			return;
		}

		int LastRow = sheet.getLastRowNum()+1;
		for(int i=0; i<this.appendContent.size(); i++){
			Row row = sheet.createRow(LastRow+i);
			row.createCell(0).setCellValue(this.appendContent.get(i));
		}
	}

	/**
	 * 获取安全的工作表名
	 *
	 * @param sheetName
	 *            工作表名
	 * @param part
	 *            工作表份数
	 * @return 字符安全的工作表名
	 */
	private String getSafeSheetName(String sheetName, int part) {
		return WorkbookUtil.createSafeSheetName(sheetName) + "_" + part;
	}

	private String getSafeSheetName(String sheetName) {
		return WorkbookUtil.createSafeSheetName(sheetName);
	}

	/**
	 * 设置工作表第一行标题内容与样式
	 *
	 * @param cellNames
	 *            标题列表
	 * @param sheet
	 *            工作表
	 */
	private int setTitle(String[] cellNames, Sheet sheet) {
		int index = 0;
		if (null != strHeader) {
			index = insertHeader(strHeader, index);
		}

		if (null != header) {
			index = insertHeader(header, index);
		}

		// 要让数据表项翻页时一直显示，就采用冻结窗格
		// 冻结第一行：标题行
		// sheet.createFreezePane(0, 1, 0, 1);
		sheet.createFreezePane(0, index + 1); // 边界为数据部分的边界
		Row row0 = sheet.createRow(index++);
		// 标题行格式
		CellStyle cellStyle = wb.createCellStyle();

		// 水平对齐方式-居中
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

		// 允许自动换行
		// cellStyle.setWrapText(true);

		// 垂直对齐方式-靠下
		// cellStyle.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);

		// 前景填充
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		// 设置边框类型
		setBorderType(cellStyle, CellStyle.BORDER_THIN);
		// 设置边框颜色
		setBorderColor(cellStyle, IndexedColors.BLACK.getIndex());

		// 标题栏字体加粗
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);

		// 横向设置单元格标题
		for (int i = 0; i < cellNames.length; i++) {
			Cell cell = row0.createCell(i);
			cell.setCellValue(cellNames[i]);
			cell.setCellStyle(cellStyle);
		}

		return index;
	}

	/**
	 * 设置边框类型
	 *
	 * @param cellStyle
	 *            单元格样式
	 * @param type
	 *            单元格边框类型
	 */
	private void setBorderType(CellStyle cellStyle, short type) {
		cellStyle.setBorderTop(type);
		cellStyle.setBorderBottom(type);
		cellStyle.setBorderLeft(type);
		cellStyle.setBorderRight(type);
	}

	/**
	 * 设置边框颜色
	 *
	 * @param cellStyle
	 *            单元格样式
	 * @param color
	 *            单元格边框颜色
	 */
	private void setBorderColor(CellStyle cellStyle, short color) {
		cellStyle.setTopBorderColor(color); // 上边框
		cellStyle.setBottomBorderColor(color); // 下边框
		cellStyle.setLeftBorderColor(color); // 左边框
		cellStyle.setRightBorderColor(color); // 右边框
	}

	/**
	 * 获取居中样式
	 *
	 * @return
	 */
	private CellStyle getAlignCenterStyle() {
		CellStyle cellStyle = wb.createCellStyle();
		// 居中对齐
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	/**
	 * 加粗样式
	 *
	 * @return
	 */
	private CellStyle getBoldWeight() {
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
		return cellStyle;
	}

	/**
	 * 插入string表头
	 */
	private int insertHeader(String header, int index) {
		if (wb.getNumberOfSheets() == 0) {
			wb.createSheet();
		}
		Sheet sheet = wb.getSheetAt(index);

		Row row = sheet.createRow(0);

		Cell keyCell = row.createCell(0);

		CellStyle cellStyle = getBoldWeight();

		cellStyle = getAlignCenterStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

		Font font = wb.createFont();
		font.setFontHeightInPoints((short)16);
		cellStyle.setFont(font);
		keyCell.setCellStyle(cellStyle);

		keyCell.setCellValue(header);

		sheet.addMergedRegion(new CellRangeAddress(0, // first row(0-based)
				0, // last row(0-based)
				0, // first column(0-based)
				13 // last column(0-based)
		));
		return ++index;
	}

	/**
	 * 插入map表头
	 */
	private int insertHeader(Map<String, String> header, int index) {
		if (wb.getNumberOfSheets() == 0) {
			wb.createSheet();
		}
		Sheet sheet = wb.getSheetAt(0);

		Row row = sheet.createRow(index);

		Cell keyCell = row.createCell(0);
		Cell valueCell = row.createCell(1);

		CellStyle cellStyle = getBoldWeight();
		keyCell.setCellStyle(cellStyle);

		cellStyle = getAlignCenterStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

		Entry<String, String> keyValue = header.entrySet().iterator().next();
		String key = keyValue.getKey();
		String value = keyValue.getValue();
		keyCell.setCellValue(key);

		valueCell.setCellValue(value);
		valueCell.setCellStyle(cellStyle);

		sheet.addMergedRegion(new CellRangeAddress(0, // first row(0-based)
				0, // last row(0-based)
				1, // first column(0-based)
				13 // last column(0-based)
		));
		return ++index;
	}

	/**
	 * 判断字符串是否为空
	 * @param str  NULL null 空串
	 * @return true：空  false：不为空
	 */
	private boolean isEmpty(String str){

		if(str==null || str.equals("") || str.equals("null")){
			return true;
		}

		return false;
	}
}
