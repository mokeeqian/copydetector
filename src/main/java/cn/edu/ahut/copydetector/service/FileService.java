/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service;

import cn.edu.ahut.copydetector.entity.File;
import cn.edu.ahut.copydetector.entity.LayuiDtree;
import cn.edu.ahut.copydetector.entity.PageBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FileService {
	/**
	 * 查询学生以往作业
	 */
	PageBean<File> selectOldSubmitted(Integer submitter, int page, int limit);

	/**
	 * 查询最近批改
	 */
	PageBean<File> selectRecent(Integer owner, int page, int limit, String modern);

	/**
	 * 展示系统文件列表
	 */
	PageBean<Map<String, Object>> showFileList(String path, int page, int limit);

	/**
	 * 教师注册时，新建的教师文件夹
	 */
	int newTeacherFile(String username, String realname, Integer id);

	/**
	 * 创建文件或文件夹时，往数据库添加相应记录，支持批量操作
	 * paramMap包含(Collection<MultipartFile> files, String path, Integer owner, Integer submitter)
	 */
	int addFiles(Collection<MultipartFile> files, String path , Integer submitter);

	/**
	 * 添加文件夹
	 */
	Integer addDirectory(String name, String path, Integer owner);

	/**
	 * 更新文件属性时，更新数据库相关记录，支持批量操作
	 * 动态更新
	 * 更新后的name文件名；path物理路径；size文件大小；updateTime修改时间；status文件状态
	 */
	int updateFiles(HashMap<String, Object> updateParam, String action);

	/**
	 * 删除文件
	 */
	int deleteFile(HashMap<String, Object> param);

	/**
	 * 删除教师及文件夹下所有文件
	 */
	int deleteTeacherFiles(Integer owner);

	/**
	 * 删除学生未通过的作业
	 */
	int deleteUnPassedFiles(Integer submitter, Integer status);

	/**
	 * 文档simHash查重，返回File集合中，每个File对象有一个都有一个映射记录着和其他文档的海明距离
	 */
	List<LayuiDtree> checkMethod(String[] names, String path);
}

