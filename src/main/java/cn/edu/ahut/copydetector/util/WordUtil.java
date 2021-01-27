/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.FileInputStream;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/27 19:59
 * @description
 */
@Slf4j
public class WordUtil {

	private static final String DOC = "doc";
	private static final String DOCX = "docx";


	public static String readWord(String path) {
		String buffer = "";
		try {
			if (path.endsWith(DOC)) {
				FileInputStream is = new FileInputStream(path);
				WordExtractor ex = new WordExtractor(is);
				buffer = ex.getText();
				is.close();
			} else if (path.endsWith(DOCX)) {
				OPCPackage opcPackage = POIXMLDocument.openPackage(path);
				POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
				buffer = extractor.getText();
				opcPackage.close();
			} else {
				log.debug("此文件不是word文件！");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		//new
		return buffer;
	}

}
