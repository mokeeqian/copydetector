/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.util;

import cn.edu.ahut.copydetector.constant.OtherConstant;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/27 19:58
 * @description
 */
public class ExcelUtil<T> {

	/**
	 * 记录excel表格的表头
	 */
	private static final String XLS = "xls";
	private static final String XLSX = "xlsx";
	private List<T> models;
	private Class<T> t;

	public ExcelUtil( Class<T> t) {
		models = new ArrayList<T>();
		this.t = t;
	}

	public List<T> explain(String filename) throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
		if (filename == null || "".equals(filename)){
			throw new RuntimeException("文件名为空");
		}

		if(!filename.endsWith(XLS) && !filename.endsWith(XLSX)){
			throw new IOException(filename + "不是excel文件");
		}

		FileInputStream in = new FileInputStream(filename);
		/**
		 * 遇到直接改文件后缀名，打开excel警告的行为，必须另存为其中一种格式后再操作
		 */
		if (filename.endsWith(XLS)){
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			for (int s = 0; s < workbook.getNumberOfSheets(); s++){
				HSSFSheet sheet = workbook.getSheetAt(s);
				HSSFRow firstRow = sheet.getRow(0);
				String name;
				for(int r = 1; r < sheet.getLastRowNum(); r++){
					HSSFRow row = sheet.getRow(r);
					Map<String, Object> tmpBeanMap = new HashMap<>();
					for (int c = 0; c < row.getLastCellNum(); c++){
						HSSFCell cell = row.getCell(c);
						name = OtherConstant.SHEET_HEAD.get(firstRow.getCell(c).getStringCellValue());
						if (name != null){
							cell.setCellType(CellType.STRING);
							tmpBeanMap.put(name, cell.toString());
						}
					}
					T ta = (T) t.newInstance();
					BeanUtils.populate(ta, tmpBeanMap);
					models.add(ta);
				}
			}
		}else {
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			for (int s = 0; s < workbook.getNumberOfSheets(); s++){
				XSSFSheet sheet = workbook.getSheetAt(s);
				XSSFRow firstRow = sheet.getRow(0);
				String name;
				for(int r = 1; r < sheet.getLastRowNum(); r++){
					XSSFRow row = sheet.getRow(r);
					Map<String, Object> tmpBeanMap = new HashMap<>();
					for (int c = 0; c < row.getLastCellNum(); c++){
						XSSFCell cell = row.getCell(c);
						name = OtherConstant.SHEET_HEAD.get(firstRow.getCell(c).getStringCellValue());
						if (name != null){
							cell.setCellType(CellType.STRING);
							tmpBeanMap.put(name, cell.toString());
						}
					}
					T ta = (T) t.newInstance();
					BeanUtils.populate(ta, tmpBeanMap);
					models.add(ta);
				}
			}
		}
		in.close();
		return models;
	}


}
