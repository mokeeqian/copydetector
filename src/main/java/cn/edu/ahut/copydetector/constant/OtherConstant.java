/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.constant;

import org.springframework.util.ClassUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/24 15:14
 * @description
 */
public final class OtherConstant {
	/**
	 * 日期格式化器
	 */
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * Excel表格表头解析
	 */
	public static final Map<String, String> SHEET_HEAD = new HashMap<>();
	public static final String REALPATH;
	public static final String NOT_SEPARATOR;
	public static final String SEPARATOR;

	static {
		SHEET_HEAD.put("姓名", "realname");
		SHEET_HEAD.put("学号", "username");
		SHEET_HEAD.put("系别", "department");
		SHEET_HEAD.put("专业", "major");
		SHEET_HEAD.put("工号", "username");

		// /target/classes/root 作为资源根目录
		REALPATH = Objects.requireNonNull(
				Objects.requireNonNull(
						ClassUtils.getDefaultClassLoader()
				).getResource("")
				).getPath()
				.replaceAll("%20", " ") + "root";
		if (File.separator.equals("/")){
			SEPARATOR = File.separator;
			NOT_SEPARATOR = Matcher.quoteReplacement("\\");
		}else {
			SEPARATOR = Matcher.quoteReplacement(File.separator);
			NOT_SEPARATOR = "/";
		}
	}

}