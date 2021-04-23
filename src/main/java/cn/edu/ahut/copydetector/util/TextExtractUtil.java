/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.util;


import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/4/22 11:23
 * @description 文本提取工具
 */
public class TextExtractUtil {
	public static String convertToText(File f) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(f);
			Tika tika = new Tika();
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName()); //避免gbk编码txt文本提取错误
			return tika.parseToString(new FileInputStream(f),metadata);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
