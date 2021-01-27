/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.dao;

import cn.edu.ahut.copydetector.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface FileDao {

	/**
	 * 检查数据库中是否有相同记录
	 * @param path
	 * @param name
	 * @return
	 */
	File checkFile(@Param("name")String name, @Param("path")String path);

	/**
	 * 同一目录下的所有File对象
	 * @param path
	 * @return
	 */
	List<File> checkFiles(String path);

	/**
	 * 查询同提交人的所有文件
	 * @param param
	 * @return
	 */
	List<File> selectFileBySubmitter(HashMap<String, Object> param);

	/**
	 * 查询同拥有人的所有文件
	 * @param param
	 * @return
	 */
	List<File> selectFileByOwner(HashMap<String, Object> param);

	/**
	 * 根据id群查询文件群
	 * @param ids
	 * @return
	 */
	List<File> selectFileByIds(List<Integer> ids);

	/**
	 * 相同提交人的总记录数
	 * @param submitter
	 * @return
	 */
	int countBySubmitter(Integer submitter);

	/**
	 * 相同拥有者的总记录数
	 * @param owner
	 * @return
	 */
	int countByOwner(Integer owner);

	/**
	 * 增加文件
	 * @param file
	 * @return
	 */
	Integer addFile(File file);

	/**
	 * 更新文件,作业状态
	 * @param file
	 * @return
	 */
	Integer updateFile(Map<String,Object> file);

	/**
	 * 批量增加文件
	 * @param file
	 * @return
	 */
	Integer addFiles(List<File> file);

	/**
	 * 批量更新文件
	 * @param file
	 * @return
	 */
	Integer updateFiles(List<File> file);

	/**
	 * 删除文件
	 * @param file
	 * @return
	 */
	Integer deleteFile(File file);

	/**
	 * 删除文件（根据owner）
	 * @param owner
	 * @return
	 */
	Integer deleteFileByOwner(Integer owner);

	/**
	 * 删除文件（根据submitter和status）
	 * @param submitter
	 * @param status
	 * @return
	 */
	Integer deleteFilesByStatus(@Param("submitter")Integer submitter, @Param("status")Integer status);
}
