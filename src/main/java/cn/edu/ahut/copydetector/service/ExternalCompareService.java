/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service;

import cn.edu.ahut.copydetector.util.FileIOUtil;
import cn.edu.ahut.copydetector.util.TextExtractUtil;
import jplag.Program;
import jplag.options.CommandLineOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/4/22 10:57
 * @description 外部扩展查重服务
 */
@Service
@Slf4j
public class ExternalCompareService {
	private static final File resource = new File("static-file-folder", "resource");
	private static final File result = new File("static-file-folder", "result");


	public String[] jplag(String lang, int simValue, String sessionId) {
		cleanResult(new File(result, sessionId));
		if ("doc".equals(lang))
			jplagDocConvert(sessionId);
		String[] ret;
		try {
			ArrayList<String> args = new ArrayList<>();
			args.add("-l");
			if ("java".equals(lang))
				args.add("java19");
			else if ("doc".equals(lang))
				args.add("text");
			else
				args.add(lang);
			args.add("-r"); //指定结果存放的路径
			args.add(new File(result, sessionId).getPath());
			args.add("-m");  //设置相似度检查门限参数值
			args.add(simValue + "%");
			if ("doc".equals(lang))
				args.add(new File(result, sessionId+"-converted").getPath());
			else
				args.add(new File(resource, sessionId).getPath());
			String[] toPass = new String[args.size()];
			toPass = args.toArray(toPass);

			// 调用jplag
			new Program(new CommandLineOptions(toPass)).run();
		} catch (Exception e) {
			System.out.println(e.toString());
			ret = new String[1];
			ret[0] = "error";
			return ret;
		}
		ret = new String[2];
		File file = new File(result, sessionId);
		ret[0] = convertMatchesFileToString(new File(file, "matches_avg.csv"));
		ret[1] = convertMatchesFileToString(new File(file, "matches_max.csv"));
		return ret;
	}

	/**
	 * 查重结果转为String
	 * @param file
	 * @return
	 */
	private String convertMatchesFileToString(File file) {
		byte[] fileContent = new byte[(int) file.length()];
		try {
			FileInputStream in = new FileInputStream(file);
			//noinspection ResultOfMethodCallIgnored
			in.read(fileContent);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(fileContent);
	}

	private void cleanResult(File dir) {
		File[] files = dir.listFiles();
		if (files != null)
			for (File file : files) {
				//noinspection ResultOfMethodCallIgnored
				file.delete();
			}
	}

	private void jplagDocConvert(String sessionId) {
		File convertDir = new File(result, sessionId+"-converted");
		if (convertDir.exists()) cleanResult(convertDir);
		else { //noinspection ResultOfMethodCallIgnored
			convertDir.mkdir();
		}
		File resourceDir = new File(resource, sessionId);
		String[] files = resourceDir.list();
		if (files != null) for (String file : files) {
			FileIOUtil.saveFile(
					new File(convertDir, file + ".txt"),
					TextExtractUtil.convertToText(new File(resourceDir, file)),
					"utf-8");
		}
	}

}
