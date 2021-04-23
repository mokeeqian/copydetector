/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.util;

import java.io.*;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/4/22 11:22
 * @description
 */
public class FileIOUtil {
	//将str以指定编码方式，写入文件
	public static void saveFile(File outfile, String str, String encode) {
		BufferedWriter fr = null;
		try {
			fr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile, true), encode));
			fr.write(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fr != null)
					fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
