package com.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReadExcel {
	public static final String SAMPLE_XLSX_FILE_PATH = "E:\\credentials.xlsx";
	public static Map<String, String> map = new HashMap<String, String>();

	public static Map<String, String> read(String fileName) throws EncryptedDocumentException, InvalidFormatException, IOException {

		Workbook workbook = WorkbookFactory.create(new File(fileName));

		Sheet sheet = workbook.getSheetAt(0);
		Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if(row.getCell(0)!=null && row.getCell(1)!=null){
				map.put(row.getCell(0).toString(), row.getCell(1).toString());
			}
			
		}
		workbook.close();
		return map;
	}

}
